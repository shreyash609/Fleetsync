package com.fleetsync.job_service.entity;

import com.fleetsync.job_service.enums.JobStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="jobs")
public class Job {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private string description;

    @NotNull
    private String pickupAddress;

    @NotNull
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

//    private Long dispatcherId;//need to see it later how we can do this

    private Long  assignedDriverId;

    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime updatedAt;

}
