package examcalendar.server;

import com.sun.net.httpserver.HttpExchange;
import examcalendar.optimizer.domain.Room;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

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
