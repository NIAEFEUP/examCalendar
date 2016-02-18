package examination.domain;

import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Exam {

	private int numStudents;
	private boolean pc;
	/**
	 * shadow variable
	 */
	private List<RoomPeriod> roomPeriods;
	public Topic m_Topic;

	public Exam(){

	}

	public void finalize() throws Throwable {

	}

}