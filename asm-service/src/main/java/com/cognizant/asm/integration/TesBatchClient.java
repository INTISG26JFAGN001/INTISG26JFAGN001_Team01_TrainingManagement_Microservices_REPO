package com.cognizant.asm.integration;

import com.cognizant.asm.dto.response.external.BatchResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign client for Training Execution Service (TES) to validate Batch existence.
@FeignClient(contextId = "tesBatchClient", name = "tes-service", path = "/batches")
public interface TesBatchClient {

    @GetMapping("/{id}")
    BatchResponse getBatchById(@PathVariable("id") Long id);
}