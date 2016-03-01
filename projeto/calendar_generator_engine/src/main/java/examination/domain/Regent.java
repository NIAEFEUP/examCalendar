package examination.domain;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Regent {
	protected Professor professor;

	public Regent(Professor professor){
		this.professor = professor;

	}

	public Professor getProfessor() {
		return professor;
	}

	public void finalize() throws Throwable {

	}

}