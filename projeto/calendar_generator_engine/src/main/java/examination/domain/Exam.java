package examination.domain;

import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Exam implements Comparable<Exam> {

	private static int currId = 0;
	private int id;
	private int numStudents;
	//private String name;
	private Topic topic;
	private boolean pc;
	private boolean normal; // or appeal
	/**
	 * shadow variable
	 */
	private List<RoomPeriod> roomPeriods;

	public Exam(){
		this.attributeId();
	}

	public Exam(int numStudents, boolean normal, boolean pc, Topic topic){
		this();
		this.topic = topic;
		this.numStudents = numStudents;
		this.normal = normal;
		this.pc = pc;
	}

	private void attributeId(){
		this.currId++;
		this.id = this.currId;
	}

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
		return "Exam with id = "+ this.id + " " + topic.toString();
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