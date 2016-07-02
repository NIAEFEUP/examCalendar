package examination.domain;

import examination.domain.Exam;
import examination.domain.Period;
import examination.domain.Room;
import examination.domain.RoomPeriod;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
@PlanningEntity
public class NormalRoomPeriod extends RoomPeriod {
	@PlanningVariable(nullable = true,valueRangeProviderRefs = {"normalExamRange"})
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam){
		this.exam = exam;}
}