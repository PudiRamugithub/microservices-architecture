package com.quiz_service_microservice.Quiz_Service_MicroService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {

    private String title;
    private String category;
    private Integer numOfQuesns;
}
