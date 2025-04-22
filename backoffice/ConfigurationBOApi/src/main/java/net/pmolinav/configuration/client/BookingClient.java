package net.pmolinav.configuration.client;

import net.pmolinav.bookingslib.dto.BookingDTO;
import net.pmolinav.bookingslib.dto.BookingUpdateDTO;
import net.pmolinav.bookingslib.model.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "BookingService", url = "bookingsservice:8001/bookings")
public interface BookingClient {

    @GetMapping("/")
    List<Booking> findAllBookings();

    @PostMapping("/")
    Long createBooking(@RequestBody BookingDTO bookingDTO);

    @GetMapping("/{id}")
    Booking findBookingById(@PathVariable long id);

    @PutMapping("/{id}")
    Booking updateBooking(@PathVariable long id, @RequestBody BookingUpdateDTO bookingDetails);

    @DeleteMapping("/{id}")
    void deleteBooking(@PathVariable long id);
}
