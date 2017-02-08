package examcalendar.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.persistence.ExaminationDBImporter;
import examcalendar.server.Server;
import org.json.JSONException;
import org.json.JSONObject;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.ConstraintMatch;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Gustavo on 10/07/2016.
 */
public class EvaluateRequestHandler extends AbstractRequestHandler {
    private final Server server;
    public EvaluateRequestHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            try {
                String method = exchange.getRequestMethod();
                if (method.equals("GET")) {
                    Map<String, String> params = parseQueryParams(exchange.getRequestURI());
                    String calendar = params.get("calendar");
                    if (calendar == null) {
                        JSONObject data = new JSONObject();
                        data.put("calendar", "Missing calendar ID.");
                        throw new RequestHandlerFailException(400, data);
                    }
                    int calendarID;
                    try {
                        calendarID = Integer.parseInt(params.get("calendar"));
                    } catch (NumberFormatException e) {
                        JSONObject data = new JSONObject();
                        data.put("calendar", "Calendar ID is invalid.");
                        throw new RequestHandlerFailException(400, data);
                    }
                    Examination solution = new ExaminationDBImporter(server).readSolution(calendarID);
                    evaluateSolution(solution);
                    this.sendSuccessResponse(exchange, JSONObject.NULL, 200);
                } else {
                    // Method not allowed
                    JSONObject data = new JSONObject();
                    data.put("method", "Method \"" + method + "\" not allowed.");
                    exchange.getResponseHeaders().add("Allow", "GET");
                    throw new RequestHandlerFailException(405, data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RequestHandlerErrorException(500, "Unknown error.");
            }
        } catch (RequestHandlerException e) {
            e.send(exchange);
        }
    }

    public static JSONObject evaluateSolution(Examination examination) {
        SolverFactory<Examination> solverFactory = SolverFactory.createFromXmlResource("examinationSolverConfig.xml");
        Solver<Examination> solver = solverFactory.buildSolver();
        ScoreDirectorFactory<Examination> scoreDirectorFactory = solver.getScoreDirectorFactory();
        ScoreDirector<Examination> guiScoreDirector = scoreDirectorFactory.buildScoreDirector();
        guiScoreDirector.setWorkingSolution(examination);
        HardSoftScore score = (HardSoftScore) guiScoreDirector.calculateScore();
        JSONObject jsonScore = new JSONObject();
        try {
            jsonScore.put("hard", score.getHardScore());
            jsonScore.put("soft", score.getSoftScore());
        } catch (JSONException e) {
            e.printStackTrace();
            throw new AssertionError();
        }
        System.out.println(jsonScore);
        for (ConstraintMatchTotal constraintMatchTotal : guiScoreDirector.getConstraintMatchTotals()) {
            String constraintName = constraintMatchTotal.getConstraintName();
            Number weightTotal = constraintMatchTotal.getWeightTotalAsNumber();
            for (ConstraintMatch constraintMatch : constraintMatchTotal.getConstraintMatchSet()) {
                List<Object> justificationList = constraintMatch.getJustificationList();
                Number weight = constraintMatch.getWeightAsNumber();
                for (Object justification : justificationList)
                    System.out.print(justification + " - ");
                System.out.println(" --- " + constraintMatch.getIdentificationString() + " --- " + constraintMatch.getConstraintName() + " --- " + weight);
            }
        }
        return jsonScore; // TODO
    }
}
