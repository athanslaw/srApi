package com.edunge.srtool;

import com.edunge.srtool.service.impl.CallServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@ActiveProfiles("local")
class SrToolApplicationTests {
//
//	@Autowired
//	CallServiceImpl callService;
	@Test
	void contextLoads() {
	}
/*
	@Test
	void callTwilio(){
		try {
			callService.makeCall("+2347068551023");
		}catch (URISyntaxException e){
			System.out.println("Here");
		}
		assertNotNull("requeryAccount");
	}*/

}
