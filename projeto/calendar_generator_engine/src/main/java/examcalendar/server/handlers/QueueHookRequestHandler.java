package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import examcalendar.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Gustavo on 06/07/2016.
 */
public class QueueHookRequestHandler extends AbstractRequestHandler {
    private Server server;

    public QueueHookRequestHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("POST")) {
                server.notifyEvent(Server.Event.ENQUEUEING);
                this.sendSuccessResponse(exchange, JSONObject.NULL, 200);
            } else {
                // Method not allowed
                JSONObject data = new JSONObject();
                data.put("method", "Method \"" + method + "\" not allowed.");
                exchange.getResponseHeaders().add("Allow", "POST");
                throw new RequestHandlerFailException(405, data.toString().getBytes());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
