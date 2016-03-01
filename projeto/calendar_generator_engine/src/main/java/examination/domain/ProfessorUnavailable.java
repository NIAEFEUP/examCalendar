package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duarte on 25/02/2016.
 */
public class ProfessorUnavailable {
    List<Period> periods;
    Professor professor;

    public ProfessorUnavailable(){
        this.periods = new ArrayList<Period>();
    }
    public ProfessorUnavailable(Professor professor){
        this.professor = professor;
        this.periods = new ArrayList<Period>();
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void addPeriod(Period period){
        this.periods.add(period);
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
}
