package examination.domain;

import java.util.Random;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Student {

	private long id;
	private String name;

	public Student(){
		this.id = new Random().nextLong();
	}

	public Student(long id){
		this.id = id;
	}

	public Student(long id, String name){
		this.id = id;
		this.name = name;
	}

	public void finalize() throws Throwable {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student id = " + id;
	}
}