package parser;

/**
 * Created by Duarte on 01/07/2016.
 */
public class ExcelRegex {
    public static final String UC = "^([A-z]{1,5}\\d{4})\\s*-\\s*(.+)$";
    public static final String CLASS = "^Turma\\s*:\\s*(\\d)[A-z]{1,7}\\w*$";
    public static final String ROOM_SECTION = "[A-z]";
    public static final String ROOM_NUMBER  = "\\d{3}";
    public static final String ROOM = ROOM_SECTION+ROOM_NUMBER;
}
