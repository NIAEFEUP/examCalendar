package examcalendar.optimizer.domain;

/**
 * Created by Gustavo on 05/07/2016.
 */
public class TopicRegent {
    private int id;
    private Topic topic;
    private Professor regent;

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

    public Professor getRegent() {
        return regent;
    }

    public void setRegent(Professor regent) {
        this.regent = regent;
    }
}
