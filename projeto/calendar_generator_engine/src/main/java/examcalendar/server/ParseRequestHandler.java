package examcalendar.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class ParseRequestHandler implements HttpHandler {
    public static final String TEMP_DIR = "tmp/";
    public ParseRequestHandler() {
    }

    private UCMapParser ucMapFileHandler(Connection conn, HttpExchange httpExchange, int clientID, File file) throws SQLException {
        UCMapParser ucMapParser = new UCMapParser(file.getPath());
        ucMapParser.generate();
        file.delete();
        return ucMapParser;
    }

    private ProfessorParser professorsFileHandler(Connection conn, HttpExchange httpExchange, int clientID, File file, Set<Topic> topics) {
        ProfessorParser professorParser = new ProfessorParser(file.getPath(), topics);
        professorParser.generate();
        file.delete();
        return professorParser;
    }

    private RoomsParser roomsFileHandler(Connection conn, HttpExchange httpExchange, int clientID, File file) throws SQLException {
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
            if (result.size() < 3) return null;
            List<FileItem> validFileItems = new ArrayList<FileItem>();

            FileItem ucMapFileItem = getFileFromUploadedFiles("ucmap", result);
            if (ucMapFileItem == null) return null;
            validFileItems.add(ucMapFileItem);

            FileItem professorsFileItem = getFileFromUploadedFiles("professors", result);
            if (professorsFileItem == null) return null;
            validFileItems.add(professorsFileItem);

            FileItem roomsFileItem = getFileFromUploadedFiles("rooms", result);
            if (roomsFileItem == null) return null;
            validFileItems.add(roomsFileItem);

            for (FileItem fileItem : validFileItems) {
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
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=UTC", "root", ""); // TODO (hardcoded)

            List<File> files = getUploadedFiles(httpExchange);
            if (files == null) return;

            int clientID = 1; // TODO
            UCMapParser ucMapParser = ucMapFileHandler(conn, httpExchange, clientID, files.get(0));
            ProfessorParser professorParser = professorsFileHandler(conn, httpExchange, clientID, files.get(1), ucMapParser.getTopics());
            RoomsParser roomsParser = roomsFileHandler(conn, httpExchange, clientID, files.get(2));

            if (ucMapParser.getFeedback().isGenerated() && professorParser.getFeedback().isGenerated() && roomsParser.getFeedback().isGenerated()) {
                // Update database and return possible parser warnings
                Hashtable<String, Student> students = ucMapParser.getStudents();
                Set<Topic> topics = ucMapParser.getTopics();
                Hashtable<String, Professor> professors = professorParser.getProfessors();
                Hashtable<String, Room> rooms = roomsParser.getRooms();
                insertDataInDB(conn, clientID, students, topics, professors, rooms);
            } else {
                // Return errors and possible warnings
            }
            System.out.println("UC Map parser feedback: " + ucMapParser.getFeedback());
            System.out.println("Professors parser feedback: " + professorParser.getFeedback());
            System.out.println("Rooms parser feedback: " + roomsParser.getFeedback());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean insertDataInDB(Connection conn, int clientID, Hashtable<String, Student> students, Set<Topic> topics, Hashtable<String, Professor> professors, Hashtable<String, Room> rooms) {
        try {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // TODO think if we really need this to be serializable
            emptyDB(conn, clientID);
            insertProfessorsInDB(conn, clientID, professors);
            insertTopicsInDB(conn, clientID, topics);
            insertExamsInDB(conn, clientID, topics);
            insertStudentsInDB(conn, clientID, students);
            insertStudentTopicAssociationsInDB(conn, clientID, topics);
            insertRoomsInDB(conn, clientID, rooms);
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

    private void emptyDB(Connection conn, int clientID) throws SQLException {
        String[] tables = { "topics", "students", "rooms" };
        for (String table : tables) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + " WHERE creator = ?");
            ps.setInt(1, clientID);
            ps.execute();
        }
    }

    private void insertProfessorsInDB(Connection conn, int clientID, Hashtable<String, Professor> professors) throws SQLException {
        Iterator<Map.Entry<String, Professor>> it = professors.entrySet().iterator();
        while (it.hasNext()) {
            Professor professor = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO professors (creator, name, acronym, cod) VALUES (?, ?, ?, ?)");
            ps.setInt(1, clientID);
            ps.setString(2, professor.getAcronym()); // TODO
            ps.setString(3, professor.getAcronym());
            ps.setString(4, professor.getAcronym()); // TODO
            ps.execute();
        }
    }

    private void insertTopicsInDB(Connection conn, int clientID, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO topics (creator, name, acronym, code, year) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clientID);
            ps.setString(2, topic.getName());
            ps.setString(3, topic.getCode()); // TODO
            ps.setString(4, topic.getCode());
            ps.setInt(5, topic.getYear());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            topic.setId(id);
        }
    }

    private void insertExamsInDB(Connection conn, int clientID, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO exams (creator, topic, normal, pc) VALUES (?, ?, TRUE, FALSE)");
            ps.setInt(1, clientID);
            ps.setInt(2, topic.getId());
            ps.execute();

            ps = conn.prepareStatement("INSERT INTO exams (creator, topic, normal, pc) VALUES (?, ?, FALSE, FALSE)");
            ps.setInt(1, clientID);
            ps.setInt(2, topic.getId());
            ps.execute();
        }
    }

    private void insertStudentsInDB(Connection conn, int clientID, Hashtable<String, Student> students) throws SQLException {
        Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator();
        while (it.hasNext()) {
            Student student = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO students (creator, name, cod) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clientID);
            ps.setString(2, student.getName());
            ps.setString(3, student.getCode());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            student.setId(id);
        }
    }

    private void insertStudentTopicAssociationsInDB(Connection conn, int clientID, Set<Topic> topics) throws SQLException {
        for (Topic topic : topics) {
            Set<Student> topicStudents = topic.getStudentList();
            for (Student student : topicStudents) {
                System.out.println(student.getId() + " " + topic.getId());
                PreparedStatement ps = conn.prepareStatement("INSERT INTO studentTopic (student, topic) VALUES (?, ?)");
                ps.setInt(1, student.getId());
                ps.setInt(2, topic.getId());
                ps.execute();
            }
        }
    }

    private void insertRoomsInDB(Connection conn, int clientID, Hashtable<String, Room> rooms) throws SQLException {
        Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
        while (it.hasNext()) {
            Room room = it.next().getValue();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO rooms (creator, cod, capacity, pc) VALUES (?, ?, ?, ?)");
            ps.setInt(1, clientID);
            ps.setString(2, room.getCodRoom());
            ps.setInt(3, room.getCapacity());
            ps.setBoolean(4, room.isPc());
            ps.execute();
        }
    }
}
