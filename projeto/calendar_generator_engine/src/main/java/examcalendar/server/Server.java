package examcalendar.server;

import com.sun.net.httpserver.HttpServer;
import examcalendar.server.handlers.ParseRequestHandler;
import examcalendar.server.handlers.QueueHookRequestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by Gustavo on 03/07/2016.
 */
public class Server {
    public enum Event {
        SCHEDULER_END,
        ENQUEUEING,
        TERMINATE
    }
    public static final int MAX_PARALLEL_SCHEDULING = 3;
    BlockingQueue<Event> events;
    Connection conn;
    volatile boolean running;
    List<Scheduler> runningSchedulers;
    private Thread dispatcherThread;
    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server();
        server.start();
    }

    public void start() throws IOException, SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=UTC", "root", ""); // TODO (hardcoded)
        events = new LinkedBlockingQueue<Event>();
        runningSchedulers = new ArrayList<Scheduler>();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/parse", new ParseRequestHandler());
        server.createContext("/queuehook", new QueueHookRequestHandler(this));
        server.setExecutor(null);
        dispatcherThread = new Thread(new EventDispatcher(this));
        running = true;
        dispatcherThread.start();
        server.start();
    }

    boolean canStartNewScheduler() {
        return runningSchedulers.size() >= MAX_PARALLEL_SCHEDULING && running;
    }

    /**
     *
     * @return true if a request has been attended, false otherwise
     * @throws SQLException
     */
    boolean attendNextRequest() throws SQLException {
        // Fetch requests from the database (in order)
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM requests" +
                " WHERE enqueueingTime IS NOT NULL AND startTime IS NULL AND endTime IS NULL ORDER BY enqueueingTime DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int requestID = rs.getInt("id");
            runningSchedulers.add(new Scheduler(this, requestID));
            return true;
        }
        return false;
    }

    public void stop() {
        running = false;
        events.add(Event.TERMINATE);
    }

    public void notifyEvent(Event event) {
        events.add(event);
    }

    public boolean isRunning() {
        return this.running;
    }
}
