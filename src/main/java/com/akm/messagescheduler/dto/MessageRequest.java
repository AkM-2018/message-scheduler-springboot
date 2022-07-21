package com.akm.messagescheduler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @Schema(example = "Some random message text")
    private String message;
    @Schema(example = "2022-07-13T15:45:00.000")
    private String scheduledAt;
    @Schema(example = "919876543210")
    private String receiverNo;
}
