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

	private int minDaysBetweenSameTopicExams = 14;
	private int minDaysBetweenSameYearExams = 2;
	private float roomUsableRatio = 0.7f;
	private int roomUsableMargin = 10;
	private float spreadPenalty = 10;
	private float difficultyPenalty = 1;
	private float periodPenalty = 1;

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

	public float getPeriodPenalty() {
		return periodPenalty;
	}

	public int getMinDaysBetweenSameYearExams() {
		return minDaysBetweenSameYearExams;
	}
}