package examcalendar.server;

import examcalendar.optimizer.domain.Exam;
import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.persistence.ExaminationDBExporter;
import examcalendar.optimizer.persistence.ExaminationDBImporter;
import examcalendar.server.handlers.EvaluateRequestHandler;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Gustavo on 08/07/2016.
 */
public class Scheduler extends Thread {
    private Server server;
    private int calendarID;
    public Scheduler(Server server, int calendarID) {
        this.server = server;
        this.calendarID = calendarID;
    }
    @Override
    public void run() {
        try {
            Connection conn = server.createDatabaseConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE calendars SET starttime = NOW() WHERE id = ?");
            ps.setInt(1, calendarID);
            ps.execute();

            ExaminationDBImporter examinationDBImporter = new ExaminationDBImporter(true);
            Examination unsolvedExamination = examinationDBImporter.readSolution(calendarID);

            SolverFactory solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
            TerminationConfig terminationConfig = new TerminationConfig();
            terminationConfig.setSecondsSpentLimit((long)examinationDBImporter.getRequestConfig().timeout); // TODO
            solverFactory.getSolverConfig().setTerminationConfig(terminationConfig);

            Solver solver = solverFactory.buildSolver();

            solver.addEventListener(new SolverEventListener<Examination>() {
                public void bestSolutionChanged(BestSolutionChangedEvent<Examination> event) {
                    // Ignore infeasible (including uninitialized) solutions
                    if (event.getNewBestSolution().getScore().isSolutionInitialized()) {
                        new ExaminationDBExporter(true).writeSolution(event.getNewBestSolution(), calendarID);
                    }
                }
            });

            solver.solve(unsolvedExamination);
            Examination solvedExamination = (Examination) solver.getBestSolution();
            solvedExamination.removeNullPeriodsExams();
            solvedExamination.sort();

            System.out.println(solvedExamination);
            System.out.println(EvaluateRequestHandler.evaluateSolution(solvedExamination));

            new ExaminationDBExporter(true).writeSolution(solvedExamination, calendarID);
            ps = conn.prepareStatement("UPDATE calendars SET endtime = NOW() WHERE id = ?");
            ps.setInt(1, calendarID);
            ps.execute();
            server.notifyEvent(Server.Event.SCHEDULER_END);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
