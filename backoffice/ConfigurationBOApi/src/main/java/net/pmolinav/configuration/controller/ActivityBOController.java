package net.pmolinav.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.exception.CustomStatusException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.model.Activity;
import net.pmolinav.configuration.service.ActivityBOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("activities")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "4. Activity", description = "The Activity Controller. Contains all the operations that can be performed on an activity.")
public class ActivityBOController {

    @Autowired
    private ActivityBOService activityBOService;

    @GetMapping
    @Operation(summary = "Retrieve all activities", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<Activity>> findAllActivities(@RequestParam String requestUid) {
        List<Activity> activities = activityBOService.findAllActivities();
        return ResponseEntity.ok(activities);
    }

    @PostMapping
    @Operation(summary = "Create a new activity", description = "Bearer token is required to authorize users.")
    public ResponseEntity<String> createActivity(@RequestParam String requestUid, @Valid @RequestBody ActivityDTO activityDTO) {
        try {
            String createdActivityId = activityBOService.createActivity(activityDTO);
            return new ResponseEntity<>(createdActivityId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("{name}")
    @Operation(summary = "Get a specific activity by name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<Activity> getActivityByName(@RequestParam String requestUid, @PathVariable String name) {
        try {
            Activity activity = activityBOService.findActivityByName(name);
            return ResponseEntity.ok(activity);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

// TODO: Complete
//    @PutMapping("{id}")
//    @Operation(summary = "Update a specific activity", description = "Bearer token is required to authorize users.")
//    public ResponseEntity<Activity> updateActivity(@RequestParam String requestUid, @PathVariable long id, @RequestBody ActivityDTO activityDetails) {
//        String message = validateMandatoryFieldsInRequest(activityDetails);
//        try {
//            Activity updatedActivity = activityService.findById(id);
//
//            if (!StringUtils.hasText(message)) {
//                updatedActivity.setName(activityDetails.getName());
//                updatedActivity.setDescription(activityDetails.getDescription());
//                updatedActivity.setPrice(activityDetails.getPrice());
//                if (activityDetails.getType() != null) {
//                    updatedActivity.setType(activityDetails.getType().name());
//                }
//                activityService.createActivity(updatedActivity);
//                return ResponseEntity.ok(updatedActivity);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (UnexpectedException e) {
//            return ResponseEntity.status(e.getStatusCode()).build();
//        }
//    }

    @DeleteMapping("{name}")
    @Operation(summary = "Delete an activity by name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteActivity(@RequestParam String requestUid, @PathVariable String name) {
        try {
            activityBOService.deleteActivity(name);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

//    private String validateMandatoryFieldsInRequest(ActivityDTO activityDTO) {
//        StringBuilder messageBuilder = new StringBuilder();
//        if (activityDTO == null) {
//            messageBuilder.append("Body is mandatory.");
//        } else if (activityDTO.getType() == null) {
//            messageBuilder.append("Activity type is mandatory.");
//        } else if (!StringUtils.hasText(activityDTO.getName())) {
//            messageBuilder.append("Activity name is mandatory.");
//        } else if (activityDTO.getPrice() == null || activityDTO.getPrice().equals(BigDecimal.ZERO)) {
//            messageBuilder.append("Activity price is mandatory and must be greater than zero.");
//        }
//        return messageBuilder.toString();
//    }
}
