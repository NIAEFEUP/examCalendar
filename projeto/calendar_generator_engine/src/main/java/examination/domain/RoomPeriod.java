package examination.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
@PlanningEntity
public class RoomPeriod {

	private Room room;
	public Exam exam;
	private Period period;

	@PlanningVariable(nullable = true,valueRangeProviderRefs = {"examRange"})
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam){
		this.exam = exam;}
	public void finalize() throws Throwable {

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
}