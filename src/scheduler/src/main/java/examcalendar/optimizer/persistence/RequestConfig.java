package examcalendar.optimizer.persistence;

import examcalendar.optimizer.domain.InstitutionParametrization;

import java.util.Date;

/**
 * Created by Gustavo on 02/07/2016.
 */
public class RequestConfig {
    public InstitutionParametrization institutionParametrization;
    public int calendar;
    public int normalSeasonDuration = 21;
    public int appealSeasonDuration = 14;
    public Date startingDate;
    public int timeout = 1000;
}
