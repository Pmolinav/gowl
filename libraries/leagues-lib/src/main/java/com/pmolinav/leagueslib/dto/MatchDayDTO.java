package com.pmolinav.leagueslib.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MatchDayDTO {

    @NotBlank(message = "Category ID cannot be blank")
    @Size(max = 50, message = "Category ID must be at most 50 characters long")
    private String categoryId;

    @NotNull(message = "Season cannot be null")
    private Integer season;

    @NotNull(message = "Match day number cannot be null")
    private Integer matchDayNumber;

    @NotNull(message = "Start date cannot be null")
    @Min(value = 0, message = "Start date must be a positive timestamp (milliseconds since epoch)")
    private Long startDate;

    @NotNull(message = "End date cannot be null")
    @Min(value = 0, message = "End date must be a positive timestamp (milliseconds since epoch)")
    private Long endDate;

    @JsonIgnore
    @AssertTrue(message = "Start date must be earlier than end date")
    public boolean isStartDateBeforeEndDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return startDate < endDate;
    }

}
