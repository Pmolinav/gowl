package net.pmolinav.bookingslib.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ActivityDTO {

    @NotBlank(message = "Activity name is mandatory.")
    private String activityName;

    private String description;

    @Min(value = 1, message = "Activity price is mandatory and must be an integer greater than zero.")
    private Integer price;

}
