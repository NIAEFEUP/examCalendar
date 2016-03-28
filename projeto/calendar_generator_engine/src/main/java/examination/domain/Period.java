package examination.domain;

/**
 * @author Gustavo
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Period implements Comparable<Period> {

	private int dayIndex;
	private PeriodTime time;
	private int penalty;

	public Period(int dayIndex, PeriodTime time){
		this.dayIndex = dayIndex;
		this.time = time;
		this.penalty = this.time.ordinal();
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

	@Override
	public String toString() {
		return "Period{" +
				"dayIndex=" + dayIndex +
				", time=" + time +
				'}';
	}

	public PeriodTime getTime() {
		return time;
	}

	public void setTime(PeriodTime time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Period) {
			Period op2 = (Period) obj;
			return dayIndex == op2.dayIndex && time.equals(op2.time);
		}
		return false;
	}

	@Override
	public int compareTo(Period p) {
		if (dayIndex != p.dayIndex) {
			return dayIndex - p.dayIndex;
		}

		return time.ordinal() - p.time.ordinal();
	}
}