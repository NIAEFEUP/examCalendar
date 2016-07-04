package examcalendar.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import examcalendar.optimizer.domain.Room;
import examcalendar.optimizer.domain.Student;
import examcalendar.optimizer.domain.Topic;
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

    private void ucMapFileHandler(Connection conn, HttpExchange httpExchange, int clientID) throws SQLException {
        File file = getUploadedFile(httpExchange);
        UCMapParser ucMapParser = new UCMapParser(file.getPath());
        ucMapParser.generate();
        if (ucMapParser.getFeedback().isGenerated()) {
            PreparedStatement ps;

            ps = conn.prepareStatement("DELETE FROM students WHERE creator = ?");
            ps.setInt(1, clientID);
            ps.execute();

            ps = conn.prepareStatement("DELETE FROM topics WHERE creator = ?");
            ps.setInt(1, clientID);
            ps.execute();

            Hashtable<String, Student> students = ucMapParser.getStudents();
            Iterator<Map.Entry<String, Student>> it = students.entrySet().iterator();
            while (it.hasNext()) {
                Student student = it.next().getValue();
                ps = conn.prepareStatement("INSERT INTO students (creator, name, cod) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, clientID);
                ps.setString(2, student.getName());
                ps.setString(3, student.getCode());
                ps.execute();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                student.setId(id);
            }

            Set<Topic> topics = ucMapParser.getTopics();
            for (Topic topic : topics) {
                ps = conn.prepareStatement("INSERT INTO topics (creator, name, acronym, code, year) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

                Set<Student> topicStudents = topic.getStudentList();
                for (Student student : topicStudents) {
                    System.out.println(student.getId() + " " + topic.getId());
                    ps = conn.prepareStatement("INSERT INTO studentTopic (student, topic) VALUES (?, ?)");
                    ps.setInt(1, student.getId());
                    ps.setInt(2, topic.getId());
                    ps.execute();
                }
            }
        }
        else {
            System.err.println(ucMapParser.getFeedback());
        }
        file.delete();
    }

    private void roomsFileHandler(Connection conn, HttpExchange httpExchange, int clientID) throws SQLException {
        File file = getUploadedFile(httpExchange);
        RoomsParser roomsParser = new RoomsParser(file.getPath());
        roomsParser.generate();
        if (roomsParser.getFeedback().isGenerated()) {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM rooms WHERE creator = ?");
            ps.setInt(1, clientID);
            ps.execute();
            Hashtable<String, Room> rooms = roomsParser.getRooms();
            Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
            while (it.hasNext()) {
                Room room = it.next().getValue();
                ps = conn.prepareStatement("INSERT INTO rooms (creator, cod, capacity, pc) VALUES (?, ?, ?, ?)");
                ps.setInt(1, clientID);
                ps.setString(2, room.getCodRoom());
                ps.setInt(3, room.getCapacity());
                ps.setBoolean(4, room.isPc());
                ps.execute();
            }
        }
        else {
            // TODO
        }
        file.delete();
    }

    private File getUploadedFile(final HttpExchange httpExchange) {
        DiskFileItemFactory d = new DiskFileItemFactory();
        File file = null;
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
            if (result.size() < 1) return null;
            FileItem fileItem = (FileItem) result.get(0);
            file = new File(TEMP_DIR + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileItem.getName()));
            file.getParentFile().mkdirs();
            fileItem.write(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            if (file != null)
                file.delete();
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
            //roomsFileHandler(conn, httpExchange, 1); // TODO
            ucMapFileHandler(conn, httpExchange, 1); // TODO
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
