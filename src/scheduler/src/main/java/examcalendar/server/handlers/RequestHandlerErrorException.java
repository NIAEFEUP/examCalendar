package examcalendar.server.handlers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gustavo on 12/07/2016.
 */
class RequestHandlerErrorException extends RequestHandlerException {
    private JSONObject data;

    RequestHandlerErrorException(int code, String message) {
        super(code, message);
    }

    RequestHandlerErrorException(int code, String message, JSONObject data) {
        super(code, message);
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "error");
            response.put("message", getMessage());
            if (data != null) // TODO is this needed?
                response.put("data", data);
        } catch (JSONException e) {
            throw new AssertionError();
        }
        return response.toString();
    }
}
