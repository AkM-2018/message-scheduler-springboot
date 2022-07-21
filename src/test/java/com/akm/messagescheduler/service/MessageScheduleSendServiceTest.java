package com.akm.messagescheduler.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.repository.MessageRepository;
import com.akm.messagescheduler.service.impl.MessageScheduleSendServiceImpl;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

@SpringBootTest
public class MessageScheduleSendServiceTest {

    @Autowired
    MessageScheduleSendServiceImpl messageScheduleSendServiceImpl;

    @MockBean
    MessageRepository messageRepository;

    @MockBean
    MessageService messageService;

    @MockBean
    OkHttpClient client;

    @Autowired
    Gson gson;

    private Message message;

    @BeforeEach
    void init() {
        message = Message.builder().messageId(1000L).message("message text")
                .receiverNo("919876543210").scheduledAt("2022-07-13T15:45:00.000")
                .messageStatus("Pending").build();
    }

    @ParameterizedTest
    @CsvSource(value = { "true", "false" })
    void testSendScheduledMessage(boolean isSuccessful) throws IOException {
        Call call = mock(Call.class);
        Response response = mock(Response.class);

        doReturn(call).when(client).newCall(any());
        doReturn(response).when(call).execute();
        doReturn(isSuccessful).when(response).isSuccessful();

        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        when(messageRepository.findPendingMessages()).thenReturn(messageList);

        String messageStatus = (isSuccessful) ? "Success" : "Failed";
        Message updatedMessage = Message.builder().messageStatus(messageStatus).build();

        messageScheduleSendServiceImpl.sendScheduledMessage();

        verify(messageService).updateMessageById(1000L, updatedMessage);
    }
}
