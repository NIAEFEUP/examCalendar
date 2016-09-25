package examcalendar.optimizer.persistence;

import examcalendar.optimizer.ExaminationGenerator;
import examcalendar.optimizer.domain.Examination;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

/**
 * Created by Gustavo on 01/07/2016.
 */
public class ExaminationIO implements SolutionFileIO<Examination> {
    @Override
    public String getInputFileExtension() {
        return null;
    }

    @Override
    public String getOutputFileExtension() {
        return null;
    }

    @Override
    public Examination read(File file) {
        return ExaminationGenerator.createExamination();
    }

    @Override
    public void write(Examination examination, File file) {

    }
}
