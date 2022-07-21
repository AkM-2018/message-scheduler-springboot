package com.akm.messagescheduler.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akm.messagescheduler.dto.MessageRequest;
import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.error.EmptyFieldException;
import com.akm.messagescheduler.error.InvalidDateException;
import com.akm.messagescheduler.error.InvalidReceiverNumberException;
import com.akm.messagescheduler.repository.MessageRepository;
import com.akm.messagescheduler.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private static final String BLANK_STRING = "";

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message saveMessage(MessageRequest messageRequest)
            throws EmptyFieldException, InvalidDateException, InvalidReceiverNumberException {
        isValidMessageRequest(messageRequest);
        isValidDate(messageRequest.getScheduledAt());
        isValidReceiverNumber(messageRequest.getReceiverNo());

        Message message = Message.builder().message(messageRequest.getMessage())
                .receiverNo(messageRequest.getReceiverNo()).scheduledAt(messageRequest.getScheduledAt())
                .messageStatus("Pending").build();
        return messageRepository.save(message);
    }

    private void isValidMessageRequest(MessageRequest messageRequest) throws EmptyFieldException {
        if (Objects.isNull(messageRequest.getMessage()) || BLANK_STRING.equalsIgnoreCase(messageRequest.getMessage())) {
            throw new EmptyFieldException("Please provide a non-empty message");
        }

        if (Objects.isNull(messageRequest.getScheduledAt())
                || BLANK_STRING.equalsIgnoreCase(messageRequest.getScheduledAt())) {
            throw new EmptyFieldException("Please provide a date in form uuuu-MM-dd'T'HH:mm:ss.SSS");
        }

        if (Objects.isNull(messageRequest.getReceiverNo())
                || BLANK_STRING.equalsIgnoreCase(messageRequest.getReceiverNo())) {
            throw new EmptyFieldException("Please provide a receiver number");
        }
    }

    private void isValidReceiverNumber(String receiverNo) throws InvalidReceiverNumberException {
        if (!receiverNo.matches("[0-9]+") || receiverNo.length() < 8 || receiverNo.length() > 13) {
            throw new InvalidReceiverNumberException("Please provide a valid receiver number. Ex: 919876543210");
        }
    }

    private void isValidDate(String scheduledAt) throws InvalidDateException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime.parse(scheduledAt, formatter);
        } catch (Exception ex) {
            throw new InvalidDateException("Please provide a valid date in form uuuu-MM-dd'T'HH:mm:ss.SSS");
        }
    }

    @Override
    public List<Message> findScheduledMessages() {
        return messageRepository.findScheduledMessages();
    }

    @Override
    public Message updateMessageById(Long messageId, Message message) {
        Message messageToUpdate = messageRepository.findById(messageId).get();

        if (Objects.nonNull(message.getMessage()) && !BLANK_STRING.equalsIgnoreCase(message.getMessage())) {
            messageToUpdate.setMessage(message.getMessage());
        }

        if (Objects.nonNull(message.getScheduledAt()) && !BLANK_STRING.equalsIgnoreCase(message.getScheduledAt())) {
            messageToUpdate.setScheduledAt(message.getScheduledAt());
        }

        if (Objects.nonNull(message.getReceiverNo()) && !BLANK_STRING.equalsIgnoreCase(message.getReceiverNo())) {
            messageToUpdate.setReceiverNo(message.getReceiverNo());
        }

        if (Objects.nonNull(message.getMessageStatus()) && !BLANK_STRING.equalsIgnoreCase(message.getMessageStatus())) {
            messageToUpdate.setMessageStatus(message.getMessageStatus());
        }

        return messageRepository.save(messageToUpdate);
    }

}
