package dbComponent.data.Comparators;

import dbComponent.data.Semester;
import dbComponent.enums.SemesterSeason;

import java.util.Comparator;

public class SemesterComparator implements Comparator<Semester> {

    // Method:
    @Override
    public int compare(Semester semesterA, Semester semesterB) {
        if (semesterA.getYear() == semesterB.getYear()) {
            if (semesterA.getSeason() == semesterB.getSeason()) {
                return 0;
            } else if (semesterA.getSeason().equals(SemesterSeason.SPRING)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return semesterB.getYear() - semesterA.getYear();
        }
    }
}
