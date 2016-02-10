package examination;

import examination.domain.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Gustavo on 10/02/2016.
 */
public class ExaminationGenerator {
    public Examination createExamination() {
        Examination examination = new Examination();

        List<Topic> topicList = new ArrayList<Topic>();
        topicList.add(createTopic(0, "COMP", 157));
        topicList.add(createTopic(1, "IART", 154));
        topicList.add(createTopic(2, "LBAW", 135));
        topicList.add(createTopic(3, "PPIN", 118));
        topicList.add(createTopic(4, "SDIS", 152));
        examination.setTopicList(topicList);

        List<Exam> examList = new ArrayList<Exam>();
        for (Topic topic : topicList) {
            // Época normal
            LeadingExam exam1 = new LeadingExam();
            exam1.setTopic(topic);

            // Época recurso
            FollowingExam exam2 = new FollowingExam();
            exam2.setTopic(topic);

            List<FollowingExam> followingExamList = new ArrayList<FollowingExam>();
            followingExamList.add(exam2);
            exam1.setFollowingExamList(followingExamList);

            examList.add(exam1);
            examList.add(exam2);
        }
        examination.setExamList(examList);

        examination.setPeriodList(createPeriods());

        List<Room> roomList = new ArrayList<Room>();
        roomList.add(createRoom(0, 70));
        roomList.add(createRoom(1, 80));
        roomList.add(createRoom(2, 100));
        roomList.add(createRoom(3, 120));
        roomList.add(createRoom(4, 70));
        roomList.add(createRoom(5, 80));
        roomList.add(createRoom(6, 100));
        roomList.add(createRoom(7, 120));
        examination.setRoomList(roomList);

        examination.setPeriodPenaltyList(new ArrayList<PeriodPenalty>());

        examination.setRoomPenaltyList(new ArrayList<RoomPenalty>());

        return examination;
    }

    private Topic createTopic(long id, String name, int numStudents) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setName(name);
        topic.setDuration(3 * 60);
        List<Student> students = new ArrayList<Student>();
        for (int i = 0; i < numStudents; i++) {
            students.add(new Student());
        }
        topic.setStudentList(students);
        return topic;
    }

    private List<Period> createPeriods() {
        List<Period> periods = new ArrayList<Period>();
        for (int i = 0; i < 100; i++) {
            Period period = new Period();
            period.setDayIndex(i / 10);
            period.setDuration(3 * 60);
            period.setFrontLoadLast(false);
            period.setPenalty(0);
            period.setPeriodIndex(i);
            periods.add(period);
        }
        return periods;
    }

    private Room createRoom(long id, int capacity) {
        Room room = new Room();
        room.setId(id);
        room.setCapacity(capacity);
        return room;
    }
}