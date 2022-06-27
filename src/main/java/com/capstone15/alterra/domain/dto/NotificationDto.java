package com.capstone15.alterra.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationDto implements Serializable {

    private static final long serialVersionUID = -841934041447250707L;

    private Long id;

    private Long userId;

    private String title;

    private String message;

    private String createdAt;

    private Boolean isRead;
}
