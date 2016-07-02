package examination.persistence;

import examination.ExaminationGenerator;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

/**
 * Created by Gustavo on 01/07/2016.
 */
public class ExaminationIO implements SolutionFileIO {
    @Override
    public String getInputFileExtension() {
        return null;
    }

    @Override
    public String getOutputFileExtension() {
        return null;
    }

    @Override
    public Solution read(File file) {
        return ExaminationGenerator.createExamination();
    }

    @Override
    public void write(Solution solution, File file) {

    }
}
