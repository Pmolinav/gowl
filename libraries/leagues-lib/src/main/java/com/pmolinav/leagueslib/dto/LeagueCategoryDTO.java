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

    @NotBlank(message = "Category ID cannot be blank")
    @Size(max = 50, message = "Category ID must be at most 50 characters long")
    private String categoryId;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters long")
    private String name;

    @Size(max = 256, message = "Description must be at most 256 characters long")
    private String description;

    @NotBlank(message = "Sport cannot be blank")
    @Size(max = 100, message = "Sport must be at most 100 characters long")
    private String sport;

    @NotBlank(message = "Country code cannot be blank")
    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters long (ISO 3166-1 alpha-2)")
    private String country;

    @Size(max = 256, message = "Icon URL must be at most 256 characters long")
    private String iconUrl;

    private boolean isActive = true;

}
