package examination.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
@PlanningSolution()
public class Examination implements Solution<HardSoftScore> {
	private HardSoftScore score;
	private List<RoomPeriod> roomPeriodList;
	private List<Topic> topicList;
	public List<Exam> examList;
	private List<Period> periodList;
	private List<Room> roomList;
	private List<ProfessorUnavailable> professorUnavailableList;
	public InstitutionParametrization institutionParametrization = new InstitutionParametrization();

	public Examination(){
		this.examList = new ArrayList<Exam>();
		this.roomPeriodList = new ArrayList<RoomPeriod>();
	}

	@PlanningEntityCollectionProperty
	public List<RoomPeriod> getRoomPeriodList() {
		return roomPeriodList;
	}
	public void addRoomPeriod(RoomPeriod roomPeriod){ this.roomPeriodList.add(roomPeriod); }
    public void setRoomPeriodList(List<RoomPeriod> roomPeriodList) { this.roomPeriodList = roomPeriodList; };

	@ValueRangeProvider(id = "examRange")
	public List<Exam> getExamList() {
		return this.examList;
	}

	@ValueRangeProvider(id = "normalExamRange")
	public List<Exam> getNormalExamList() {
		List<Exam> l = new ArrayList<Exam>(this.examList);
		for (Iterator<Exam> iter = l.listIterator(); iter.hasNext(); ) {
			Exam a = iter.next();
			if (!a.isNormal()) {
				iter.remove();
			}
		}
        System.exit(0);
		return l;
	}

	@ValueRangeProvider(id = "appealExamRange")
	public List<Exam> getAppealExamList() {
		List<Exam> l = new ArrayList<Exam>(this.examList);
		for (Iterator<Exam> iter = l.listIterator(); iter.hasNext(); ) {
			Exam a = iter.next();
			if (!a.isAppeal()) {
				iter.remove();
			}
		}
		return l;
	}

	public void setExamList(List<Exam> periodList) {
		this.examList = periodList;
	}

	@Override
	public HardSoftScore getScore() {
		return this.score;
	}

	@Override
	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	@Override
	public Collection<?> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(topicList);
		facts.addAll(examList);
		facts.addAll(periodList);
		facts.addAll(roomList);
		facts.addAll(professorUnavailableList);
		facts.add(institutionParametrization);

		return facts;
	}

	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}

	public List<Room> getRoomList() { return roomList; }

	public void setRoomList(List<Room> roomList) { this.roomList = roomList; }

	public List<ProfessorUnavailable> getProfessorUnavailableList() { return professorUnavailableList; }

	public void setProfessorUnavailableList(List<ProfessorUnavailable> professorUnavailableList) { this.professorUnavailableList = professorUnavailableList; }

	public List<Topic> getTopicList() { return topicList; }

	public void setTopicList(List<Topic> topicList) { this.topicList = topicList; }

	public void sort() {
		Collections.sort(roomPeriodList);
	}

	@Override
	public String toString() {
		String result = "";
		int examId = 0;
		Period period = null;
		for (RoomPeriod rp : roomPeriodList) {
			if (rp.getExam() == null) continue;
			//first period
			if (period == null) {
				period = rp.getPeriod();
				examId = rp.getExam().getId();

				result += createBegin(rp);
				continue;
			}
			//if same period than before
			if (period.equals(rp.getPeriod())) {
				//if a new room for the same exam
				if (examId == rp.getExam().getId()) {
					result += continueExam(rp);
				} else {
					examId = rp.getExam().getId();

					result += sameDayMoreExams(rp);
				}
			} else {
				period = rp.getPeriod();
				examId = rp.getExam().getId();

				result += createBegin(rp);
			}
		}
		return result;
	}

	private String sameDayMoreExams(RoomPeriod rp) {
		return "\n\t" + rp.getExam().getTopic().getName() + (rp.getExam().isNormal() ? " N" : " A") + " (" + rp.getExam().getId() + ") - " + rp.getRoom().getCodRoom() + " ";
	}

	private String continueExam(RoomPeriod rp) {
		return rp.getRoom().getCodRoom() + " ";
	}

	private String createBegin(RoomPeriod rp) {
		return "\n" + rp.getPeriod() + sameDayMoreExams(rp);
	}

	public void removeNullPeriodsExams() {
		for(int i = 0; i < roomPeriodList.size();i++){
			if(roomPeriodList.get(i).getExam() == null){
				roomPeriodList.remove(i--);
			}
		}
	}
}