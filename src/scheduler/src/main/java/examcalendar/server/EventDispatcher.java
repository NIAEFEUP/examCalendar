package examcalendar.server;

import java.sql.SQLException;

/**
 * Created by Gustavo on 09/07/2016.
 */
public class EventDispatcher implements Runnable {
    private Server server;
    public EventDispatcher(Server server) {
        this.server = server;
    }
    @Override
    public void run() {
        try {
            while (server.canStartNewScheduler()) {
                server.attendNextRequest();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (server.isRunning()) {
            try {
                // Wait for a new event
                do {
                    Object event = server.events.take(); // Block waiting for a new event
                    if (event instanceof Scheduler) {
                        server.runningSchedulers.remove(event);
                    }
                } while (server.canStartNewScheduler()); // Keep waiting for an event if too many schedulers are already running

                server.attendNextRequest();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
            }
        }
    }
}
