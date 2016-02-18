package examination.domain;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Period {

	private int dayIndex;
	private PeriodTime time;
	private int penalty;

	public Period(int dayIndex, PeriodTime time){
		this.dayIndex = dayIndex;
		this.time = time;
		this.penalty = this.time.ordinal();
	}

	public void finalize() throws Throwable {

	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}


}