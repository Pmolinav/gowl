package net.pmolinav.bookingslib.model;

import lombok.*;
import net.pmolinav.bookingslib.dto.ChangeType;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @Column(name = "changeType", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChangeType changeType;

    @Column(name = "entity", nullable = false)
    private String entity;

    @Column(name = "entityId", nullable = false)
    private String entityId;

    @Column(name = "changeDetails")
    private String changeDetails;

    @Column(name = "createUserId", nullable = false)
    private String createUserId;

    public History(Date creationDate, ChangeType changeType, String entity, String entityId, String changeDetails, String createUserId) {
        this.creationDate = creationDate;
        this.changeType = changeType;
        this.entity = entity;
        this.entityId = entityId;
        this.changeDetails = changeDetails;
        this.createUserId = createUserId;
    }
}
