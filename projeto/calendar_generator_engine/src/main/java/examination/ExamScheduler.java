package examination;

import examination.domain.Examination;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Created by Duarte on 10/02/2016.
 */
public class ExamScheduler {
    public static void main(String [] args){
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        Examination unsolvedExamination = new ExaminationGenerator().createExamination();
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();
        solvedExamination.removeNullPeriodsExams();
        solvedExamination.sort();
        System.out.println(solvedExamination);
    }
}