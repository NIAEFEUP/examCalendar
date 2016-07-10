package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Gustavo on 06/07/2016.
 */
public abstract class AbstractRequestHandler implements HttpHandler {

    @Override
    public abstract void handle(HttpExchange exchange) throws IOException;

    protected void sendSuccessResponse(HttpExchange exchange, Object data, int code) throws IOException {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "success");
            response.put("data", data);
        } catch (JSONException e) {
            throw new AssertionError();
        }
        exchange.getResponseHeaders().add("Content-Type:", "application/json");
        sendResponse(exchange, code, response.toString());
    }

    protected void sendFailResponse(HttpExchange exchange, Object data, int code) throws IOException {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "fail");
            response.put("data", data);
        } catch (JSONException e) {
            throw new AssertionError();
        }
        exchange.getResponseHeaders().add("Content-Type:", "application/json");
        sendResponse(exchange, code, response.toString());
    }

    protected void sendErrorResponse(HttpExchange exchange, String message, int code) throws IOException {
        sendErrorResponse(exchange, message, null, code);
    }

    protected void sendErrorResponse(HttpExchange exchange, String message, byte[] data, int code) throws IOException {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "error");
            response.put("message", message);
            if (data != null)
                response.put("data", data);
        } catch (JSONException e) {
            throw new AssertionError();
        }
        exchange.getResponseHeaders().add("Content-Type:", "application/json");
        sendResponse(exchange, code, response.toString());
    }

    protected void sendResponse(HttpExchange exchange, int code, byte[] data) throws IOException {
        exchange.sendResponseHeaders(code, data.length);
        OutputStream os = new BufferedOutputStream(exchange.getResponseBody());
        os.write(data);
        os.close();
        exchange.close();
    }

    private void sendResponse(HttpExchange exchange, int code, String message) throws IOException {
        sendResponse(exchange, code, message.getBytes());
    }
}