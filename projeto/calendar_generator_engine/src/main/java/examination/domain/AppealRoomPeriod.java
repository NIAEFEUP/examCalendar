package examination.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
@PlanningEntity
public class AppealRoomPeriod extends RoomPeriod {
	@PlanningVariable(nullable = true,valueRangeProviderRefs = {"appealExamRange"})
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam){
		this.exam = exam;}
}