package com.akm.messagescheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akm.messagescheduler.annotation.ValidUser;
import com.akm.messagescheduler.dto.MessageRequest;
import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.error.EmptyFieldException;
import com.akm.messagescheduler.error.InvalidDateException;
import com.akm.messagescheduler.error.InvalidReceiverNumberException;
import com.akm.messagescheduler.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/schedule")
@Tag(name = "Message API")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Operation(summary = "Test route")
    @GetMapping("/test")
    public String testRoute() {
        return "Everything seems to be working fine!";
    }

    @Operation(summary = "Schedule a message for later")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message scheduled", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/message")
    @ValidUser
    public Message saveMessage(@RequestBody MessageRequest messageRequest)
            throws EmptyFieldException, InvalidDateException, InvalidReceiverNumberException {
        return messageService.saveMessage(messageRequest);
    }

    @Operation(summary = "Get all scheduled messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all pending messages", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/message")
    public List<Message> findScheduledMessages() {
        return messageService.findScheduledMessages();
    }

}
