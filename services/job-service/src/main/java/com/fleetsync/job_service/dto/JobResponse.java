package com.fleetsync.job_service.dto;

import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String pickupAddress;
    private String deliveryAddress;
    private String jobStatus;
    private Long dispatcherId;
    private Long  assignedDriverId;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;

}

