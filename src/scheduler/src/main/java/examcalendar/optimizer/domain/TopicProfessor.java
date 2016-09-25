package examcalendar.optimizer.domain;

/**
 * Created by Gustavo on 05/07/2016.
 */
public class TopicProfessor {
    private int id;
    private Topic topic;
    private Professor professor;

    public TopicProfessor(Topic topic, Professor professor) {
        this.topic = topic;
        this.professor = professor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
