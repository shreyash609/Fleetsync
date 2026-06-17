package com.fleetsync.job_service.entity;

import com.fleetsync.job_service.enums.AvailabilityStatus;
import com.fleetsync.job_service.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class DriverProfile {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="id")
    private  User user;

    @NotNull
    private String name;

    @NotNull
    @Column(unique =true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AvailabilityStatus availabilityStatus;

    private Integer totalJobsCompleted = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
