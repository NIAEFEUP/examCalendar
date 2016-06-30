package parser;

import examination.domain.Student;
import examination.domain.Topic;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duarte on 29/06/2016.
 */
public class UCMapParser {
    enum State{
        START,
        UC_ID,
        UC_YEAR,
        STUDENT,
        FINISHED
    }
    public static final int NUM_COLUMNS = 3;
    public static final String UC_REGEX = "^([A-z]{1,5}\\d{4})\\s*-\\s*(.+)$";
    public static final String CLASS_REGEX = "^Turma\\s*:\\s*(\\d)[A-z]{1,7}\\w*$";

    private State state = State.START;
    File file;
    Feedback feedback;
    Hashtable<Topic, Set<Student>> topics = new Hashtable<Topic, Set<Student>>();
    Hashtable<String,Student> students = new Hashtable<String, Student>();

    public UCMapParser(String file){
        this.file = new File(file);
        this.feedback = new Feedback(file);
    }



    // TODO: 29/06/2016 Não está acabado 
    boolean generate() throws IOException {
        Workbook wb;
        if(FilenameUtils.getExtension(file.getName()).equals("xlsx"))
            wb = new XSSFWorkbook(new FileInputStream(file));
        else if(FilenameUtils.getExtension(file.getName()).equals("xls")) {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            wb= new HSSFWorkbook(fs);
        }else{
            return false;
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        String cellContent;

        String currTopicName = null;
        String currTopicID = null;
        int currTopicYear;
        Topic currTopic = null;


        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if(row != null){
                cell = row.getCell(0);
                cellContent = cell.getStringCellValue();
                switch (state){
                    case START:
                        if(isBlankCell(cellContent) || !cellContent.matches("^[A-z]{1,5}\\d{4}\\s*-\\s*.+$"))
                            continue;
                        else{
                            currTopicID = extractTopicID(cellContent);

                            currTopicName = extractTopicName(cellContent);
                            this.state = State.UC_ID;
                        }
                        break;
                    case UC_ID:
                        if(isBlankCell(cellContent) || !cellContent.matches(CLASS_REGEX))
                            continue;
                        else {
                            Matcher matcherYear = Pattern.compile(CLASS_REGEX).matcher(cellContent);
                            matcherYear.matches();
                            currTopicYear = Integer.parseInt(matcherYear.group(1));

                            currTopic = new Topic(currTopicID,currTopicYear,currTopicName);
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
                            continue;
                        else {
                            Student student = parseStudent(row);
                            if(student == null)
                                continue;
                            try {
                                addStudentToTopic(currTopic,student);
                            } catch (ParserException e) {
                                handleParserException(row, cell,e);
                            }
                            this.state = State.STUDENT;
                        }
                        break;
                    case STUDENT:
                        if(isBlankCell(cellContent) || cellContent.trim().equalsIgnoreCase("Nome") || cellContent.matches(CLASS_REGEX))
                            continue;
                        else if(cellContent.matches(UC_REGEX)) {
                            currTopicID = extractTopicID(cellContent);
                            currTopicName = extractTopicName(cellContent);
                            this.state = State.UC_ID;
                        }else{
                            Student student = parseStudent(row);
                            if(student == null)
                                continue;
                            try {
                                addStudentToTopic(currTopic,student);
                            } catch (ParserException e) {
                                handleParserException(row, cell,e);
                            }
                        }
                        break;
                    case FINISHED:
                        return true;
                    default:
                        feedback.addError("Input inexperado",row.getRowNum()+"",cell.getColumnIndex()+"");
                        return false;
                }
            }
        }
        if(state.equals(State.FINISHED)|| state.equals(State.STUDENT)){
            this.state = State.FINISHED;
            feedback.setResult(true);
            return true;
        }else {
            return false;
        }
    }

    private String extractTopicName(String cellContent) {
        String currTopicName;Matcher matcherName = Pattern.compile(UC_REGEX).matcher(cellContent);
        matcherName.matches();
        currTopicName = matcherName.group(2);
        return currTopicName;
    }

    private String extractTopicID(String cellContent) {
        String currTopicID;Matcher matcherID = Pattern.compile(UC_REGEX).matcher(cellContent);
        matcherID.matches();
        currTopicID = matcherID.group(1);
        return currTopicID;
    }

    private void handleParserException(Row row, Cell cell, ParserException e) {
        if(e.getType().equals(ParserException.Type.WARNING)){
            feedback.addWarning(e.getMessage(), row.getRowNum() + "", cell.getColumnIndex() + "");
        }else{
            feedback.addError(e.getMessage(), row.getRowNum() + "", cell.getColumnIndex() + "");
        }
    }

    private Student parseStudent(Row row) {
        String name = row.getCell(0).getStringCellValue();
        long id = (long) row.getCell(1).getNumericCellValue();
        if(!row.getCell(2).getStringCellValue().equalsIgnoreCase("sim"))
            return null;
        return new Student(id, name);
    }

    void addStudentToTopic(Topic topic, Student givenStudent) throws ParserException {
        Student student = students.get(givenStudent.getId());
        Set<Student> studentsList = getTopics().get(topic);
        if(student == null){
            student = givenStudent;
            students.put(student.getId()+"",student);
        }

        if(studentsList.contains(student)){
            String message = "O aluno '"+student.getName()+"' encontra-se múltiplas vezes inscrito á UC "+topic.getId();
            throw new ParserException(message, ParserException.Type.WARNING);
        }
        studentsList.add(student);
    }

    void addTopic(Topic topic) throws ParserException {
        if(topics.get(topic) != null){
            String message = "A UC "+topic.getId()+" encontra-se referenciada mais que uma vez";
            throw new ParserException(message, ParserException.Type.WARNING);
        }

        topics.put(topic, new HashSet<Student>());
    }

    protected boolean isBlankCell(String cell){
        return cell.isEmpty() || cell.matches("^\\s+$");
    }

    public File getFile() {
        return file;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public Hashtable<Topic, Set<Student>> getTopics() {
        return topics;
    }

    public Hashtable<String, Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Set<Topic> keys = topics.keySet();
        for(Topic topic : keys){
            str.append(topic.getId() + " - "+topic.getName() + " "+ topic.getYear()+"\n");
            for(Student student: topics.get(topic)){
                str.append(student.getName()+" "+student.getId()+"\n");
            }
            str.append("\n\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String file = "../../mapa_exames_mieic.xls";
        UCMapParser parser = new UCMapParser(file);
        try {
            parser.generate();
            System.out.println(parser.toString());
            System.out.println(parser.getFeedback().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
