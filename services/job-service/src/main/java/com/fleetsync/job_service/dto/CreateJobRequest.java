package com.fleetsync.job_service.dto;


import jakarta.validation.constraints.NotNull;

public class CreateJobRequest {

    private String title;
    private String description;

    @NotNull
    private String pickupAddress;

    @NotNull
    private String deliveryAddress;
}
