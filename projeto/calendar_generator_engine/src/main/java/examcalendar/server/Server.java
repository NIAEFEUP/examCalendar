package examcalendar.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class Server {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public Server() {

    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/parse", new ParseRequestHandler());
        server.setExecutor(null);
        server.start();
    }

    public class RequestHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //new ParseRequestHandler(conn, t, 1);
        }
    }
}
