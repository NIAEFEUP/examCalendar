package examcalendar.optimizer.domain.solver;

import examcalendar.optimizer.domain.RoomPeriod;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Created by Gustavo on 29/06/2016.
 */
public class SwapMoveFilter implements SelectionFilter<SwapMove> {
    @Override
    public boolean accept(ScoreDirector scoreDirector, SwapMove swapMove) {
        RoomPeriod leftRP = (RoomPeriod) swapMove.getLeftEntity();
        RoomPeriod rightRP = (RoomPeriod) swapMove.getRightEntity();

        return rightRP.getExam() == null || rightRP.getExam().getPC() == rightRP.getRoom().isPc();
    }
}
