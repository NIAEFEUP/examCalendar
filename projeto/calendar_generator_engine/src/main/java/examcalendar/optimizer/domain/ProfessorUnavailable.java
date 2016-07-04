package examcalendar.optimizer.domain;

/**
 * Created by Duarte on 25/02/2016.
 */
public class ProfessorUnavailable {
    Professor professor;
    Period period;

    public ProfessorUnavailable() {}
    public ProfessorUnavailable(Professor professor, Period period) {
        this.professor = professor;
        this.period = period;
    }
    public Professor getProfessor() {
        return this.professor;
    }
    public Period getPeriod() {
        return this.period;
    }
    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setPeriod(Period period) { this.period = period; }
}
