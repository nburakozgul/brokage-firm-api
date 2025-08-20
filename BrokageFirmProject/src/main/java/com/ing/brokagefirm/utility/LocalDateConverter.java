package com.ing.brokagefirm.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateConverter {
    public static LocalDateTime convertStartDate(LocalDate startDate){
        return (startDate != null) ? startDate.atStartOfDay() : null;
    }

    public static LocalDateTime convertEndDate(LocalDate endDate){
        return (endDate != null) ? endDate.atTime(23, 59, 59) : null;
    }
}
