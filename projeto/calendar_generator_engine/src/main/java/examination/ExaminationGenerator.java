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

        RoomPeriod rp1 = new RoomPeriod();
        rp1.setRoom(new Room("B001", 60, false) );
        rp1.setPeriod(new Period(1, PeriodTime.NINE_AM));

        RoomPeriod rp2 = new RoomPeriod();
        rp2.setRoom(new Room("B002", 60, false) );
        rp2.setPeriod(new Period(1, PeriodTime.ONE_PM));

        RoomPeriod rp3 = new RoomPeriod();
        rp3.setRoom(new Room("B003", 60, false) );
        rp3.setPeriod(new Period(1, PeriodTime.FIVE_PM));

        examination.addRoomPeriod(rp1);
        examination.addRoomPeriod(rp2);
        examination.addRoomPeriod(rp3);

        return examination;
    }
}