package com.BTL_JAVA.BTL.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "invalidated_token")
public class InvalidtedToken {
    @Id
    String id;
    
    @Column(name = "expiry_time")
    Date expiryTime;
}
