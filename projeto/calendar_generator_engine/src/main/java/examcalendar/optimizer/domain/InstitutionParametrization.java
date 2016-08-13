package examcalendar.optimizer.domain;

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
	private int minDaysBetweenSameYearExams = 3;
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

	public void setMinDaysBetweenSameTopicExams(int minDaysBetweenSameTopicExams) {
		this.minDaysBetweenSameTopicExams = minDaysBetweenSameTopicExams;
	}

	public void setMinDaysBetweenSameYearExams(int minDaysBetweenSameYearExams) {
		this.minDaysBetweenSameYearExams = minDaysBetweenSameYearExams;
	}

	public void setRoomUsableRatio(float roomUsableRatio) {
		this.roomUsableRatio = roomUsableRatio;
	}

	public void setRoomUsableMargin(int roomUsableMargin) {
		this.roomUsableMargin = roomUsableMargin;
	}

	public void setSpreadPenalty(float spreadPenalty) {
		this.spreadPenalty = spreadPenalty;
	}

	public void setDifficultyPenalty(float difficultyPenalty) {
		this.difficultyPenalty = difficultyPenalty;
	}

	public void setPeriodPenalty(float periodPenalty) {
		this.periodPenalty = periodPenalty;
	}
}