package parser;

import examination.domain.Examination;
import examination.domain.Student;
import examination.domain.Topic;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

/**
 * Created by Duarte on 29/06/2016.
 */
public class UCMapParser {
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
        if(FilenameUtils.getExtension(file.getName()).equals(".xlsx"))
            wb = new XSSFWorkbook(new FileInputStream(file));
        else{
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            wb= new HSSFWorkbook(fs);
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        return false;
    }

    void addStudentToTopic(Topic topic, String studentID, String studentName) throws Exception {
        Student student = students.get(studentID);
        Set<Student> studentsList = getTopics().get(topic);
        if(student == null){
            student = new Student(Long.getLong(studentID), studentName);
            students.put(studentID,student);
        }

        if(studentsList.contains(student)){
            String message = "O aluno '"+studentName+"' encontra-se múltiplas vezes inscrito á UC "+topic.getId();
            throw new ParserException(message, ParserException.Type.WARNING);
        }
        studentsList.add(student);
    }

    void addTopic(Topic topic) throws Exception {
        if(topics.get(topic) != null){
            String message = "A UC "+topic.getId()+" já foi processada";
            throw new ParserException(message, ParserException.Type.WARNING);
        }

        topics.put(topic, new HashSet<Student>());
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

    public static void main(String[] args) {
            
    }
}
