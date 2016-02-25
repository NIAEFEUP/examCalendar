package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Professor {

	private String id;

	public List<Auxiliar> auxliarList;
	public List<Regent> regentList;
	List<ProfessorUnavailable> unavailableList;

	public Professor(){
		unavailableList = new ArrayList<ProfessorUnavailable>();
		auxliarList = new ArrayList<Auxiliar>();
		regentList = new ArrayList<Regent>();
	}

	public void addAsAuxiliar(Auxiliar auxiliar){
		this.auxliarList.add(auxiliar);
	}

	public void addAsRegent(Regent regent){
		this.regentList.add(regent);
	}

	public void addUnavailability(ProfessorUnavailable unavailability){
		this.unavailableList.add(unavailability);
	}

	public List<ProfessorUnavailable> getUnavailableList() {
		return unavailableList;
	}

	public void setUnavailableList(List<ProfessorUnavailable> unavailableList) {
		this.unavailableList = unavailableList;
	}

	public List<Regent> getRegentList() {
		return regentList;
	}

	public void setRegentList(List<Regent> regentList) {
		this.regentList = regentList;
	}

	public List<Auxiliar> getAuxliarList() {
		return auxliarList;
	}

	public void setAuxliarList(List<Auxiliar> auxliarList) {
		this.auxliarList = auxliarList;
	}

	public void finalize() throws Throwable {

	}

}