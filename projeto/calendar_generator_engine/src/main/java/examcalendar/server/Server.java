package examcalendar.server;

import com.sun.net.httpserver.HttpServer;
import examcalendar.server.handlers.EvaluateRequestHandler;
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
    public static final int PORT = 8080; // TODO change to 80

    String hostname;
    String database;
    String username;
    String password;

    BlockingQueue<Event> events;
    Connection conn;
    volatile boolean running;
    List<Scheduler> runningSchedulers;
    private Thread dispatcherThread;
    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server("localhost", "test", "root", "");
        server.start();
    }

    public Server(String hostname, String database, String username, String password) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void start() throws IOException, SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database + "?serverTimezone=UTC", username, password);
        events = new LinkedBlockingQueue<Event>();
        runningSchedulers = new ArrayList<Scheduler>();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/parse", new ParseRequestHandler());
        server.createContext("/queuehook", new QueueHookRequestHandler(this));
        server.createContext("/evaluate", new EvaluateRequestHandler());
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
        System.out.println("Searching for a new request...");
        // Fetch requests from the database (in order)
        PreparedStatement ps = conn.prepareStatement("SELECT id FROM calendars" +
                " WHERE enqueueingTime IS NOT NULL AND startTime IS NULL AND endTime IS NULL ORDER BY enqueueingTime DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int calendarID = rs.getInt("id");
            Scheduler scheduler = new Scheduler(this, calendarID);
            runningSchedulers.add(scheduler);
            System.out.println("Building calendar #" + calendarID + ".");
            scheduler.start();
            return true;
        }
        System.out.println("No calendars found in the waiting queue.");
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
