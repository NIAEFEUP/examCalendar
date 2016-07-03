package parser;

import examination.domain.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gustavo on 30/06/2016.
 */
public class ConfigParser {
    private File file;
    private int timeout;
    private InstitutionParametrization institutionParametrization;
    private List<RoomPeriod> roomPeriods;
    private ProfessorUnavailable professorUnavailable;
    public ConfigParser(File file) {
        this.file = file;
    }
    public void parse() {
        JSONObject jsonObject = new JSONObject(file);
        try {
            timeout = jsonObject.getInt("timeout");
            int minDaysBetweenSameTopicExams = jsonObject.getInt("minDaysBetweenSameTopicExams");
            float roomUsableRatio = (float)jsonObject.getDouble("roomUsableRatio");
            int roomUsableMargin = jsonObject.getInt("roomUsableMargin");
            int spreadPenalty = jsonObject.getInt("spreadPenalty");
            int difficultyPenalty = jsonObject.getInt("difficultyPenalty");
            int periodPenalty = jsonObject.getInt("periodPenalty");
            int startingDay = jsonObject.getInt("startingDay");
            int startingMonth = jsonObject.getInt("startingMonth");
            int startingYear = jsonObject.getInt("startingYear");
            int maxDays = jsonObject.getInt("maxDays");
            JSONArray unavailableRoomPeriods = jsonObject.getJSONArray("unavailableRoomPeriods");
            JSONArray unavailableProfessors = jsonObject.getJSONArray("unavailableProfessors");

            institutionParametrization = new InstitutionParametrization();
            institutionParametrization.setMinDaysBetweenSameTopicExams(minDaysBetweenSameTopicExams);
            institutionParametrization.setRoomUsableRatio(roomUsableRatio);
            institutionParametrization.setRoomUsableMargin(roomUsableMargin);
            institutionParametrization.setSpreadPenalty(spreadPenalty);
            institutionParametrization.setDifficultyPenalty(difficultyPenalty);
            institutionParametrization.setPeriodPenalty(periodPenalty);

            List<Period> periods = createPeriods(startingDay, startingMonth, startingYear, maxDays);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Period> createPeriods(int startingDay, int startingMonth, int startingYear, int maxDays) {
        Calendar c = Calendar.getInstance();
        c.set(startingDay, startingMonth, startingYear);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        ArrayList<Period> periodList = new ArrayList<Period>();
        for (int i = 0; i < maxDays; i++) {
            if ((dayOfWeek + i) % 7 < 5) { // Working days only
                periodList.add(new Period(startingDay + i, PeriodTime.NINE_AM,true));
                periodList.add(new Period(startingDay + i, PeriodTime.ONE_PM,true));
                periodList.add(new Period(startingDay + i, PeriodTime.FIVE_PM,true));
            }
        }
        return periodList;
    }
}
