package examination;

import examination.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gustavo on 10/02/2016.
 */
public class ExaminationGenerator {
    public Examination createExamination() {
        Examination examination = new Examination();

        ArrayList<Period> periodList = createPeriods();

        RoomPeriod rp1 = new RoomPeriod();
        rp1.setRoom(new Room("B001", 60, false) );
        rp1.setPeriod(periodList.get(0));

        RoomPeriod rp2 = new RoomPeriod();
        rp2.setRoom(new Room("B002", 60, false) );
        rp2.setPeriod(periodList.get(1));

        RoomPeriod rp3 = new RoomPeriod();
        rp3.setRoom(new Room("B003", 60, false) );
        rp3.setPeriod(periodList.get(2));

        examination.addRoomPeriod(rp1);
        examination.addRoomPeriod(rp2);
        examination.addRoomPeriod(rp3);

        examination.setExamList(createExams());
        examination.setPeriodList(periodList);

        return examination;
    }

    private ArrayList<Period> createPeriods() {
        ArrayList<Period> periodList = new ArrayList<Period>();
        periodList.add(new Period(1, PeriodTime.NINE_AM));
        periodList.add(new Period(1, PeriodTime.ONE_PM));
        periodList.add(new Period(1, PeriodTime.FIVE_PM));
        return periodList;
    }

    private List<Exam> createExams() {
        List<Exam> examList = new ArrayList<Exam>();
        for (int i = 0; i < 100; i++) {
            Exam exam = new Exam();
            examList.add(exam);
        }
        return examList;
    }
}