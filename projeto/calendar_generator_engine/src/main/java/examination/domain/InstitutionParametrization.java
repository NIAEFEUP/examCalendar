package examination.domain;

/**
 * TODO:
 * <ul>
 * 	<li>Standard deviation penalty relative to the difficulty </li>
 * 	<li>Optimize break time </li>
 * </ul>
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class InstitutionParametrization {

	private int minDaysBetweenSameTopicExams;
	private float roomUsableRatio;
	private int roomUsableMargin;
	private float spreadPenalty;
	private float difficultyPenalty;

	public InstitutionParametrization(){

	}

	public void finalize() throws Throwable {

	}

	public int getMinDaysBetweenSameTopicExams() {
		return minDaysBetweenSameTopicExams;
	}

	public float getRoomUsableRatio() {
		return roomUsableRatio;
	}

	public int getRoomUsableMargin() {
		return roomUsableMargin;
	}

	public float getSpreadPenalty() {
		return spreadPenalty;
	}

	public float getDifficultyPenalty() {
		return difficultyPenalty;
	}
}