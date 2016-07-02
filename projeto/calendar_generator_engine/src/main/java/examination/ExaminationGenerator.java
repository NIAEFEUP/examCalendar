package examination;

import examination.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Gustavo on 10/02/2016.
 */
public class ExaminationGenerator {
    public static Examination createExamination() {
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

    private static ArrayList<Period> createPeriods(int numWeeks) {
        ArrayList<Period> periodList = new ArrayList<Period>();
        for (int i = 0; i < numWeeks; i++) {
            int offset = 7 * i;
            for (int j = 0; j < 5; j++) {
                int day = offset + j;
                periodList.add(new Period(day, PeriodTime.NINE_AM, day < numWeeks * 7 * 3/5));
                periodList.add(new Period(day, PeriodTime.ONE_PM, day < numWeeks * 7 * 3/5));
                periodList.add(new Period(day, PeriodTime.FIVE_PM, day < numWeeks * 7 * 3/5));
            }
        }
        return periodList;
    }

    private static ArrayList<Room> createRooms() {
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

    private static List<Exam> createExams() {
        List<Exam> examList = new ArrayList<Exam>();
        //Exam(int numStudents, boolean normal, boolean pc, Topic topic)

        //2 year 1 semester
        examList.add(new Exam(120, true, false, new Topic("EIC001",2, "FISI2")));
        examList.add(new Exam(120, true, true, new Topic("EIC002",2, "MNUM")));
        examList.add(new Exam(120, true, false, new Topic("EIC003",2, "TCOM")));

        examList.add(new Exam(120, false, false, new Topic("EIC004",2, "FISI2")));
        examList.add(new Exam(120, false, true, new Topic("EIC005",2, "MNUM")));
        examList.add(new Exam(120, false, false, new Topic("EIC006",2, "TCOM")));

        //3 year 1 semester
        examList.add(new Exam(120, true, true, new Topic("EIC007",3, "ESOF")));
        examList.add(new Exam(120, true, false, new Topic("EIC008",3, "LTW")));
        examList.add(new Exam(120, true, true, new Topic("EIC009",3, "PLOG")));
        examList.add(new Exam(120, true, false, new Topic("EIC010",3, "RCOM")));

        examList.add(new Exam(120, false, true, new Topic("EIC011",3, "ESOF")));
        examList.add(new Exam(120, false, false, new Topic("EIC012",3, "LTW")));
        examList.add(new Exam(120, false, true, new Topic("EIC013",3, "PLOG")));
        examList.add(new Exam(120, false, false, new Topic("EIC014",3, "RCOM")));

        //4 year 1 semester
        examList.add(new Exam(120, true, false, new Topic("EIC015",4, "SINF")));
        examList.add(new Exam(120, true, false, new Topic("EIC016",4, "GEMP")));
        examList.add(new Exam(120, true, false, new Topic("EIC017",4, "AIAD")));
        examList.add(new Exam(120, true, false, new Topic("EIC018",4, "MFES")));

        examList.add(new Exam(120, false, false, new Topic("EIC019",4, "SINF")));
        examList.add(new Exam(120, false, false, new Topic("EIC020",4, "GEMP")));
        examList.add(new Exam(120, false, false, new Topic("EIC021",4, "AIAD")));
        examList.add(new Exam(120, false, false, new Topic("EIC022",4, "MFES")));

        return examList;
    }

    private static void addRoomPeriods(Examination examination, List<Room> rooms, List<Period> periods) {
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