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
public class Topic{

	public String id;
	private int year;
	private String name;
	private HashSet<Student> studentList = new HashSet<Student>();
	public Set<Auxiliar> auxiliarList = new HashSet<Auxiliar>();
	private Regent regent;
	private int difficulty = 2;

	public Topic(String id, int year, String name){
		this.id = id;
		this.year = year;
		this.name = name;
	}

	public void finalize() throws Throwable {

	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getId() {
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

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof Topic))
			return false;

		return getId().equals(((Topic) obj).getId());
	}
}