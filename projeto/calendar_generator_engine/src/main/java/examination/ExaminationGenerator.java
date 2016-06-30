package examination;

import examination.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
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

        return examination;
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
        //Exam(int numStudents, boolean pc, Topic topic)
        //1 year 2 semester
        /*examList.add(new Exam(120, false, new Topic(1, "FISI1")));
        examList.add(new Exam(120, false, new Topic(1, "MPCP")));
        examList.add(new Exam(120, false, new Topic(1, "MEST")));
        examList.add(new Exam(120, false, new Topic(1, "PROG")));

        //2 year 1 semester
        examList.add(new Exam(120, false, new Topic(2, "AEDA")));
        examList.add(new Exam(120, false, new Topic(2, "FISI2")));
        examList.add(new Exam(120, true, new Topic(2, "MNUM")));
        examList.add(new Exam(120, false, new Topic(2, "TCOM")));

        //2 year 2 semester
        examList.add(new Exam(120, false, new Topic(2, "BDAD")));
        examList.add(new Exam(120, false, new Topic(2, "CGRA")));
        examList.add(new Exam(120, false, new Topic(2, "CAL")));
        examList.add(new Exam(120, false, new Topic(2, "SOPE")));

        //3 year 1 semester
        examList.add(new Exam(120, false, new Topic(3, "ESOF")));
        examList.add(new Exam(120, false, new Topic(3, "LTW")));
        examList.add(new Exam(120, true, new Topic(3, "PLOG")));
        examList.add(new Exam(120, false, new Topic(3, "RCOM")));

        //3 year 2 semester
        examList.add(new Exam(120, false, new Topic(3, "IART")));
        examList.add(new Exam(120, false, new Topic(3, "SDIS")));*/

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
