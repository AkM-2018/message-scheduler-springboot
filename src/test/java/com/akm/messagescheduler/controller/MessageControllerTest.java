package com.akm.messagescheduler.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.service.MessageService;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessageService messageService;

    @Value("${auth.token}")
    private String authToken;

    private Message message;

    @BeforeEach
    void init() {
        message = Message.builder().message("Hello!").receiverNo("919876543210").scheduledAt("2023-07-12T12:45:00.000")
                .messageStatus("Pending").build();
    }

    @Test
    void testFindScheduledMessages() throws Exception {
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);

        when(messageService.findScheduledMessages()).thenReturn(messageList);

        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/message"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].message").value(message.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].receiverNo").value(message.getReceiverNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].scheduledAt").value(message.getScheduledAt()));
    }

    @Test
    void testSaveMessage() throws Exception {
        when(messageService.saveMessage(any())).thenReturn(message);

        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/message").header("token", authToken)
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{\n    \"message\" : \"Hello!\",\n    \"scheduledAt\" : \"2023-07-12T12:45:00.000\",\n    \"receiverNo\" : \"919876543210\"\n}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.getMessage()));
    }

    @Test
    void testTestRoute() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/test")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Everything seems to be working fine!"));
    }
}
