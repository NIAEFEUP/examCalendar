package examcalendar.optimizer.persistence;

import examcalendar.optimizer.domain.InstitutionParametrization;

import java.util.Date;

/**
 * Created by Gustavo on 02/07/2016.
 */
public class RequestConfig {
    InstitutionParametrization institutionParametrization;
    int calendar;
    int normalSeasonDuration = 21;
    int appealSeasonDuration = 14;
    Date startingDate;
    int timeout = 1000;
}
