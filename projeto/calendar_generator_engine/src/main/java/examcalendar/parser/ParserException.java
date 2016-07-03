package examcalendar.parser;

/**
 * Created by Duarte on 29/06/2016.
 */
public class ParserException extends Exception {
    public enum Type{ERROR, WARNING}

    Type type;

    public ParserException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
