package examination;

import examination.domain.Examination;
import examination.persistence.ExaminationDBImporter;
import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Created by Duarte on 10/02/2016.
 */
public class ExamScheduler {
    public static void main(String [] args){
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        /*PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("examinationBenchmarkConfig.xml");
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark();
        plannerBenchmark.benchmark();*/

        Examination unsolvedExamination = new ExaminationGenerator().createExamination();
        //Examination unsolvedExamination = new ExaminationDBImporter(true).readSolution(1);
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();
        solvedExamination.removeNullPeriodsExams();
        solvedExamination.sort();
        System.out.println(solvedExamination);
    }
}