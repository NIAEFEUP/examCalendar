package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Gustavo on 12/07/2016.
 */
class RequestHandlerException extends RuntimeException {
    protected int code;
    RequestHandlerException(int code) {
        super();
        this.code = code;
    }

    RequestHandlerException(int code, String message) {
        super(message);
        this.code = code;
    }

    protected void send(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type:", "application/json");
        byte[] data = this.toString().getBytes();
        exchange.sendResponseHeaders(code, data.length);
        OutputStream os = new BufferedOutputStream(exchange.getResponseBody());
        os.write(data);
        os.close();
        exchange.close();
    }
}
