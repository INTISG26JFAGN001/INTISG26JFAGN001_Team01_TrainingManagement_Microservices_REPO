package com.cognizant.pes.client;


import com.cognizant.pes.dto.response.RubricResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="asm-service")
public interface AsmRubricClient {

    @GetMapping("/assessments/interview/{interviewId}/rubrics")
    List<RubricResponseDTO> getRubricsForInterview(@PathVariable("interviewId") Long interviewId);
}
