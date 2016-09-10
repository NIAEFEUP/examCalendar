package examcalendar.optimizer.domain.solver;

import examcalendar.optimizer.domain.Exam;
import examcalendar.optimizer.domain.RoomPeriod;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Gustavo on 29/06/2016.
 */
public class ExamMove extends AbstractMove {

    private Exam exam;
    private List<RoomPeriod> oldRPs;
    private List<RoomPeriod> newRPs;

    public ExamMove(Exam exam, List<RoomPeriod> oldRPs, List<RoomPeriod> newRPs) {
        this.exam = exam;
        this.oldRPs = oldRPs;
        this.newRPs = newRPs;
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector scoreDirector) {
        for (RoomPeriod rp : oldRPs) {
            scoreDirector.beforeVariableChanged(rp, "exam");
            rp.setExam(null);
            scoreDirector.afterVariableChanged(rp, "exam");
        }

        for (RoomPeriod rp : newRPs) {
            scoreDirector.beforeVariableChanged(rp, "exam");
            rp.setExam(exam);
            scoreDirector.afterVariableChanged(rp, "exam");
        }
    }

    @Override
    public boolean isMoveDoable(ScoreDirector scoreDirector) {
        return true;
    }

    @Override
    public Move createUndoMove(ScoreDirector scoreDirector) {
        return new ExamMove(exam, newRPs, oldRPs);
    }

    @Override
    public Collection<? extends Object> getPlanningEntities() {
        List<RoomPeriod> newList = new ArrayList<RoomPeriod>(oldRPs);
        newList.addAll(newRPs);
        return newList;
    }

    @Override
    public Collection<? extends Object> getPlanningValues() {
        List<Exam> newList = new ArrayList<Exam>();
        newList.add(exam);
        newList.add(null);
        return newList;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.exam).append(" {");
        s.append(oldRPs.get(0).getPeriod());
        s.append(" -> ");
        s.append(newRPs.get(0).getPeriod());
        s.append("}");
        return s.toString();
    }
}
