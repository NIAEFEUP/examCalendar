package examcalendar.optimizer.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.*;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
@PlanningSolution()
public class Examination {
	private HardSoftScore score;
	private List<RoomPeriod> roomPeriodList = new ArrayList<RoomPeriod>();
	private List<Topic> topicList = new ArrayList<Topic>();
	private List<TopicProfessor> topicProfessors = new ArrayList<TopicProfessor>();
	public List<Exam> examList = new ArrayList<Exam>();
	private List<Period> periodList = new ArrayList<Period>();
	private List<Room> roomList = new ArrayList<Room>();
	private List<ProfessorUnavailable> professorUnavailableList = new ArrayList<ProfessorUnavailable>();

	private InstitutionParametrization institutionParametrization = new InstitutionParametrization();

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
	@ProblemFactCollectionProperty
	public List<Exam> getExamList() {
		return this.examList;
	}

	public void setExamList(List<Exam> periodList) {
		this.examList = periodList;
	}

	@PlanningScore
	public HardSoftScore getScore() {
		return this.score;
	}
	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	@ProblemFactCollectionProperty
	private List<TopicConflict> calculateTopicConflictList() {
		List<TopicConflict> topicConflictList = new ArrayList<TopicConflict>();
		for (Topic leftTopic : topicList) {
			for (Topic rightTopic : topicList) {
				if (leftTopic.getId() < rightTopic.getId()) {
					int studentSize = 0;
					for (Student student : leftTopic.getStudentList()) {
						if (rightTopic.getStudentList().contains(student)) {
							studentSize++;
						}
					}
					if (studentSize > 0) {
						topicConflictList.add(new TopicConflict(leftTopic, rightTopic, studentSize));
					}
				}
			}
		}
		return topicConflictList;
	}

	@ProblemFactCollectionProperty
	public List<Period> getPeriodList() {
		return periodList;
	}

	public void setPeriodList(List<Period> periodList) {
		this.periodList = periodList;
	}

	@ProblemFactCollectionProperty
	public List<Room> getRoomList() { return roomList; }

	public void setRoomList(List<Room> roomList) { this.roomList = roomList; }

	@ProblemFactCollectionProperty
	public List<ProfessorUnavailable> getProfessorUnavailableList() { return professorUnavailableList; }

	public void setProfessorUnavailableList(List<ProfessorUnavailable> professorUnavailableList) { this.professorUnavailableList = professorUnavailableList; }

	@ProblemFactCollectionProperty
	public List<Topic> getTopicList() { return topicList; }

	public void setTopicList(List<Topic> topicList) { this.topicList = topicList; }

	@ProblemFactCollectionProperty
	public List<TopicProfessor> getTopicProfessors() { return topicProfessors; }

	public void setTopicProfessors(List<TopicProfessor> topicProfessors) { this.topicProfessors = topicProfessors; }

	@ProblemFactProperty
	public InstitutionParametrization getInstitutionParametrization() {
		return institutionParametrization;
	}

	public void setInstitutionParametrization(InstitutionParametrization institutionParametrization) {
		this.institutionParametrization = institutionParametrization;
	}

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