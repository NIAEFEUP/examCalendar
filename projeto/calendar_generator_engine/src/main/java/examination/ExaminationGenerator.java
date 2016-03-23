package examination;

import examination.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Gustavo on 10/02/2016.
 */
public class ExaminationGenerator {
    public Examination createExamination() {
        Examination examination = new Examination();

        //5 weeks
        ArrayList<Period> periodList = createPeriods(5);
        ArrayList<Room> roomList = createRooms();

        addRoomPeriods(examination, roomList, periodList);

        examination.setExamList(createExams());
        examination.setPeriodList(periodList);
        examination.setRoomList(roomList);

        //this is only useful for test purposes
        //different topics with same student
        setTopicsToExams(examination.getExamList());

        return examination;
    }

    private void setTopicsToExams(List<Exam> examList) {

        for (int i = 0; i < examList.size(); i+=2) {
            Topic tp = new Topic();
            examList.get(i).setTopic(tp);
            examList.get(i+1).setTopic(tp);
        }
    }

    private ArrayList<Period> createPeriods(int numWeeks) {
        ArrayList<Period> periodList = new ArrayList<Period>();
        for (int i = 0; i < numWeeks; i++) {
            int offset = 7 * i;
            for (int j = 0; j < 5; j++) {
                periodList.add(new Period(offset + j, PeriodTime.NINE_AM));
                periodList.add(new Period(offset + j, PeriodTime.ONE_PM));
                periodList.add(new Period(offset + j, PeriodTime.FIVE_PM));
            }
        }
        return periodList;
    }

    private ArrayList<Room> createRooms() {
        ArrayList<Room> roomList = new ArrayList<Room>();
        //Room(ID, Capacity, pc_room?)
        roomList.add(new Room("B116", 69, false));
        roomList.add(new Room("B120", 69, false));
        roomList.add(new Room("B215", 82, false));
        roomList.add(new Room("B221", 89, false));
        roomList.add(new Room("B227", 89, false));
        roomList.add(new Room("B231", 69, false));
        roomList.add(new Room("B232C", 44, false));
        roomList.add(new Room("B338", 69, false));

        //fake rooms
        roomList.add(new Room("B001", 69, true));
        roomList.add(new Room("B002", 44, true));
        roomList.add(new Room("B003", 69, true));

        return roomList;
    }

    private List<Exam> createExams() {
        List<Exam> examList = new ArrayList<Exam>();
        //Exam(int year, int numStudents, String name, boolean pc)
        //1 year 2 semester
        examList.add(new Exam(1, 120, "FISI1", false));
        examList.add(new Exam(1, 120, "MPCP", false));
        examList.add(new Exam(1, 120, "MEST", false));
        examList.add(new Exam(1, 120, "PROG", false));

        //2 year 1 semester
        examList.add(new Exam(2, 120, "AEDA", false));
        examList.add(new Exam(2, 120, "FISI2", false));
        examList.add(new Exam(2, 120, "MNUM", true));
        examList.add(new Exam(2, 120, "TCOM", false));

        //2 year 2 semester
        examList.add(new Exam(2, 120, "BDAD", false));
        examList.add(new Exam(2, 120, "CGRA", false));
        examList.add(new Exam(2, 120, "CAL", false));
        examList.add(new Exam(2, 120, "SOPE", false));

        //3 year 1 semester
        examList.add(new Exam(3, 120, "ESOF", false));
        examList.add(new Exam(3, 120, "LTW", false));
        examList.add(new Exam(3, 120, "PLOG", true));
        examList.add(new Exam(3, 120, "RCOM", false));

        //3 year 2 semester
        examList.add(new Exam(3, 120, "IART", false));
        examList.add(new Exam(3, 120, "SDIS", false));

        return examList;
    }

    private void addRoomPeriods(Examination examination, List<Room> rooms, List<Period> periods) {
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = 0; j < periods.size(); j++) {
                RoomPeriod rp = new RoomPeriod();
                rp.setRoom(rooms.get(i));
                rp.setPeriod(periods.get(j));
                examination.addRoomPeriod(rp);
            }
        }
    }
}