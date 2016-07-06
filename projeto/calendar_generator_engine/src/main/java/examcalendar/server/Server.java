package examcalendar.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class Server {
    public static final int MAX_PARALLEL_SCHEDULING = 3;
    private volatile int numRunningJobs = 0;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public Server() {

    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/parse", new ParseRequestHandler());
        server.createContext("/queuehook", new QueueHookRequestHandler());
        server.setExecutor(null);

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_PARALLEL_SCHEDULING);

        server.start();
    }
}
