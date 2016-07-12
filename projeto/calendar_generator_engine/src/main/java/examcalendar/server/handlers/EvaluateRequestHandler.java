package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Gustavo on 10/07/2016.
 */
public class EvaluateRequestHandler extends AbstractRequestHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            try {
                String method = exchange.getRequestMethod();
                if (method.equals("GET")) {
                    Map<String, String> params = parseQueryParams(exchange.getRequestURI());
                    String creator = params.get("creator");
                    if (creator == null) {
                        JSONObject data = new JSONObject();
                        data.put("creator", "Missing creator ID.");
                        throw new RequestHandlerFailException(400, data.toString().getBytes());
                    }
                    try {
                        int creatorID = Integer.parseInt("creator");
                    } catch (NumberFormatException e) {
                        JSONObject data = new JSONObject();
                        data.put("creator", "Creator ID is invalid.");
                        throw new RequestHandlerFailException(400, data.toString().getBytes());
                    }
                    // TODO
                } else {
                    // Method not allowed
                    JSONObject data = new JSONObject();
                    data.put("method", "Method \"" + method + "\" not allowed.");
                    exchange.getResponseHeaders().add("Allow", "GET");
                    throw new RequestHandlerFailException(405, data.toString().getBytes());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RequestHandlerErrorException(500, "Unknown error.");
            }
        } catch (RequestHandlerException e) {
            e.send(exchange);
        }
    }
}
