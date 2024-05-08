package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.log4j.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {
	static Logger log = Logger.getLogger(SareetaApplicationTests.class);
	@Test
	public void contextLoads() {
	}
	@Test
	public void writeLogs()
	{

		log.trace("the built-in TRACE level");
		log.debug("the built-in DEBUG level");
		log.info("the built-in INFO level");
		log.warn("the built-in WARN level");
		log.error("the built-in ERROR level");
		log.fatal("the built-in FATAL level");
	}
}