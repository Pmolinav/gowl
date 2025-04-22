package net.pmolinav.bookings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookings.producer.MessageProducer;
import net.pmolinav.bookings.repository.BookingRepository;
import net.pmolinav.bookingslib.dto.BookingDTO;
import net.pmolinav.bookingslib.dto.ChangeType;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.mapper.BookingMapper;
import net.pmolinav.bookingslib.model.Booking;
import net.pmolinav.bookingslib.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@EnableAsync
@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper, MessageProducer messageProducer) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<Booking> findAllBookings() {
        List<Booking> bookingsList;
        try {
            bookingsList = bookingRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all bookings in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(bookingsList)) {
            logger.warn("No bookings were found in repository.");
            throw new NotFoundException("No bookings found in repository.");
        } else {
            return bookingsList;
        }
    }

    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {
        try {
            Booking booking = bookingMapper.bookingDTOToBookingEntity(bookingDTO);
            return bookingRepository.save(booking);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new booking in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Booking findById(long id) {
        try {
            return bookingRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Booking with id %s does not exist.", id)));
        } catch (NotFoundException e) {
            logger.error("Booking with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching booking with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteBooking(Long id) {
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Booking with id %s does not exist.", id)));

            bookingRepository.delete(booking);
        } catch (NotFoundException e) {
            logger.error("Booking with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing booking with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Async
    public void storeInKafka(ChangeType changeType, Long bookingId, Booking booking) {
        try {
            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
                    new Date(),
                    changeType,
                    "Booking",
                    String.valueOf(bookingId),
                    booking == null ? null : new ObjectMapper().writeValueAsString(booking), // TODO: USE JSON PATCH.
                    "Admin" // TODO: createUser is not implemented yet.
            ));
        } catch (Exception e) {
            logger.warn("Kafka operation {} with name {} and booking {} need to be reviewed", changeType, bookingId, booking);
        }
    }
}
