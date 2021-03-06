package examcalendar.optimizer.domain;

import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Exam implements Comparable<Exam> {

	private int id;
	private int numStudents;
	//private String name;
	private Topic topic;
	private boolean pc = false;
	private boolean normal; // or appeal
	/**
	 * shadow variable
	 */
	private List<RoomPeriod> roomPeriods;

	public Exam(){}

	public Exam(int numStudents, boolean normal, boolean pc, Topic topic){
		this();
		this.topic = topic;
		this.numStudents = numStudents;
		this.normal = normal;
		this.pc = pc;
	}

	public Exam(boolean normal, Topic topic){
		this();
		this.topic = topic;
		this.normal = normal;
		this.numStudents = topic.getStudentList().size();
	}

	public void setId(int id) { this.id = id; }

	public int getNumStudents() { return this.numStudents; }
	public void setNumStudents(int numStudents) { this.numStudents = numStudents; };
	public boolean getPC() { return this.pc; }
	public void setPC(boolean pc) { this.pc = pc; }

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public int getId() {
		return id;
	}

	public boolean isNormal() { return normal; }
	public boolean isAppeal() { return !normal; }

	public void setNormal() { normal = true; };
	public void setAppeal() { normal = false; };

	@Override
	public String toString() {
		return (normal ? "Normal" : "Appeal") + " exam of " + topic + " (" + (pc ? "" : "non-") + "pc)";
	}

	@Override
	public int compareTo(Exam o) {
		int yearDiff = topic.getYear() - o.topic.getYear();
		if (yearDiff != 0) {
			return yearDiff;
		} else {
			return id - o.id;
		}
	}
}