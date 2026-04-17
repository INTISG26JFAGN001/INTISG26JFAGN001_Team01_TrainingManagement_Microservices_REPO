package com.cognizant.asm.integration;

import com.cognizant.asm.dto.response.external.TrainerResponse;
import com.cognizant.asm.dto.response.external.AssociateResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign client for Training Execution Service (TES) to validate Trainer Associate.
@FeignClient(contextId = "tesUserClient", name = "tes-service")
public interface TesUserClient {

    @GetMapping("/trainer/{trainerId}")
    TrainerResponse getTrainerById(@PathVariable("trainerId") Long trainerId);

    @GetMapping("/associates/{userId}")
    AssociateResponse getAssociateByUserId(@PathVariable("userId") Long userId);
}