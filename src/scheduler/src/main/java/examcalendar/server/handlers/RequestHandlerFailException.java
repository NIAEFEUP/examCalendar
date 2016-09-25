package examcalendar.server.handlers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gustavo on 12/07/2016.
 */
public class RequestHandlerFailException extends RequestHandlerException {
    private JSONObject data;
    RequestHandlerFailException(int code, JSONObject data) {
        super(code);
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
            response.put("status", "fail");
            response.put("data", data);
        } catch (JSONException e) {
            throw new AssertionError();
        }
        return response.toString();
    }
}
