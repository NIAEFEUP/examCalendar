package examcalendar.optimizer.domain;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Professor {
	private int id;
	private String name;
	private String acronym;
	private String code;

	public Professor(){ }

	public Professor(String acronym) {
		this.acronym = acronym;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() { return acronym; }

	public void setAcronym(String acronym) { this.acronym = acronym; }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Professor professor = (Professor) o;

		if (id == professor.id)
			return acronym.equals(professor.acronym);
		else
			return false;
	}

	@Override
	public int hashCode() {
		return id;
	}
}