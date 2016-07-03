package parser;

import examination.domain.Professor;
import examination.domain.Topic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Hashtable;
import java.util.Set;

import static parser.ExcelRegex.UC_ID;

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

        String currTopicID;
        String currProfessorID;
        Professor currProf = null;
        Topic currTopic = null;

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                cell = row.getCell(0);
                cellContent = cell.getStringCellValue().trim();
                if(isBlankCell(cellContent) || !cellContent.matches(UC_ID))
                    continue;

                currTopicID = UCMapParser.extractTopicID(cellContent);
                currTopic = getTopic(currTopicID);
                if(currTopic == null){
                    feedback.addError("UC não existe", row.getRowNum()+"", cell.getColumnIndex()+"");
                    return false;
                }

                currProfessorID = row.getCell(1).getStringCellValue();
                if(professors.containsKey(currProfessorID)){
                    currProf = professors.get(currProfessorID);
                }else {
                    currProf = new Professor(currProfessorID);
                    professors.put(currProfessorID,currProf);
                }

                if(currTopic.getProfessors().contains(currProf)){
                    feedback.addWarning("Professor já está adicionado a esta UC", row.getRowNum()+"", cell.getColumnIndex()+"");
                }else{
                    currTopic.addProfessor(currProf);
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

    private Topic getTopic(String currTopicID) {
        Topic currTopic = null;
        for(Topic topic: topics){
            if(topic.getId().equals(currTopicID)){
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

        for(Topic topic : topics){
            str.append(topic.getId() + " " +topic.getName()+"\n");
            for(Professor professor:topic.getProfessors()){
                str.append(professor.getId()+"\n");
            }
            str.append("\n");
        }

        return  str.toString();
    }

    public static void main(String[] args) {
        String ucFile = "src/test/java/mapa_exames_mieic.xls";
        UCMapParser parser = new UCMapParser(ucFile);
        parser.generate();

        String profFile = "src/test/java/professors.xlsx";
        ProfessorParser professorParser = new ProfessorParser(profFile,parser.getTopics());
        professorParser.generate();
        System.out.println(professorParser.toString());
        System.out.println(professorParser.feedback.toString());


    }
}
