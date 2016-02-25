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
		this.setTime(time);
		this.penalty = this.getTime().ordinal() + 1;
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

	public void setPeriodTime(PeriodTime time) {
		this.penalty = this.getTime().ordinal();
	}

	@Override
	public String toString() {
		return "Period{" +
				"dayIndex=" + dayIndex +
				", time=" + getTime() +
				'}';
	}

	public PeriodTime getTime() {
		return time;
	}

	public void setTime(PeriodTime time) {
		this.time = time;
	}
}