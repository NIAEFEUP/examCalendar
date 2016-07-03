package examination.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Topic {

	public int id;
	private int year;
	private String name;
	private String acronym;
	private String code;
	private Set<Student> studentList = new HashSet<Student>();
	private Professor regent;
	private int difficulty = 2;

	public Topic(){	}

	public Topic(int id, int year, String name, String acronym, String code){
		this.id = id;
		this.year = year;
		this.name = name;
		this.acronym = acronym;
		this.code = code;
	}

	public void finalize() throws Throwable {

	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) { this.id = id; }

	@Override
	public String toString() {
		return "Topic with id = "+this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() { return acronym; }

	public void setAcronym(String acronym) { this.acronym = acronym; }

	public String getCode() { return code; }

	public void setCode(String code) { this.code = code; }

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Set<Student> getStudentList() { return studentList; }

	public void setStudentList(Set<Student> studentList) { this.studentList = studentList; }

	public void addStudent(Student student) { studentList.add(student); }

	public Professor getRegent() {
		return regent;
	}

	public void setRegent(Professor regent) {
		this.regent = regent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Topic topic = (Topic) o;

		if (id == topic.id) return true;
		return code.equals(topic.code);
	}

	@Override
	public int hashCode() {
		return id;
	}
}