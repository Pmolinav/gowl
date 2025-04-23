package com.pmolinav.bookings.controller;

import com.pmolinav.bookings.service.ActivityService;
import com.pmolinav.userslib.dto.ActivityDTO;
import com.pmolinav.userslib.dto.ChangeType;
import com.pmolinav.userslib.exception.InternalServerErrorException;
import com.pmolinav.userslib.exception.NotFoundException;
import com.pmolinav.userslib.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<Activity>> findAllActivities() {
        try {
            List<Activity> activities = activityService.findAllActivities();
            return ResponseEntity.ok(activities);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createActivity(@RequestBody ActivityDTO activityDTO) {
        try {
            Activity createdActivity = activityService.createActivity(activityDTO);

            activityService.storeInKafka(ChangeType.CREATE, createdActivity.getActivityName(), createdActivity);

            return new ResponseEntity<>(createdActivity.getActivityName(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("{name}")
    public ResponseEntity<Activity> findActivityByName(@PathVariable String name) {
        try {
            Activity activity = activityService.findByName(name);
            return ResponseEntity.ok(activity);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
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
    public ResponseEntity<?> deleteActivity(@PathVariable String name) {
        try {
            activityService.deleteActivity(name);

            activityService.storeInKafka(ChangeType.DELETE, name, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
