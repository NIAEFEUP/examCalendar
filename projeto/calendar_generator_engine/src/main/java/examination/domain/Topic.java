package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Topic {

	private static int currId = 0;
	public int id;
	private Student m_Student;
	public List<Auxiliar> auxiliarList;
	private Regent regent;

	public Topic(){
		this.attributeId();
		this.auxiliarList = new ArrayList<Auxiliar>();
	}

	private void attributeId(){
		this.currId++;
		this.id = this.currId;
	}

	public void finalize() throws Throwable {

	}

	public Regent getRegent() {
		return regent;
	}

	public void setRegent(Regent regent) {
		this.regent = regent;
	}

	public Student getM_Student() {
		return m_Student;
	}

	public void setM_Student(Student m_Student) {
		this.m_Student = m_Student;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Topic with id = "+this.id;
	}
}