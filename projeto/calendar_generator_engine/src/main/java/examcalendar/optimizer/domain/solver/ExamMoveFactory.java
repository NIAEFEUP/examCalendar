package examcalendar.optimizer.domain.solver;

import examcalendar.optimizer.domain.Exam;
import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.domain.Period;
import examcalendar.optimizer.domain.RoomPeriod;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 29/06/2016.
 */
public class ExamMoveFactory implements MoveListFactory<Examination> {
    @Override
    public List<? extends Move> createMoveList(Examination examination) {
        List<Move> moveList = new ArrayList<Move>();
        for (Exam exam : examination.getExamList()) {
            for (Period period : examination.getPeriodList()) {
                int capacity = 0;
                List <RoomPeriod> oldRPs = new ArrayList<RoomPeriod>();
                List <RoomPeriod> newRPs = new ArrayList<RoomPeriod>();
                for (RoomPeriod rp : examination.getRoomPeriodList()) {
                    if (rp.getExam() != null && rp.getExam().equals(exam)) {
                        oldRPs.add(rp);
                    }
                }
                if (oldRPs.size() == 0) continue;
                if (period.equals(oldRPs.get(0).getPeriod())) break;
                for (RoomPeriod rp : examination.getRoomPeriodList()) {
                    if (rp.getPeriod().equals(period) && rp.getExam() == null) {
                        capacity += rp.getRoom().getCapacity();
                        newRPs.add(rp);
                    }
                    if (capacity >= exam.getNumStudents()) break;
                }
                if (capacity < exam.getNumStudents() || oldRPs.size() == 0) continue;
                moveList.add(new ExamMove(exam, oldRPs, newRPs));
            }
        }
        return moveList;
    }
}
