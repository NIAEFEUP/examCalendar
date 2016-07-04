import examcalendar.optimizer.ExaminationGenerator;
import examcalendar.optimizer.domain.*;
import examcalendar.parser.ProfessorParser;
import examcalendar.parser.RoomsParser;
import examcalendar.parser.UCMapParser;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Created by Duarte on 03/07/2016.
*/
public class GenerateCalendar {

    public static void main(String[] args) {
        if(args.length < 3)
            return;
        String ucFile = args[0];
        String roomFile = args[1];
        String professorsFile = args[2];

        UCMapParser ucMapParser = new UCMapParser(ucFile);
        ucMapParser.generate();
        if(!ucMapParser.getFeedback().isGenerated()){
            System.out.println(ucMapParser.getFeedback().toString());
            return;
        }

        RoomsParser roomsParser = new RoomsParser(roomFile);
        roomsParser.generate();
        if(!roomsParser.getFeedback().isGenerated()){
            System.out.println(roomsParser.getFeedback().toString());
            return;
        }

        ProfessorParser professorParser = new ProfessorParser(professorsFile, ucMapParser.getTopics());
        professorParser.generate();
        if(!professorParser.getFeedback().isGenerated()){
            System.out.println(professorParser.getFeedback().toString());
            return;
        }

        List<Student> students = new ArrayList<Student>(ucMapParser.getStudents().values());
        List<Topic> topics = new ArrayList<Topic>(ucMapParser.getTopics());
        List<Room> rooms = new ArrayList<Room>(roomsParser.getRooms().values());
        List<Professor> professors = new ArrayList<Professor>(professorParser.getProfessors().values());
        List<Period> periods= ExaminationGenerator.createPeriods(100);
        System.out.println(periods.toString());

        Examination unsolvedExamination = new Examination();
        unsolvedExamination.setRoomList(rooms);
        unsolvedExamination.setPeriodList(periods);
        unsolvedExamination.setTopicList(topics);
        ExaminationGenerator.addRoomPeriods(unsolvedExamination,rooms,periods);
        ExaminationGenerator.addExamsFromTopics(unsolvedExamination,topics);

        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();
        solvedExamination.removeNullPeriodsExams();
        solvedExamination.sort();
        System.out.println(solvedExamination);

        return;
    }
}
