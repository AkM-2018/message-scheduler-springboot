package com.akm.messagescheduler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.akm.messagescheduler.dto.MessageRequest;
import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.error.EmptyFieldException;
import com.akm.messagescheduler.error.InvalidDateException;
import com.akm.messagescheduler.error.InvalidReceiverNumberException;
import com.akm.messagescheduler.repository.MessageRepository;

import okhttp3.OkHttpClient;

@SpringBootTest
public class MessageServiceTest {

	@Autowired
	MessageService messageService;

	@MockBean
	MessageRepository messageRepository;

	@MockBean
	OkHttpClient client;

	private Message message;

	@BeforeEach
	void init() {
		message = Message.builder().message("message text").receiverNo("919876543210")
				.scheduledAt("2022-07-13T15:45:00.000").messageStatus("Pending").build();
	}

	@Test
	void testFindScheduledMessages() {
		List<Message> expectedMessageList = new ArrayList<>();
		expectedMessageList.add(message);

		when(messageRepository.findScheduledMessages()).thenReturn(expectedMessageList);

		List<Message> actualMessageList = messageService.findScheduledMessages();

		assertEquals(expectedMessageList.size(), actualMessageList.size());
		for (int i = 0; i < expectedMessageList.size(); i++) {
			assertEquals(expectedMessageList.get(i), actualMessageList.get(i));
		}
	}

	@Nested
	class testSaveMessage {

		@ParameterizedTest
		@CsvSource(value = { "null", "''" }, nullValues = { "null" })
		void testSaveMessage_messageIsNull(String messageText) {
			MessageRequest messageRequest = MessageRequest.builder().message(messageText)
					.receiverNo("919876543210").scheduledAt("2025-08-01T15:45:00.000").build();

			Throwable exception = assertThrows(EmptyFieldException.class,
					() -> messageService.saveMessage(messageRequest));
			assertEquals("Please provide a non-empty message", exception.getMessage());
		}

		@ParameterizedTest
		@CsvSource(value = { "null", "''" }, nullValues = { "null" })
		void testSaveMessage_receiverNoIsNull(String receiverNo) {
			MessageRequest messageRequest = MessageRequest.builder().message("message text")
					.receiverNo(receiverNo).scheduledAt("2025-08-01T15:45:00.000").build();

			Throwable exception = assertThrows(EmptyFieldException.class,
					() -> messageService.saveMessage(messageRequest));
			assertEquals("Please provide a receiver number", exception.getMessage());
		}

		@ParameterizedTest
		@CsvSource(value = { "null", "''" }, nullValues = { "null" })
		void testSaveMessage_scheduledAtIsNull(String scheduledAt) {
			MessageRequest messageRequest = MessageRequest.builder().message("message text")
					.receiverNo("919876543210").scheduledAt(scheduledAt).build();

			Throwable exception = assertThrows(EmptyFieldException.class,
					() -> messageService.saveMessage(messageRequest));
			assertEquals("Please provide a date in form uuuu-MM-dd'T'HH:mm:ss.SSS", exception.getMessage());
		}

		@Test
		void testSaveMessage_scheduledAtIsNotValid() {
			MessageRequest messageRequest = MessageRequest.builder().message("message text")
					.receiverNo("919876543210").scheduledAt("20625-08-01Tu15:45:00.000").build();

			Throwable exception = assertThrows(InvalidDateException.class,
					() -> messageService.saveMessage(messageRequest));
			assertEquals("Please provide a valid date in form uuuu-MM-dd'T'HH:mm:ss.SSS",
					exception.getMessage());
		}

		@ParameterizedTest
		@CsvSource(value = { "9822", "987726353552123", "alphabets" })
		void testSaveMessage_receiverNoIsNotValid(String receiverNo) {
			MessageRequest messageRequest = MessageRequest.builder().message("message text")
					.receiverNo(receiverNo).scheduledAt("2025-08-01T15:45:00.000").build();

			Throwable exception = assertThrows(InvalidReceiverNumberException.class,
					() -> messageService.saveMessage(messageRequest));
			assertEquals("Please provide a valid receiver number. Ex: 919876543210",
					exception.getMessage());
		}

		@Test
		void testSaveMessage_messageRequestIsSaved()
				throws EmptyFieldException, InvalidDateException, InvalidReceiverNumberException {
			MessageRequest messageRequest = MessageRequest.builder().message("message text")
					.receiverNo("919876543210").scheduledAt("2025-08-01T15:45:00.000").build();

			Message expectedMessage = Message.builder().messageId(100L).message("message text")
					.receiverNo("919876543210").scheduledAt("2025-08-01T15:45:00.000")
					.messageStatus("Pending").build();

			when(messageRepository.save(any())).thenReturn(expectedMessage);

			Message actualMessage = messageService.saveMessage(messageRequest);

			assertEquals(expectedMessage, actualMessage);
		}
	}

	@Nested
	class testUpdateMessageById {

		@Test
		void testUpdateMessageById_withNoChanges() {
			Message emptyMessage = Message.builder().message(null).receiverNo(null).scheduledAt(null)
					.messageStatus(null).build();

			when(messageRepository.save(any())).thenReturn(message);
			when(messageRepository.findById(any())).thenReturn(Optional.of(message));

			Message actualMessage = messageService.updateMessageById(1L, emptyMessage);

			assertEquals(message, actualMessage);
		}

		@Test
		void testUpdateMessageById_withBlankChanges() {
			Message emptyMessage = Message.builder().message("").receiverNo("").scheduledAt("")
					.messageStatus("").build();

			when(messageRepository.save(any())).thenReturn(message);
			when(messageRepository.findById(any())).thenReturn(Optional.of(message));

			Message actualMessage = messageService.updateMessageById(1L, emptyMessage);

			assertEquals(message, actualMessage);
		}

		@Test
		void testUpdateMessageById_withActualChanges() {
			Message expectedMessage = Message.builder().messageId(1L).message("new message")
					.receiverNo("919812345670").scheduledAt("2025-08-01T15:45:00.000")
					.messageStatus("Failure").build();

			when(messageRepository.save(any())).thenReturn(expectedMessage);
			when(messageRepository.findById(any())).thenReturn(Optional.of(message));

			Message actualMessage = messageService.updateMessageById(1L, expectedMessage);

			assertEquals(expectedMessage, actualMessage);
		}
	}
}
