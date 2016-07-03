package examination.domain;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Professor {
	private int id;
	private String name;
	private String acronym;
	private String cod;

	public void finalize() throws Throwable {

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

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}
}