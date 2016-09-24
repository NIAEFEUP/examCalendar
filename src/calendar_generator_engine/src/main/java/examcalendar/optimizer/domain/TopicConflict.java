package examcalendar.optimizer.domain;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class TopicConflict {

	private int studentSize;
	public Topic leftTopic;
	public Topic rightTopic;

	public TopicConflict(Topic leftTopic, Topic rightTopic, int studentSize){
		this.leftTopic = leftTopic;
		this.rightTopic = rightTopic;
		this.studentSize = studentSize;
	}

	public void finalize() throws Throwable {

	}

	public int getStudentSize() {
		return studentSize;
	}

	public void setStudentSize(int studentSize) {
		this.studentSize = studentSize;
	}
}