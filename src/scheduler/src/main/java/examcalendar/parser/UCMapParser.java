package examcalendar.parser;

import examcalendar.optimizer.domain.Student;
import examcalendar.optimizer.domain.Topic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static examcalendar.parser.ExcelRegex.CLASS;
import static examcalendar.parser.ExcelRegex.UC;
import static examcalendar.parser.ExcelRegex.UC_ID;

/**
 * Created by Duarte on 29/06/2016.
 */
public class UCMapParser extends ExcelParser {
    enum State{
        START,
        UC_ID,
        UC_YEAR,
        STUDENT
    }
    public static final int NUM_COLUMNS = 3;

    private State state = State.START;
    HashSet<Topic> topics = new HashSet<Topic>();
    Hashtable<String,Student> students = new Hashtable<String, Student>();
    private Topic currTopic = null;
    private String currTopicName = null;
    private String currTopicCode = null;
    private int currTopicYear;

    public UCMapParser(String file){
        super(file);
    }

    @Override
    protected boolean hasGenerated() {
        return state.equals(UCMapParser.State.STUDENT);
    }

    @Override
    protected void parseRow(Row row) {
        Cell cell = null;
        String cellContent;
        //String currTopicName = null;
        //String currTopicCode = null;
        //int currTopicYear;
        //Topic currTopic = null;
        cell = row.getCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);

        cellContent = cell.getStringCellValue().trim();
        switch (state){
            case START:
                if(isBlankCell(cellContent) || !cellContent.matches("^[A-z]{1,5}\\d{4}\\s*-\\s*.+$"))
                    return;
                else{
                    currTopicCode = extractTopicCode(cellContent);
                    currTopicName = extractTopicName(cellContent);
                    this.state = State.UC_ID;
                }
                break;
            case UC_ID:
                if(isBlankCell(cellContent) || !cellContent.matches(CLASS))
                    return;
                else {
                    Matcher matcherYear = Pattern.compile(CLASS).matcher(cellContent);
                    matcherYear.matches();
                    currTopicYear = Integer.parseInt(matcherYear.group(1));
                    currTopic = new Topic(currTopicYear,currTopicCode,currTopicName);
                    try {
                        addTopic(currTopic);
                    } catch (ParserException e) {
                        handleParserException(row, cell, e);
                    }
                    this.state = State.UC_YEAR;
                }
                break;
            case UC_YEAR:
                if(isBlankCell(cellContent) || cellContent.trim().equalsIgnoreCase("Nome"))
                    return;
                else {
                    Student student = parseStudent(row);
                    if(student == null)
                        return;
                    try {
                        addStudentToTopic(currTopic,student);
                    } catch (ParserException e) {
                        handleParserException(row, cell,e);
                    }
                    this.state = State.STUDENT;
                }
                break;
            case STUDENT:
                if(isBlankCell(cellContent) || cellContent.trim().equalsIgnoreCase("Nome") || cellContent.matches(CLASS)){
                    return;
                }else if(cellContent.matches(UC)) {
                    currTopicCode = extractTopicCode(cellContent);
                    currTopicName = extractTopicName(cellContent);
                    this.state = State.UC_ID;
                }else{
                    Student student = parseStudent(row);
                    if(student == null)
                        return;
                    try {
                        addStudentToTopic(currTopic,student);
                    } catch (ParserException e) {
                        handleParserException(row, cell,e);
                    }
                }
                break;
            default:
                feedback.addError("Input inesperado",row.getRowNum()+"",cell.getColumnIndex()+"");
                return;
        }
    }


    private String extractTopicName(String cellContent) {
        String currTopicName;Matcher matcherName = Pattern.compile(UC).matcher(cellContent);
        matcherName.matches();
        currTopicName = matcherName.group(2);
        return currTopicName;
    }

    public static String extractTopicCode(String cellContent) {
        String currTopicID;Matcher matcherID = Pattern.compile(UC_ID+".*").matcher(cellContent);
        matcherID.matches();
        currTopicID = matcherID.group(1);
        return currTopicID;
    }

    private void handleParserException(Row row, Cell cell, ParserException e) {
        if(e.getType().equals(ParserException.Type.WARNING)){
            getFeedback().addWarning(e.getMessage(), row.getRowNum() + "", cell.getColumnIndex() + "");
        }else{
            getFeedback().addError(e.getMessage(), row.getRowNum() + "", cell.getColumnIndex() + "");
        }
    }

    private Student parseStudent(Row row) {
        String name = row.getCell(0).getStringCellValue();
        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
        String code = row.getCell(1).getStringCellValue();
        if(!row.getCell(2).getStringCellValue().equalsIgnoreCase("sim"))
            return null;
        Student student = new Student();
        student.setName(name);
        student.setCode(code);
        return student;
    }

    private void addStudentToTopic(Topic topic, Student givenStudent) throws ParserException {
        if (givenStudent == null){
            System.out.println("null ptr");
            return;
    }
        Student student = students.get(givenStudent.getCode());
        Set<Student> studentsList = topic.getStudentList();
        if(student == null){
            student = givenStudent;
            students.put(student.getCode()+"",student);
        }

        if(studentsList.contains(student)){
            String message = "O aluno '"+student.getName()+"' encontra-se múltiplas vezes inscrito á UC "+topic.getId();
            throw new ParserException(message, ParserException.Type.WARNING);
        }
        studentsList.add(student);
    }

    private void addTopic(Topic topic) throws ParserException {
        if(topics.contains(topic)){
            String message = "A UC "+topic.getId()+" encontra-se referenciada mais que uma vez";
            throw new ParserException(message, ParserException.Type.WARNING);
        }

        topics.add(topic);
    }

    public HashSet<Topic> getTopics() {
        return topics;
    }

    public Hashtable<String, Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Set<Topic> keys = topics;
        for(Topic topic : keys){
            str.append(topic.getCode() + " - "+topic.getName() + " "+ topic.getYear()+"\n");
            for(Student student: topic.getStudentList()){
                str.append(student.getName()+" "+student.getCode()+"\n");
            }
            str.append("\n\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String file = "src/scheduler/src/test/java/mapa_exames_mieic.xls";
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        UCMapParser parser = new UCMapParser(file);
        parser.generate();
        System.out.println(parser.toString());
        System.out.println(parser.getFeedback().toString());

    }
}
