package examcalendar.optimizer.persistence;

import examcalendar.optimizer.common.persistence.AbstractSolutionImporter;
import examcalendar.optimizer.common.persistence.SolutionDao;
import examcalendar.optimizer.domain.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Gustavo on 02/07/2016.
 */
public class ExaminationDBImporter extends AbstractSolutionImporter {
    public ExaminationDBImporter(SolutionDao solutionDao) {
        super(solutionDao);
    }

    public ExaminationDBImporter(boolean withoutDao) {
        super(withoutDao);
    }

    @Override
    public String getInputFileSuffix() {
        return "txt";
    }

    @Override
    public Examination readSolution(File inputFile) {
        // File contents: Request ID
        Scanner s = null;
        int id;
        try {
            s = new Scanner(inputFile);
            if (!s.hasNextInt()) return null;
            id = s.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (s != null) s.close();
        }
        return readSolution(id);
    }

    public Examination readSolution(int requestId) {
        Examination examination = new Examination();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=UTC", "root", ""); // TODO (hardcoded)

            RequestConfig requestConfig = readRequestConfig(requestId, conn);
            if (requestConfig == null) return null;

            List<Professor> professors = readProfessors(requestConfig.creator, conn);

            List<Topic> topics = readTopics(requestConfig.creator, conn, professors);
            if (topics == null) return null;

            List<Exam> exams = readExams(requestConfig.creator, conn, topics);
            if (exams == null) return null;

            List<Student> students = readStudents(requestConfig.creator, conn);

            List<Room> rooms = readRooms(requestConfig.creator, conn);

            List<Period> periods = generatePeriods(requestConfig);

            List<ProfessorUnavailable> professorUnavailables = readProfessorUnavailables(requestConfig.creator, conn, requestConfig.startingDate, periods, professors);
            if (professorUnavailables == null) return null;

            List<RoomPeriod> roomPeriods = generateRoomPeriods(rooms, periods);
            removeUnavailableRoomPeriods(requestConfig.creator, conn, roomPeriods);

            examination.setTopicList(topics);
            examination.setExamList(exams);
            examination.setRoomList(rooms);
            examination.setPeriodList(periods);
            examination.setProfessorUnavailableList(professorUnavailables);
            examination.setRoomPeriodList(roomPeriods);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
        }

        return examination;
    }

    private RequestConfig readRequestConfig(int requestId, Connection conn) throws SQLException {
        RequestConfig rc = new RequestConfig();
        InstitutionParametrization ip = new InstitutionParametrization();
        rc.institutionParametrization = ip;

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM requests WHERE id = ?");
        ps.setInt(1, requestId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        rc.creator = rs.getInt("creator");
        rc.timeout = rs.getInt("timeout");
        rc.normalSeasonDuration = rs.getInt("normalSeasonDuration");
        rc.appealSeasonDuration = rs.getInt("appealSeasonDuration");
        rc.startingDate = rs.getDate("startingDate");
        ip.setMinDaysBetweenSameTopicExams(rs.getInt("minDaysBetweenSameTopicExams"));
        ip.setMinDaysBetweenSameYearExams(rs.getInt("minDaysBetweenSameYearExams"));
        ip.setDifficultyPenalty(rs.getFloat("difficultyPenalty"));
        ip.setSpreadPenalty(rs.getFloat("spreadPenalty"));

        return rc;
    }

    private List<Professor> readProfessors(int creator, Connection conn) throws SQLException {
        List<Professor> professors = new ArrayList<Professor>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM professors WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Professor professor = new Professor();
            professor.setId(rs.getInt("id"));
            professor.setName(rs.getString("name"));
            professor.setAcronym(rs.getString("acronym"));
            professor.setCod(rs.getString("cod"));
            professors.add(professor);
        }

        return professors;
    }

    private List<Topic> readTopics(int creator, Connection conn, List<Professor> professors) throws SQLException {
        List<Topic> topics = new ArrayList<Topic>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM topics WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int regentId = rs.getInt("regent");
            Professor regent = null;
            for (Professor professor : professors) {
                if (professor.getId() == regentId) {
                    regent = professor;
                    break;
                }
            }

            if (regent == null) return null; // Regent does not exist in the database

            Topic topic = new Topic();
            topic.setId(rs.getInt("id"));
            topic.setName(rs.getString("name"));
            topic.setAcronym(rs.getString("acronym"));
            topic.setCode(rs.getString("code"));
            topic.setYear(rs.getInt("year"));
            topic.setDifficulty(rs.getInt("difficulty"));
            topic.setRegent(regent);

            topics.add(topic);
        }

        return topics;
    }

    private List<Exam> readExams(int creator, Connection conn, List<Topic> topics) throws SQLException {
        List<Exam> exams = new ArrayList<Exam>();

        PreparedStatement ps = conn.prepareStatement("SELECT *, (SELECT count(*) FROM studenttopic WHERE studenttopic.topic = exams.topic) AS numStudents FROM exams WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Exam exam = new Exam();
            int topicId = rs.getInt("topic");
            Topic topic = null;
            for (Topic t : topics) {
                if (t.getId() == topicId) {
                    topic = t;
                    break;
                }
            }
            if (topic == null) return null; // Topic does not exist in the database

            exam.setTopic(topic);
            if (rs.getBoolean("normal"))
                exam.setNormal();
            else
                exam.setAppeal();
            exam.setPC(rs.getBoolean("pc"));
            exam.setNumStudents(rs.getInt("numStudents"));
            exams.add(exam);
        }

        return exams;
    }

    private List<Student> readStudents(int creator, Connection conn) throws SQLException {
        List<Student> students = new ArrayList<Student>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Student student = new Student();
            student.setName(rs.getString("name"));
            student.setCod(rs.getString("cod"));
            students.add(student);
        }

        return students;
    }

    private List<Room> readRooms(int creator, Connection conn) throws SQLException {
        List<Room> rooms = new ArrayList<Room>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM rooms WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Room room = new Room(rs.getString("cod"), rs.getInt("capacity"), rs.getBoolean("pc"));
            rooms.add(room);
        }

        return rooms;
    }

    private List<Period> generatePeriods(RequestConfig requestConfig) {
        List<Period> periods = new ArrayList<Period>();

        Date currentDay = new Date(requestConfig.startingDate.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(currentDay);
        for (int dayIndex = 0; dayIndex < requestConfig.normalSeasonDuration + requestConfig.appealSeasonDuration; dayIndex++) {
            Period period1 = new Period(dayIndex, PeriodTime.NINE_AM, dayIndex < requestConfig.normalSeasonDuration);
            Period period2 = new Period(dayIndex, PeriodTime.ONE_PM, dayIndex < requestConfig.normalSeasonDuration);
            Period period3 = new Period(dayIndex, PeriodTime.FIVE_PM, dayIndex < requestConfig.normalSeasonDuration);
            period1.setDate(c.getTime());
            period2.setDate(c.getTime());
            period3.setDate(c.getTime());

            periods.add(period1);
            periods.add(period2);
            periods.add(period3);

            c.add(Calendar.DATE, 1); // Advance to next day
            dayIndex++;
        }

        return periods;
    }

    private List<RoomPeriod> generateRoomPeriods(List<Room> rooms, List<Period> periods) {
        List<RoomPeriod> roomPeriods = new ArrayList<RoomPeriod>();
        for (Room room : rooms) {
            for (Period period : periods) {
                RoomPeriod roomPeriod = new RoomPeriod();
                roomPeriod.setRoom(room);
                roomPeriod.setPeriod(period);
                roomPeriods.add(roomPeriod);
            }
        }
        return roomPeriods;
    }

    private void removeUnavailableRoomPeriods(int creator, Connection conn, List<RoomPeriod> roomPeriods) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM roomPeriodUnavailable WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int roomId = rs.getInt("room");
            Date day = rs.getDate("day");
            int time = rs.getInt("time");
            for (int i = 0; i < roomPeriods.size(); i++) {
                RoomPeriod rp = roomPeriods.get(i);
                if (rp.getRoom().getId() == roomId && rp.getPeriod().getDate().equals(day) && rp.getPeriod().getTime().ordinal() == time) {
                    roomPeriods.remove(i);
                    i--;
                }
            }
        }
    }

    private List<ProfessorUnavailable> readProfessorUnavailables(int creator, Connection conn, java.util.Date startingDay, List<Period> periods, List<Professor> professors) throws SQLException {
        ArrayList<ProfessorUnavailable> professorUnavailables = new ArrayList<ProfessorUnavailable>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM professorunavailable WHERE creator = ?");
        ps.setInt(1, creator);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ProfessorUnavailable professorUnavailable = new ProfessorUnavailable();
            int professorId = rs.getInt("professor");
            Professor professor = null;
            for (Professor p : professors) {
                if (p.getId() == professorId) {
                    professor = p;
                    break;
                }
            }
            if (professor == null) return null; // Professor does not exist in the database

            Date periodDate = rs.getDate("date");
            int periodTime = rs.getInt("time");
            Period period = null;
            for (Period p : periods) {
                if (rs.getDate("day").equals(p.getDate()) && rs.getInt("time") == p.getTime().ordinal()) {
                    period = p;
                    break;
                }
            }
            if (period == null) return null; // Period does not exist in the database
            professorUnavailable.setPeriod(period);
            professorUnavailable.setProfessor(professor);
            professorUnavailables.add(professorUnavailable);
        }

        return professorUnavailables;
    }
}
