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
    public static Examination createExamination() {
        Examination examination = new Examination();

        //5 weeks
        ArrayList<Period> periodList = createPeriods(5);
        ArrayList<Room> roomList = createRooms();

        addRoomPeriods(examination, roomList, periodList);

        examination.setTopicList(new ArrayList<Topic>()); // TODO
        examination.setExamList(createExams());
        examination.setPeriodList(periodList);
        examination.setRoomList(roomList);
        examination.setProfessorUnavailableList(new ArrayList<ProfessorUnavailable>()); // TODO

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
        //Exam(int numStudents, boolean pc, Topic topic)
        //1 year 2 semester
        examList.add(new Exam(120, false,true, new Topic("FIS1", 1, "Física 1")));
        examList.add(new Exam(120, false,true, new Topic("MPCP", 1, "Microprocessadores e Computadores Pessoais")));
        examList.add(new Exam(120, false,true, new Topic("MEST", 1, "Métodos Estatísticos")));
        examList.add(new Exam(120, false,true, new Topic("PROG", 1, "Programação")));

        //2 year 1 semester
        examList.add(new Exam(120, false,true, new Topic("AEDA", 2, "Algoritmos e Estruturas de Dados")));
        examList.add(new Exam(120, false,true, new Topic("FIS2", 2, "Física 2")));
        examList.add(new Exam(120, true,true, new Topic("MNUM", 2, "Métodos Numéricos")));
        examList.add(new Exam(120, false,true, new Topic("TCOM", 2, "Teoria da Computação")));

        //2 year 2 semester
        examList.add(new Exam(120, false,true, new Topic("BDAD", 2, "Bases de Dados")));
        examList.add(new Exam(120, false,true, new Topic("CGRA", 2, "Computação Gráfica")));
        examList.add(new Exam(120, false,true, new Topic("CAL", 2, "Concepção e Análise de Algoritmos")));
        examList.add(new Exam(120, false,true, new Topic("SOPE", 2, "Sistemas Operativos")));

        //3 year 1 semester
        examList.add(new Exam(120, false,true, new Topic("ESOF", 3, "Engenharia de Software")));
        examList.add(new Exam(120, false,true, new Topic("LTW", 3, "Linguagens e Tecnologias Web")));
        examList.add(new Exam(120, true,true, new Topic("PLOG", 3, "Programação em Lógica")));
        examList.add(new Exam(120, false,true, new Topic("RCOM", 3, "Redes de Computadores")));

        //4 year 1 semester
        examList.add(new Exam(120, true, false, new Topic("SINF",4, "Sistemas de Informação")));
        examList.add(new Exam(120, true, false, new Topic("GEMP",4, "Gestão de Empresas") ));
        examList.add(new Exam(120, true, false, new Topic("AIAD",4, "Agentes e Inteligência Artificial Distribuída")));
        examList.add(new Exam(120, true, false, new Topic("MFES",4, "Métodos Formais em Engenharia de Software" )));

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
