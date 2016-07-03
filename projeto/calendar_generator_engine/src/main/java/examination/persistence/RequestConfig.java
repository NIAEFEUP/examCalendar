package examination.persistence;

import examination.domain.InstitutionParametrization;

import java.util.Date;

/**
 * Created by Gustavo on 02/07/2016.
 */
public class RequestConfig {
    InstitutionParametrization institutionParametrization;
    int creator;
    int normalSeasonDuration = 21;
    int appealSeasonDuration = 14;
    Date startingDate;
    int timeout = 1000;
}
