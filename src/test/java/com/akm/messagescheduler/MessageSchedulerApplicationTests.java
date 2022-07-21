package com.akm.messagescheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import okhttp3.OkHttpClient;

@SpringBootTest
class MessageSchedulerApplicationTests {

	@MockBean
	OkHttpClient client;

	@Test
	void contextLoads() {
	}

}
