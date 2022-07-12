package com.capstone15.alterra.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class BaseResponseFollower implements Serializable {

    private static final long serialVersionUID = 2194554937198278207L;

    @Column(name = "follow_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime followTime;

    @PrePersist
    void onCreate() {
        this.followTime = LocalDateTime.now();
    }


}
