package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duarte on 25/02/2016.
 */
public class ProfessorUnavailable {
    List<Period> periods;

    public ProfessorUnavailable(){
        this.periods = new ArrayList<Period>();
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
