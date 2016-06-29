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
	private int year;
	private String name;
	private List<Student> studentList = new ArrayList<Student>();
	public List<Auxiliar> auxiliarList = new ArrayList<Auxiliar>();
	private Regent regent;
	private int difficulty = 2;

	public Topic(){
		this.attributeId();
		this.auxiliarList = new ArrayList<Auxiliar>();
		this.studentList = new ArrayList<Student>();
	}

	public Topic(int year, String name){
		this();
		this.year = year;
		this.name = name;
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

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getId() {
		return id;
	}

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Regent getRegent() {
		return regent;
	}

	public void setRegent(Regent regent) {
		this.regent = regent;
	}
}