package examcalendar.optimizer.persistence;

import examcalendar.optimizer.common.persistence.AbstractSolutionExporter;
import examcalendar.optimizer.common.persistence.SolutionDao;
import examcalendar.optimizer.domain.Exam;
import examcalendar.optimizer.domain.Examination;
import examcalendar.optimizer.domain.Period;
import examcalendar.optimizer.domain.RoomPeriod;
import org.optaplanner.core.api.domain.solution.Solution;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Gustavo on 15/07/2016.
 */
public class ExaminationDBExporter extends AbstractSolutionExporter {
    public ExaminationDBExporter(SolutionDao solutionDao) {
        super(solutionDao);
    }

    public ExaminationDBExporter(boolean withoutDao) {
        super(withoutDao);
    }

    @Override
    public String getOutputFileSuffix() {
        return null;
    }

    @Override
    public void writeSolution(Solution solution, File outputFile) {

    }

    public void writeSolution(Solution solution, int requestID) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=UTC", "root", ""); // TODO (hardcoded)
            writeExamRooms(requestID, conn, (Examination)solution);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeExamRooms(int creator, Connection conn, Examination examination) throws SQLException {
        for (Exam exam : examination.getExamList()) {
            Period period = null;
            for (RoomPeriod rp : examination.getRoomPeriodList()) {
                if (period == null) {
                    period = rp.getPeriod();
                } else if (rp.getPeriod() != period) {
                    continue;
                }
                PreparedStatement ps = conn.prepareStatement("INSERT INTO examrooms (exam, room) VALUES (?, ?)");
                ps.setInt(1, exam.getId());
                ps.setInt(2, rp.getRoom().getId());
                ps.execute();
            }
            if (period != null) {
                PreparedStatement ps = conn.prepareStatement("UPDATE exams SET day = ?, time = ? WHERE id = ?");
                ps.setDate(1, new java.sql.Date(period.getDate().getTime()));
                ps.setInt(2, period.getTime().ordinal());
                ps.setInt(3, exam.getId());
                ps.execute();
            }
        }
    }
}
