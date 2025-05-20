package com.pmolinav.leagueslib.dto;

import com.pmolinav.leagueslib.model.LeagueStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LeagueDTO {

    @NotBlank(message = "League name is required.")
    @Size(max = 100, message = "League name must not exceed 100 characters.")
    private String name;

    @Size(max = 256, message = "Description must not exceed 256 characters.")
    private String description;

    @NotBlank(message = "Category ID is required.")
    @Size(max = 50, message = "Category ID must not exceed 50 characters.")
    private String categoryId;

    private boolean isPublic;

    @Size(max = 100, message = "Password must not exceed 100 characters.")
    private String password;

    @NotNull(message = "League status is required.")
    private LeagueStatus status;

    @Min(value = 2, message = "Minimum number of players must be at least 2.")
    @Max(value = 1000, message = "Maximum number of players allowed is 1000.")
    private Integer maxPlayers;

    @Size(max = 256, message = "Logo URL must not exceed 256 characters.")
    private String logoUrl;

    private boolean isPremium;

    @NotBlank(message = "Owner username is required.")
    @Size(max = 100, message = "Owner username must not exceed 100 characters.")
    private String ownerUsername;

}
