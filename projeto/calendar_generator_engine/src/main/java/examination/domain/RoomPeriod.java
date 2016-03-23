package examination.domain;

import examination.domain.solver.RoomPeriodDifficultyComparator;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
@PlanningEntity(difficultyComparatorClass = RoomPeriodDifficultyComparator.class)
public class RoomPeriod implements Comparable<RoomPeriod> {
	private static int currId = 0;
	private int id;
	private Room room;
	public Exam exam;
	private Period period;

	public RoomPeriod() {
		this.attributeId();
	}

	private void attributeId(){
		this.currId++;
		this.id = this.currId;
	}

	@PlanningVariable(nullable = true,valueRangeProviderRefs = {"examRange"})
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam){
		this.exam = exam;}

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
		return period.compareTo(o.period);
	}
}