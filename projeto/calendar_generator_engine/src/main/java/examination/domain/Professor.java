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
	ProfessorUnavailable unavailability;

	public Professor(){
		auxliarList = new ArrayList<Auxiliar>();
		regentList = new ArrayList<Regent>();
	}

	public void addAsAuxiliar(Auxiliar auxiliar){
		this.auxliarList.add(auxiliar);
	}

	public void addAsRegent(Regent regent){
		this.regentList.add(regent);
	}

	public String getId() {
		return id;
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