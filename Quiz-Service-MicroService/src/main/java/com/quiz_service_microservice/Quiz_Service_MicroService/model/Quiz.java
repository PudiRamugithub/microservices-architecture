package com.quiz_service_microservice.Quiz_Service_MicroService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    // Since one quiz can have multiple questions
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> questionIds;
}
