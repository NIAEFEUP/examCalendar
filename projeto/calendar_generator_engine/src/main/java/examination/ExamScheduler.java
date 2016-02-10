package examination;

import examination.domain.Examination;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Created by Duarte on 10/02/2016.
 */
public class ExamScheduler {
    public static void main(String [] args){
        System.out.println("Main");
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        Examination unsolvedExamination = new Examination();
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();

        System.out.println("aaa");
    }
}