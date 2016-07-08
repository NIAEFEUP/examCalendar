package examcalendar.server;

import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.persistence.ExaminationDBImporter;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

/**
 * Created by Gustavo on 08/07/2016.
 */
public class Scheduler extends Thread {
    private Server server;
    private int requestID;
    public Scheduler(Server server, int requestID) {
        this.server = server;
        this.requestID = requestID;
    }
    @Override
    public void run() {
        // TODO update request with the starting time

        SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        Examination unsolvedExamination = new ExaminationDBImporter(true).readSolution(requestID);
        solver.solve(unsolvedExamination);
        Examination solvedExamination = (Examination) solver.getBestSolution();
        solvedExamination.removeNullPeriodsExams();
        solvedExamination.sort();
        System.out.println(solvedExamination);

        // TODO export solution to the database and update request with the ending time
        synchronized (server) {
            server.notify();
        }
    }
}
