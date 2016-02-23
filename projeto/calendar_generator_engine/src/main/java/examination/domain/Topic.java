package examination.domain;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Topic {

	private static int currId = 0;
	public int id;
	private Student m_Student;
	public Auxiliar m_Auxiliar;
	public Regent m_Regent;

	public Topic(){
		this.attributeId();
	}

	private void attributeId(){
		this.currId++;
		this.id = this.currId;
	}

	public void finalize() throws Throwable {

	}

	public Student getM_Student() {
		return m_Student;
	}

	public void setM_Student(Student m_Student) {
		this.m_Student = m_Student;
	}

	@Override
	public String toString() {
		return "Topic with id = "+this.id;
	}
}