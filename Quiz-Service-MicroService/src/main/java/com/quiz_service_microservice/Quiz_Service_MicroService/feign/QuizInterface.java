package com.quiz_service_microservice.Quiz_Service_MicroService.feign;

import com.quiz_service_microservice.Quiz_Service_MicroService.model.QuestionWrapper;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FeignClient("QUESTION-SERVICE-MICROSERVICE")
public interface QuizInterface {

    // Now basically if you remember when we talk about quiz application it was getting questions.But this time, what if we don't return questions?
    // We just return the Id's. Anyway, all the calculations is going to happen on the question service, right?
    // So this getting score or getting the questions anyway it is happening in the question service. Then why Quiz has to know about the questions.We can simply return the list of number means the IDs of the questions.
    @GetMapping("question/generateQuestions")
    public ResponseEntity<List<Integer>> generateQuestionsFOrQuiz(
            @RequestParam String category, @RequestParam Integer numberOfQuestions);

    // If Quiz service is requesting for the questions for a particular ID we can do that with the help of get questions.
    @PostMapping("question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsFromId(@RequestBody List<Integer> questionIds);

    @PostMapping("question/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
