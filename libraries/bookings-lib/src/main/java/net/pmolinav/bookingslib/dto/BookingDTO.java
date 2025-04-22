package net.pmolinav.bookingslib.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingDTO {
    @Positive(message = "User ID must be positive.")
    @Min(value = 1, message = "User ID cannot be less than 1")
    private long userId;

    @NotBlank(message = "Activity name is mandatory.")
    private String activityName;

    @NotNull(message = "Start time is mandatory.")
    @Future(message = "Start time must be in the future.")
    private Date startTime;

    @NotNull(message = "End time is mandatory.")
    @Future(message = "End time must be in the future.")
    private Date endTime;

    @NotNull(message = "Booking status is mandatory.")
    private BookingStatus status;

}
