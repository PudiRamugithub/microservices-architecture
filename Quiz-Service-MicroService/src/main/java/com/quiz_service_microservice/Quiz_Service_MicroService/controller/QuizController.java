package com.quiz_service_microservice.Quiz_Service_MicroService.controller;


import com.quiz_service_microservice.Quiz_Service_MicroService.model.QuestionWrapper;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.Quiz;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.QuizDto;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.Response;
import com.quiz_service_microservice.Quiz_Service_MicroService.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto) {
        return quizService.createQuiz(quizDto.getCategory(), quizDto.getNumOfQuesns(), quizDto.getTitle());
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable int id) {
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable int id, @RequestBody List<Response> response) {
        return quizService.submitQuiz(id, response);
    }
}
