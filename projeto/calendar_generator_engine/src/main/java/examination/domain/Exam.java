package examination.domain;

import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Exam {

	private static int currId = 0;
	public int id;
	private int year;
	private int numStudents;
	private boolean pc;
	/**
	 * shadow variable
	 */
	private List<RoomPeriod> roomPeriods;
	private Topic topic;

	public Exam(){
		this.attributeId();
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


	@Override
	public String toString() {
		return "Exam with id = "+ this.id + " " + topic.toString();
	}
}