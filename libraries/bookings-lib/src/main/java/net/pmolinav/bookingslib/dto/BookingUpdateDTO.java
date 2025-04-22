package net.pmolinav.bookingslib.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingUpdateDTO {
    private Date startTime;
    private Date endTime;
    private BookingStatus status;

}
