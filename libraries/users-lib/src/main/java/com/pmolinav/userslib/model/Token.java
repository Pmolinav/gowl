package com.pmolinav.userslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 512)
    private String refreshToken;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "creation_date", nullable = false)
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public Token(String username, String refreshToken, String deviceInfo, String ipAddress, Long creationDate, Long modificationDate, LocalDateTime expiresAt) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id)
                && Objects.equals(username, token.username)
                && Objects.equals(refreshToken, token.refreshToken)
                && Objects.equals(deviceInfo, token.deviceInfo)
                && Objects.equals(ipAddress, token.ipAddress)
                && Objects.equals(expiresAt, token.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, refreshToken, deviceInfo, ipAddress, expiresAt);
    }
}
