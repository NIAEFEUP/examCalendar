package examcalendar.optimizer;

import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.persistence.ExaminationDBImporter;
import examcalendar.server.Server;
import examcalendar.server.handlers.EvaluateRequestHandler;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Created by Duarte on 10/02/2016.
 */
public class ExamScheduler implements Runnable {
    private Server server;

    public ExamScheduler(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        /*PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("examinationBenchmarkConfig.xml");
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark();
        plannerBenchmark.benchmark();*/

        //Examination unsolvedExamination = new ExaminationGenerator().createExamination();
        Examination unsolvedExamination = new ExaminationDBImporter(server).readSolution(1);
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();
        solvedExamination.removeNullPeriodsExams();
        solvedExamination.sort();
        System.out.println(solvedExamination);
    }
}