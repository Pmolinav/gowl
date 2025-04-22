package net.pmolinav.bookingslib.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingId")
    private Long bookingId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "activityName", nullable = false)
    private String activityName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startTime", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endTime", nullable = false)
    private Date endTime;

    @Column(name = "status", nullable = false)
    private String status = "OPEN";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modificationDate")
    private Date modificationDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Activity activity;

    public Booking(Long bookingId, Long userId, String activityName, Date startTime, Date endTime, String status, Date creationDate, Date modificationDate) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.activityName = activityName;
        this.startTime = startTime;
        this.endTime = endTime;
        if (status != null) {
            this.status = status;
        }
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId)
                && Objects.equals(userId, booking.userId)
                && Objects.equals(activityName, booking.activityName)
                && Objects.equals(startTime, booking.startTime)
                && Objects.equals(endTime, booking.endTime)
                && Objects.equals(status, booking.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, userId, activityName, startTime, endTime, status);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", activityName=" + activityName +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
