package examination.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
@PlanningSolution()
public class Examination implements Solution<HardSoftScore> {
	private List<RoomPeriod> roomPeriodList;

	public List<Exam> examList;
	private List<Period> periodList;
	public InstitutionParametrization m_InstitutionParametrization;

	public Examination(){
		this.examList = new ArrayList<Exam>();
		this.roomPeriodList = new ArrayList<RoomPeriod>();
	}

	@PlanningEntityCollectionProperty
	public List<RoomPeriod> getRoomPeriodList() {
		return roomPeriodList;
	}
	public void addRoomPeriod(RoomPeriod roomPeriod){ this.roomPeriodList.add(roomPeriod); }

	@ValueRangeProvider(id = "examRange")
	public List<Exam> getExamList() {
		return this.examList;
	}

	public void setExamList(List<Exam> periodList) {
		this.examList = periodList;
	}

	@Override
	public HardSoftScore getScore() {
		return HardSoftScore.valueOf(0,0);
	}

	@Override
	public void setScore(HardSoftScore score) {

	}

	@Override
	public Collection<?> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(examList);
		facts.addAll(getPeriodList());
		return facts;
	}

	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}
}