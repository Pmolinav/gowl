package com.pmolinav.leagueslib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LeagueCategoryDTO {

    @NotBlank
    @Size(max = 50)
    private String categoryId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 256)
    private String description;

    @Size(max = 100)
    private String sport;

    @NotBlank
    @Size(min = 2, max = 2)
    private String country;

    @Size(max = 256)
    private String iconUrl;

    private boolean isActive = true;

}
