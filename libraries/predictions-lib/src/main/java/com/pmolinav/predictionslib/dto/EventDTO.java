package com.pmolinav.predictionslib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class EventDTO {

    @NotNull(message = "Match ID must not be null.")
    private Long matchId;

    @NotBlank(message = "Event name is mandatory.")
    @Size(max = 100, message = "Event name must be at most 100 characters.")
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters.")
    private String description;

}
