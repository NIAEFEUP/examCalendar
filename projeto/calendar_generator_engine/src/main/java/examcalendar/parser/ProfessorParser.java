package examcalendar.parser;

import examcalendar.optimizer.domain.Professor;
import examcalendar.optimizer.domain.Topic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Hashtable;
import java.util.Set;

import static examcalendar.parser.ExcelRegex.UC_ID;

/**
 * Created by Duarte on 01/07/2016.
 */
public class ProfessorParser extends ExcelParser{

    Set<Topic> topics;
    Hashtable<String, Professor> professors = new Hashtable<String, Professor>();

    public ProfessorParser(String file, Set<Topic> topics) {
        super(file);
        this.topics = topics;
    }

    @Override
    protected boolean parseSheet(Sheet sheet) {
        Row row = null;
        Cell cell = null;
        String cellContent;

        String currTopicCode;
        String currProfessorAcronym;
        Professor currProf = null;
        Topic currTopic = null;

        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                cell = row.getCell(0);
                cellContent = cell.getStringCellValue().trim();
                if(isBlankCell(cellContent) || !cellContent.matches(UC_ID))
                    continue;

                currTopicCode = UCMapParser.extractTopicCode(cellContent);
                currTopic = getTopic(currTopicCode);
                if(currTopic == null){
                    feedback.addError("UC não existe", (row.getRowNum()+1)+"", cell.getColumnIndex()+"");
                    return false;
                }
                currProfessorAcronym = row.getCell(1).getStringCellValue();
                if(professors.containsKey(currProfessorAcronym)){
                    currProf = professors.get(currProfessorAcronym);
                }else {
                    currProf = new Professor(currProfessorAcronym);
                    professors.put(currProfessorAcronym,currProf);
                }

                if(currTopic.getRegent() != null){
                    feedback.addWarning("Um professor já está adicionado a esta UC", row.getRowNum()+"", cell.getColumnIndex()+"");
                }else{
                    currTopic.setRegent(currProf);
                }
            }
        }
        if(professors.size() > 0) {
            feedback.setGenerated(true);
        }else{
            feedback.addError("Erro a ler a lista de professores", row.getRowNum()+"",0+"");
            return false;
        }
        return true;
    }

    private Topic getTopic(String code) {
        Topic currTopic = null;
        for(Topic topic: topics){
            if(topic.getCode().equals(code)){
                currTopic=topic;
            }
        }
        return currTopic;
    }

    public Hashtable<String, Professor> getProfessors() {
        return professors;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

            for (Topic topic : topics) {
                str.append(topic.getCode() + " " + topic.getName() + "\n");
                if(topic.getRegent() != null)
                    str.append(topic.getRegent().getAcronym() + "\n");
                str.append("\n");
            }


        return  str.toString();
    }

    public static void main(String[] args) {
        String ucFile = "src/test/java/mapa_exames_mieic.xls";
        UCMapParser parser = new UCMapParser(ucFile);
        parser.generate();
        System.out.println(parser.toString());

        String profFile = "src/test/java/professors.xlsx";
        ProfessorParser professorParser = new ProfessorParser(profFile,parser.getTopics());
        professorParser.generate();
        System.out.println(professorParser.toString());
        System.out.println(professorParser.feedback.toString());


    }
}
