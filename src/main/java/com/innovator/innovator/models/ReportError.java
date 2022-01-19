package com.innovator.innovator.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String customEmail;

    @Column(length = 10000)
    private String messageText;
    private String status;

    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime closedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {
            "reportErrorList",
            "recommendationList",
            "recommendationNewsList"
    })
    private User user;
}
