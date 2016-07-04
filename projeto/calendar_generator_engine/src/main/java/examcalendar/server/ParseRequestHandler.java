package examcalendar.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import examcalendar.optimizer.domain.Room;
import examcalendar.parser.RoomsParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class ParseRequestHandler implements HttpHandler {
    public static final String TEMP_DIR = "tmp/";
    public ParseRequestHandler() {
    }

    private void roomsFileHandler(Connection conn, HttpExchange httpExchange, int clientID) throws SQLException {
        File file = getUploadedFile(httpExchange);
        RoomsParser roomsParser = new RoomsParser(file.getPath());
        roomsParser.generate();
        if (roomsParser.getFeedback().isResult()) {
            Hashtable<String, Room> rooms = roomsParser.getRooms();
            Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
            while (it.hasNext()) {
                Room room = it.next().getValue();
                PreparedStatement ps =
                        conn.prepareStatement("INSERT INTO rooms (creator, cod, capacity, pc) VALUES (?, ?, ?, ?)");
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
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres", "123456"); // TODO (hardcoded)
            roomsFileHandler(conn, httpExchange, 1); // TODO
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
