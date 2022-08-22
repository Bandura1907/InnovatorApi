package com.innovator.innovator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ReportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String customEmail;

    @Column(length = 10000000)
    private String messageText;
    private String status;

    private Instant createDate;
    private Instant closedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {
            "reportErrorList",
            "recommendationList",
            "recommendationNewsList"
    })
    private User user;

    public ReportError() {
        this.createDate = Instant.now();
    }
}
