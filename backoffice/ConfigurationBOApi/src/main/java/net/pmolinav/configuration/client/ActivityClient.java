package net.pmolinav.configuration.client;

import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.model.Activity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ActivityService", url = "bookingsservice:8001/activities")
public interface ActivityClient {

    @GetMapping("/")
    List<Activity> findAllActivities();

    @PostMapping("/")
    String createActivity(@RequestBody ActivityDTO activityDTO);

    @GetMapping("/{name}")
    Activity findActivityByName(@PathVariable String name);

//    @PutMapping("/{id}")
//    Activity updateActivity(@PathVariable long id, @RequestBody ActivityUpdateDTO activityDetails);

    @DeleteMapping("/{name}")
    void deleteActivity(@PathVariable String name);
}
