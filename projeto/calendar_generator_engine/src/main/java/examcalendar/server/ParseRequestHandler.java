package examcalendar.server;

import com.sun.net.httpserver.HttpExchange;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class ParseRequestHandler {
    public static final String TEMP_DIR = "tmp/";
    Connection conn;
    int clientID;
    private final HttpExchange t;
    public ParseRequestHandler(Connection connection, HttpExchange t, int clientID) {
        this.conn = connection;
        this.t = t;
        this.clientID = clientID;
    }

    private void roomsFileHandler() throws SQLException {
        File file = getUploadedFile();
        RoomsParser roomsParser = new RoomsParser(file.getPath());
        roomsParser.generate();
        if (roomsParser.getFeedback().isResult()) {
            Hashtable<String, Room> rooms = roomsParser.getRooms();
            Iterator<Map.Entry<String, Room>> it = rooms.entrySet().iterator();
            while (it.hasNext()) {
                Room room = it.next().getValue();
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO rooms (creator, cod, capacity, pc) VALUES (?, ?, ?, ?)");
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
    }

    private File getUploadedFile() {
        DiskFileItemFactory d = new DiskFileItemFactory();
        File file = null;
        try {
            ServletFileUpload up = new ServletFileUpload(d);
            List<FileItem> result = up.parseRequest(new RequestContext() {
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
                    return t.getRequestHeaders().getFirst("Content-type");
                }
                @Override
                public InputStream getInputStream() throws IOException {
                    return t.getRequestBody();
                }
            });
            if (result.size() != 1) return null;
            FileItem fileItem = result.get(0);
            file = new File(TEMP_DIR + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileItem.getName()));
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
}
