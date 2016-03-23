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
	private int year;
	private int numStudents;
	private String name;
	private boolean pc;
	/**
	 * shadow variable
	 */
	private List<RoomPeriod> roomPeriods;
	private Topic topic;

	public Exam(){
		this.attributeId();
	}

	public Exam(int year, int numStudents, String name, boolean pc){
		this();
		this.year = year;
		this.numStudents = numStudents;
		this.name = name;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}


	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Exam with id = "+ this.id + " " + topic.toString();
	}

	@Override
	public int compareTo(Exam o) {
		int yearDiff = year - o.year;
		if (yearDiff != 0) {
			return yearDiff;
		} else {
			return id - o.id;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}