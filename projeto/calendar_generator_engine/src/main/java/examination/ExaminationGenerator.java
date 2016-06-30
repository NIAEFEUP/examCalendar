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
        examList.add(new Exam(120, false, new Topic("FIS1", 1, "Física 1")));
        examList.add(new Exam(120, false, new Topic("MPCP", 1, "Microprocessadores e Computadores Pessoais")));
        examList.add(new Exam(120, false, new Topic("MEST", 1, "Métodos Estatísticos")));
        examList.add(new Exam(120, false, new Topic("PROG", 1, "Programação")));

        //2 year 1 semester
        examList.add(new Exam(120, false, new Topic("AEDA", 2, "Algoritmos e Estruturas de Dados")));
        examList.add(new Exam(120, false, new Topic("FIS2", 2, "Física 2")));
        examList.add(new Exam(120, true, new Topic("MNUM", 2, "Métodos Numéricos")));
        examList.add(new Exam(120, false, new Topic("TCOM", 2, "Teoria da Computação")));

        //2 year 2 semester
        examList.add(new Exam(120, false, new Topic("BDAD", 2, "Bases de Dados")));
        examList.add(new Exam(120, false, new Topic("CGRA", 2, "Computação Gráfica")));
        examList.add(new Exam(120, false, new Topic("CAL", 2, "Concepção e Análise de Algoritmos")));
        examList.add(new Exam(120, false, new Topic("SOPE", 2, "Sistemas Operativos")));

        //3 year 1 semester
        examList.add(new Exam(120, false, new Topic("ESOF", 3, "Engenharia de Software")));
        examList.add(new Exam(120, false, new Topic("LTW", 3, "Linguagens e Tecnologias Web")));
        examList.add(new Exam(120, true, new Topic("PLOG", 3, "Programação em Lógica")));
        examList.add(new Exam(120, false, new Topic("RCOM", 3, "Redes de Computadores")));

        //3 year 2 semester
        examList.add(new Exam(120, false, new Topic("IART", 3, "Inteligência Artificial")));
        examList.add(new Exam(120, false, new Topic("SDIS", 3, "Sistemas Distribuídos")));

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