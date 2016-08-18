package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import examcalendar.optimizer.domain.Professor;
import examcalendar.optimizer.domain.Room;
import examcalendar.optimizer.domain.Student;
import examcalendar.optimizer.domain.Topic;
import examcalendar.parser.ProfessorParser;
import examcalendar.parser.RoomsParser;
import examcalendar.parser.UCMapParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.serializers.JsonSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class ParseRequestHandler extends AbstractRequestHandler {
    public static final String TEMP_DIR = "tmp/";
    public ParseRequestHandler() {
    }

    private UCMapParser ucMapFileHandler(Connection conn, HttpExchange httpExchange, File file) throws SQLException {
        UCMapParser ucMapParser = new UCMapParser(file.getPath());
        ucMapParser.generate();
        file.delete();
        return ucMapParser;
    }

    private ProfessorParser professorsFileHandler(Connection conn, HttpExchange httpExchange, File file, Set<Topic> topics) {
        ProfessorParser professorParser = new ProfessorParser(file.getPath(), topics);
        professorParser.generate();
        file.delete();
        return professorParser;
    }

    private RoomsParser roomsFileHandler(Connection conn, HttpExchange httpExchange, File file) throws SQLException {
        RoomsParser roomsParser = new RoomsParser(file.getPath());
        roomsParser.generate();
        file.delete();
        return roomsParser;
    }

    private FileItem getFileFromUploadedFiles(String fieldName, List<FileItem> uploadedFiles) {
        for (FileItem uploadedFile : uploadedFiles) {
            if (uploadedFile.getFieldName().equals(fieldName))
                return uploadedFile;
        }
        return null; // Not found
    }

    /**
     *
     * @param httpExchange
     * @return A List of 3 Files in the following order: ucmap, professors, rooms.
     */
    private List<File> getUploadedFiles(final HttpExchange httpExchange) {
        DiskFileItemFactory d = new DiskFileItemFactory();
        List<File> files = new ArrayList<File>();

        try {
            ServletFileUpload up = new ServletFileUpload(d);
            List result = up.parseRequest(new RequestContext() {
                @Override
                public String getCharacterEncoding() {
                    return "UTF-8";
                }
                @Override
                public int getContentLength() {
                    return 0; //tested to work with 0 as return
                }
                @Override
                public String getContentType() {
                    return httpExchange.getRequestHeaders().getFirst("Content-type");
                }
                @Override
                public InputStream getInputStream() throws IOException {
                    return httpExchange.getRequestBody();
                }
            });
            List<FileItem> fileItems = new ArrayList<FileItem>();

            FileItem ucMapFileItem = getFileFromUploadedFiles("ucmap", result);
            fileItems.add(ucMapFileItem);

            FileItem professorsFileItem = getFileFromUploadedFiles("professors", result);
            fileItems.add(professorsFileItem);

            FileItem roomsFileItem = getFileFromUploadedFiles("rooms", result);
            fileItems.add(roomsFileItem);

            for (FileItem fileItem : fileItems) {
                if (fileItem == null) {
                    files.add(null);
                    continue;
                }
                File file = new File(TEMP_DIR + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileItem.getName()));
                file.getParentFile().mkdirs();
                fileItem.write(file);
                files.add(file);
            }
            return files;
        } catch (Exception e) {
            e.printStackTrace();
            for (File file : files) {
                file.delete();
            }
            return null;
        }
    }

    public List<Room> parseRooms(File file) {
        return null; // TODO
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost/examcalendar?serverTimezone=UTC", "root", ""); // TODO (hardcoded)

                List<File> files = getUploadedFiles(httpExchange);
                if (files == null) throw new RequestHandlerFailException(400, null);

                int clientID = 1; // TODO
                Date date = new Date(new java.util.Date().getTime()); // TODO

                UCMapParser ucMapParser = null;
                ProfessorParser professorParser = null;
                RoomsParser roomsParser = null;

                JSONObject data = new JSONObject();
                if (files.get(0) == null) {
                    data.put("ucmap", "UC Map file is missing.");
                } else {
                    ucMapParser = ucMapFileHandler(conn, httpExchange, files.get(0));
                    data.put("ucmap", new JSONObject(ucMapParser.getFeedback()));
                }

                if (files.get(1) == null) {
                    data.put("professors", "Professors file is missing.");
                } else {
                    if (ucMapParser == null) {
                        data.put("professors", "Cannot load the professors file without the UC Map file.");
                    } else {
                        professorParser = professorsFileHandler(conn, httpExchange, files.get(1), ucMapParser.getTopics());
                        data.put("professors", new JSONObject(professorParser.getFeedback()));
                    }
                }

                if (files.get(2) == null) {
                    data.put("rooms", "Rooms file is missing.");
                } else {
                    roomsParser = roomsFileHandler(conn, httpExchange, files.get(2));
                    data.put("rooms", new JSONObject(roomsParser.getFeedback()));
                }

                if (ucMapParser != null && professorParser != null && roomsParser != null && ucMapParser.getFeedback().isGenerated() && professorParser.getFeedback().isGenerated() && roomsParser.getFeedback().isGenerated()) {
                    // Update database and return possible parser warnings
                    Hashtable<String, Student> students = ucMapParser.getStudents();
                    Set<Topic> topics = ucMapParser.getTopics();
                    Hashtable<String, Professor> professors = professorParser.getProfessors();
                    Hashtable<String, Room> rooms = roomsParser.getRooms();
                    insertDataInDB(conn, clientID, date, students, topics, professors, rooms);
                    this.sendSuccessResponse(httpExchange, data, 200);
                } else {
                    throw new RequestHandlerFailException(400, data);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RequestHandlerErrorException(503, "Could not connect to the database.");
            } catch (JSONException e) {
                throw new AssertionError();
            }
        } catch (RequestHandlerException e) {
            e.send(httpExchange);
        }
    }

    private boolean insertDataInDB(Connection conn, int clientID, Date startingDate, Hashtable<String, Student> students, Set<Topic> topics, Hashtable<String, Professor> professors, Hashtable<String, Room> rooms) {
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // TODO think if we really need this to be serializable
            int calendarID = resetCalendar(conn, clientID, startingDate);
            insertProfessorsInDB(conn, calendarID, professors);
            insertTopicsInDB(conn, calendarID, topics);
            insertExamsInDB(conn, calendarID, topics);
            insertStudentsInDB(conn, calendarID, students);
            insertStudentTopicAssociationsInDB(conn, calendarID, topics);
            insertRoomsInDB(conn, calendarID, rooms);
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private int resetCalendar(Connection conn, int clientID, Date startingDate) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM calendars WHERE creator = ?");
        ps.setInt(1, clientID);
        ps.execute();
        ps = conn.prepareStatement("INSERT INTO calendars (creator, startingDate) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, clientID);
        ps.setDate(2, startingDate);
        ps.execute();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    private void insertProfessorsInDB(Connection conn, int calendar, Hashtable<String, Professor> professors) throws SQLException {
        Iterator<Map.Entry<String, Professor>> it = professors.entrySet().iterator();
        while (it.hasNext()) {
            Professor professor = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO professors (calendar, name, acronym, cod) VALUES (?, ?, ?, ?)");
            ps.setInt(1, calendar);
            ps.setString(2, professor.getName());
            ps.setString(3, professor.getAcronym());
            ps.setString(4, professor.getCode());
            ps.execute();
        }
    }

    private void insertTopicsInDB(Connection conn, int calendar, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO topics (calendar, name, acronym, code, year) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, calendar);
            ps.setString(2, topic.getName());
            ps.setString(3, topic.getAcronym());
            ps.setString(4, topic.getCode());
            ps.setInt(5, topic.getYear());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            topic.setId(id);
        }
    }

    private void insertExamsInDB(Connection conn, int calendar, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO exams (calendar, topic, normal, pc) VALUES (?, ?, TRUE, FALSE)");
            ps.setInt(1, calendar);
            ps.setInt(2, topic.getId());
            ps.execute();

            ps = conn.prepareStatement("INSERT INTO exams (calendar, topic, normal, pc) VALUES (?, ?, FALSE, FALSE)");
            ps.setInt(1, calendar);
            ps.setInt(2, topic.getId());
            ps.execute();
        }
    }

    private void insertStudentsInDB(Connection conn, int calendar, Hashtable<String, Student> students) throws SQLException {
        Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator();
        while (it.hasNext()) {
            Student student = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO students (calendar, name, cod) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, calendar);
            ps.setString(2, student.getName());
            ps.setString(3, student.getCode());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            student.setId(id);
        }
    }

    private void insertStudentTopicAssociationsInDB(Connection conn, int calendar, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            Set<Student> topicStudents = topic.getStudentList();
            for (Student student : topicStudents) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO studentTopic (student, topic) VALUES (?, ?)");
                ps.setInt(1, student.getId());
                ps.setInt(2, topic.getId());
                ps.execute();
            }
        }
    }

    private void insertRoomsInDB(Connection conn, int calendar, Hashtable<String, Room> rooms) throws SQLException {
        Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
        while (it.hasNext()) {
            Room room = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO rooms (calendar, cod, capacity, pc) VALUES (?, ?, ?, ?)");
            ps.setInt(1, calendar);
            ps.setString(2, room.getCodRoom());
            ps.setInt(3, room.getCapacity());
            ps.setBoolean(4, room.isPc());
            ps.execute();
        }
    }
}
