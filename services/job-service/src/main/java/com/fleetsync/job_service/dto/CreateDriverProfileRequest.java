package com.fleetsync.job_service.dto;

import jakarta.validation.constraints.NotNull;

public class CreateDriverProfileRequest {

    @NotNull
     private String name;

    @NotNull
    private String phone;

    @NotNull
    private String vehicleType;
}
