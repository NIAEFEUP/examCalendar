package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    protected Map<String, String> parseQueryParams(URI uri) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = uri.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}