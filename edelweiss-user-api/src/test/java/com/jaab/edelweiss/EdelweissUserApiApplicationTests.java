package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.UserController;
import com.jaab.edelweiss.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EdelweissUserApiApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(userController);
		Assertions.assertNotNull(userService);
	}

}
