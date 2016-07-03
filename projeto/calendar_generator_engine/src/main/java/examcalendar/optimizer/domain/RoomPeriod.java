package examcalendar.optimizer.domain;

import examcalendar.optimizer.domain.solver.RoomPeriodDifficultyComparator;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
@PlanningEntity(difficultyComparatorClass = RoomPeriodDifficultyComparator.class)
public class RoomPeriod implements Comparable<RoomPeriod> {
	private static int currID = 0;
	private int id;
	private Room room;
	public Exam exam;
	private Period period;

	@PlanningVariable(nullable = true,valueRangeProviderRefs = {"examRange"})
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam){
		this.exam = exam;}

	public RoomPeriod(){
		assignID();
	}

	private void assignID(){
		this.id = currID;
		currID++;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "RoomPeriod{" +
				"room=" + room +
				", exam=" + exam +
				", period=" + period +
				'}';
	}

	@Override
	public int compareTo(RoomPeriod o) {
		int cmp = period.compareTo(o.period);
		if (cmp == 0) {
            return exam.compareTo(o.exam);
		}

        return cmp;
	}
}