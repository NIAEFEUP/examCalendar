/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examination.example_domain.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import examination.example_domain.FollowingExam;
import examination.example_domain.LeadingExam;
import examination.example_domain.Period;

public class PeriodUpdatingVariableListener implements VariableListener<LeadingExam> {

    public void beforeEntityAdded(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        // Do nothing
    }

    public void afterEntityAdded(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        updatePeriod(scoreDirector, leadingExam);
    }

    public void beforeVariableChanged(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        // Do nothing
    }

    public void afterVariableChanged(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        updatePeriod(scoreDirector, leadingExam);
    }

    public void beforeEntityRemoved(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        // Do nothing
    }

    public void afterEntityRemoved(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        // Do nothing
    }

    protected void updatePeriod(ScoreDirector scoreDirector, LeadingExam leadingExam) {
        Period period = leadingExam.getPeriod();
        for (FollowingExam followingExam : leadingExam.getFollowingExamList()) {
            scoreDirector.beforeVariableChanged(followingExam, "period");
            followingExam.setPeriod(period);
            scoreDirector.afterVariableChanged(followingExam, "period");
        }
    }

}
