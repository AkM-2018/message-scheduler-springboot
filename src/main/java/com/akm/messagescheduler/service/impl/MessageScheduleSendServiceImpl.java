package com.akm.messagescheduler.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.repository.MessageRepository;
import com.akm.messagescheduler.service.MessageScheduleSendService;
import com.akm.messagescheduler.service.MessageService;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class MessageScheduleSendServiceImpl implements MessageScheduleSendService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    OkHttpClient client;

    @Autowired
    Gson gson;

    @Value("${api.endpoint}")
    private String GupshupApiEndpoint;

    @Value("${api.source}")
    private String apiSource;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.source.name}")
    private String apiSourceName;

    @Scheduled(fixedRate = 10000L)
    public void sendScheduledMessage() {
        List<Message> messageList = messageRepository.findPendingMessages();
        System.out.println("Called sendScheduleMessage with: " + messageList.size() + " messages");
        for (Message message : messageList) {
            scheduleSend(message);
        }
    }

    @Async
    private void scheduleSend(Message message) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "text");
        messageMap.put("text", message.getMessage());

        RequestBody body = new FormBody.Builder()
                .add("message", gson.toJson(messageMap))
                .add("channel", "whatsapp")
                .add("source", apiSource)
                .add("destination", message.getReceiverNo())
                .add("src.name", apiSourceName)
                .build();

        Request request = new Request.Builder()
                .url(GupshupApiEndpoint)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("apikey", apiKey)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String messageStatus = (response.isSuccessful()) ? "Success" : "Failed";
            Message updatedMessage = Message.builder().messageStatus(messageStatus).build();
            messageService.updateMessageById(message.getMessageId(), updatedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
