package examcalendar.optimizer.persistence;

import examcalendar.optimizer.domain.*;
import examcalendar.server.Server;
import org.optaplanner.examples.common.persistence.AbstractSolutionImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Gustavo on 02/07/2016.
 */
public class ExaminationDBImporter extends AbstractSolutionImporter {
    private RequestConfig requestConfig;
    private final Server server;

    public ExaminationDBImporter(Server server) {
        super();
        this.server = server;
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

    public Examination readSolution(int calendar) {
        Examination examination = new Examination();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/examcalendar?serverTimezone=UTC", "root", "root"); // TODO (hardcoded)

            requestConfig = readRequestConfig(calendar, conn);
            if (requestConfig == null) return null;

            List<Professor> professors = readProfessors(requestConfig.calendar, conn);

            List<Student> students = readStudents(requestConfig.calendar, conn);

            List<Topic> topics = readTopics(requestConfig.calendar, conn, students, professors);
            if (topics == null) return null;

            List<TopicProfessor> topicProfessors = readTopicProfessors(requestConfig.calendar, conn, topics, professors);

            List<Exam> exams = readExams(requestConfig.calendar, conn, topics);
            if (exams == null) return null;

            List<Room> rooms = readRooms(requestConfig.calendar, conn);

            List<Period> periods = generatePeriods(requestConfig);

            List<ProfessorUnavailable> professorUnavailables = readProfessorUnavailables(requestConfig.calendar, conn, requestConfig.startingDate, periods, professors);
            if (professorUnavailables == null) return null;

            List<RoomPeriod> roomPeriods = generateRoomPeriods(rooms, periods);
            removeUnavailableRoomPeriods(requestConfig.calendar, conn, roomPeriods);
            //fillRoomPeriods(calendar, conn, roomPeriods, exams);

            examination.setId(calendar);
            examination.setTopicList(topics);
            examination.setTopicProfessors(topicProfessors);
            examination.setExamList(exams);
            examination.setRoomList(rooms);
            examination.setPeriodList(periods);
            examination.setProfessorUnavailableList(professorUnavailables);
            examination.setRoomPeriodList(roomPeriods);
            examination.setInstitutionParametrization(requestConfig.institutionParametrization);

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

    private RequestConfig readRequestConfig(int calendar, Connection conn) throws SQLException {
        RequestConfig rc = new RequestConfig();
        InstitutionParametrization ip = new InstitutionParametrization();
        rc.institutionParametrization = ip;

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return null;

        rc.calendar = calendar;
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

    private List<Professor> readProfessors(int calendar, Connection conn) throws SQLException {
        List<Professor> professors = new ArrayList<Professor>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM professors WHERE calendar = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Professor professor = new Professor();
            professor.setId(rs.getInt("id"));
            professor.setName(rs.getString("name"));
            professor.setAcronym(rs.getString("acronym"));
            professor.setCode(rs.getString("cod"));
            professors.add(professor);
        }

        return professors;
    }

    private List<Topic> readTopics(int calendar, Connection conn, List<Student> students, List<Professor> professors) throws SQLException {
        List<Topic> topics = new ArrayList<Topic>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM topics WHERE calendar = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Topic topic = new Topic();
            topic.setId(rs.getInt("id"));
            topic.setName(rs.getString("name"));
            topic.setAcronym(rs.getString("acronym"));
            topic.setCode(rs.getString("code"));
            topic.setYear(rs.getInt("year"));
            topic.setDifficulty(rs.getInt("difficulty"));

            topics.add(topic);

            ps = conn.prepareStatement("SELECT student FROM studenttopic WHERE studenttopic.topic = ?");
            ps.setInt(1, topic.getId());
            ResultSet rs2 = ps.executeQuery();
            while (rs2.next()) {
                Student student = null;
                for (Student s : students) {
                    if (s.getId() == rs2.getInt("student")) {
                        student = s;
                        break;
                    }
                }
                if (student == null) return null; // Student not found
                topic.addStudent(student);
            }
        }

        return topics;
    }

    private List<TopicProfessor> readTopicProfessors(int calendar, Connection conn, List<Topic> topics, List<Professor> professors) throws SQLException {
        List<TopicProfessor> topicProfessors = new ArrayList<TopicProfessor>();
        for (Topic topic : topics) {
            PreparedStatement ps = conn.prepareStatement("SELECT professor FROM topicprofessor WHERE topicprofessor.topic = ?");
            ps.setInt(1, topic.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Professor professor = null;
                for (Professor p : professors) {
                    if (p.getId() == rs.getInt("professor")) {
                        professor = p;
                        break;
                    }
                }
                if (professor == null) return null; // Professor not found
                topic.addProfessor(professor);
                topicProfessors.add(new TopicProfessor(topic, professor));
            }
        }
        return topicProfessors;
    }

    private List<Exam> readExams(int calendar, Connection conn, List<Topic> topics) throws SQLException {
        List<Exam> exams = new ArrayList<Exam>();

        PreparedStatement ps = conn.prepareStatement("SELECT exams.*, (SELECT count(*) FROM studenttopic WHERE studenttopic.topic = exams.topic) AS numStudents FROM exams, topics WHERE topic = topics.id AND topics.calendar = ?");
        ps.setInt(1, calendar);
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

            exam.setId(rs.getInt("id"));
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

    private List<Student> readStudents(int calendar, Connection conn) throws SQLException {
        List<Student> students = new ArrayList<Student>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE calendar = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Student student = new Student();
            student.setName(rs.getString("name"));
            student.setCode(rs.getString("cod"));
            student.setId(rs.getInt("id"));
            students.add(student);
        }

        return students;
    }

    private List<Room> readRooms(int calendar, Connection conn) throws SQLException {
        List<Room> rooms = new ArrayList<Room>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM rooms WHERE calendar = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Room room = new Room(rs.getString("cod"), rs.getInt("capacity"), rs.getBoolean("pc"));
            room.setId(rs.getInt("id"));
            rooms.add(room);
        }

        return rooms;
    }

    public static void main(String[] args) {

       /* RequestConfig rc = new RequestConfig();
        rc.startingDate = new Date(System.currentTimeMillis());
        ExaminationDBImporter ex = new ExaminationDBImporter(true);

        List<Period> ret = ex.generatePeriods(rc);
        int[] x = new int[temp.size()];
        Arrays.fill(x,0);

        ArrayList<Integer> indexs = new ArrayList<>();
        for (Period aRet : ret) indexs.add(aRet.getDayIndex());

        for(int i = 0; i < temp.size(); i++){
            x[temp.get(ret.get(i).getDayIndex())]++;
        }

        //System.out.println(ret);
        System.out.println(temp.toString());
        System.out.println(Arrays.toString(x));
        System.out.println(indexs.toString());*/
    }

    // public static ArrayList<Integer> temp = new ArrayList<>();

    private Calendar pairCalendar = null;
    private Integer pairInteger = 0;

    private List<Period> generatePeriods(RequestConfig requestConfig) {
        List<Period> periods = new ArrayList<Period>();

        Date currentDay = new Date(requestConfig.startingDate.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(currentDay);
        int dayIndex = 0;

        while (dayIndex < requestConfig.normalSeasonDuration + requestConfig.appealSeasonDuration) {
            pairCalendar = null;
            skipToNextWorkingDayAux(c,dayIndex);
            if(pairCalendar != null) {
                c = pairCalendar;
                dayIndex = pairInteger;
            }

           // System.out.println(dayIndex);
            //temp.add(dayIndex);

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

    private void skipToNextWorkingDayAux(Calendar c, int dayIndex){

        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
            c.add(Calendar.DATE, 1); // Advance to monday (nextday = +1)
            dayIndex++;
        }else if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            c.add(Calendar.DATE, 2); // Advance to monday
            dayIndex+=2;
        }else
            return;

            pairCalendar = c;
            pairInteger = dayIndex;
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

    private void removeUnavailableRoomPeriods(int calendar, Connection conn, List<RoomPeriod> roomPeriods) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM roomPeriodUnavailable WHERE calendar = ?");
        ps.setInt(1, calendar);
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

    private void fillRoomPeriods(int calendar, Connection conn, List<RoomPeriod> roomPeriods, List<Exam> exams) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM exams WHERE calendar = ?");
        ps.setInt(1, calendar);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int examID = rs.getInt("id");
            Date date = rs.getDate("day");
            int time = rs.getInt("time");
            if (rs.wasNull() || date == null)
                continue; // Exam unassigned

            Exam exam = null;
            for (Exam e : exams) {
                if (e.getId() == examID) {
                    exam = e;
                    break;
                }
            }
            if (exam == null)
                return; // Exam not found

            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM examrooms WHERE exam = ?");
            ps2.setInt(1, rs.getInt("id"));
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                for (RoomPeriod rp : roomPeriods) {
                    if (!rp.getPeriod().getDate().equals(date))
                        continue;
                    if (rp.getPeriod().getTime().ordinal() != time)
                        continue;
                    int roomID = rs2.getInt("room");
                    if (rp.getRoom().getId() != roomID)
                        continue;

                    rp.setExam(exam);
                }
            }
        }
    }

    private List<ProfessorUnavailable> readProfessorUnavailables(int calendar, Connection conn, java.util.Date startingDay, List<Period> periods, List<Professor> professors) throws SQLException {
        ArrayList<ProfessorUnavailable> professorUnavailables = new ArrayList<ProfessorUnavailable>();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM professorunavailable WHERE calendar = ?");
        ps.setInt(1, calendar);
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

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }


}
