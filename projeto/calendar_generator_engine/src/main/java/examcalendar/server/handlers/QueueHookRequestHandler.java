package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import examcalendar.server.Server;

import java.io.IOException;

/**
 * Created by Gustavo on 06/07/2016.
 */
public class QueueHookRequestHandler extends RequestHandler {
    private Server server;

    public QueueHookRequestHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        server.notifyEvent(Server.Event.ENQUEUEING);
    }
}
