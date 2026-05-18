package com.cognizant.asm_service;

import com.cognizant.asm.AsmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AsmApplication.class)
@ActiveProfiles("test")
class AsmApplicationTests {

	@Test
	void contextLoads() {
	}

}
