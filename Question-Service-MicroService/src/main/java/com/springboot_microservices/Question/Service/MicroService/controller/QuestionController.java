package com.springboot_microservices.Question.Service.MicroService.controller;

import com.springboot_microservices.Question.Service.MicroService.model.Question;
import com.springboot_microservices.Question.Service.MicroService.model.QuestionWrapper;
import com.springboot_microservices.Question.Service.MicroService.model.Response;
import com.springboot_microservices.Question.Service.MicroService.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    public QuestionService questionService;

    @Autowired
    Environment environment;

//    @GetMapping("/allQuestions")
//    public ResponseEntity<List<Question>> getAllQuestions() {
//        return questionService.getAllQuestions();
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Question>> getAllQuestionsByCategory(@PathVariable String category) {
//        return questionService.getQuestionsByCategory(category);
//    }
//
//    @GetMapping("/getQuestion/{id}")
//    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
//        return questionService.getQuestionById(id);
//    }

    @GetMapping("/allQuestions")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestions() {
        return questionService.getAllQuestionsV2();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategoryV2(category);
    }

    @GetMapping("/getQuestion/{id}")
    public ResponseEntity<QuestionWrapper> getQuestionById(@PathVariable int id) {
        return questionService.getQuestionByIdV2(id);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @PutMapping("/updateQuestion/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable int id, @RequestBody Question updatedQuestion) {
        return questionService.updateQuestion(id, updatedQuestion);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable int id) {
        return questionService.deleteQuestionById(id);
    }

    // in the earlier scenario the quiz was responsible to generate questions, right? But then it also needs access to the question database.
    // We don't want that now.The quiz service will not be having access to the question database, which is in two different servers.
    // So if Question service needs a quiz, it is the job of your question service to generate it. So basically quiz service will request to the question service.
    // Hey give me questions.The question service will say okay let me give you the questions and it will generate the questions.So basically I need to have a method here called generate or create your choice.
//      generate()
    // Next we need to also get the question for a particular quiz. Let's say we have a quiz database, right? A quiz service has quiz database.
    // In the quiz database it will have its own data about the quiz, but it will not have data about the questions. Right.For the questions. It has to go to the question service.
    // So what if your quiz service says, hey, you know, I have the quiz number, but I don't have a questions for it. Give me the questions.
    // So maybe we have to find a way to give them the questions based on the question ID because we don't have the quiz ID right on the question database
//      getQuestions(questionId)
    // And also now we need to get the score as well. You know why we have to calculate this score here in the earlier project. We basically calculated this score in the quiz service.
    // Now we don't want to do that in the quiz service because in the question service we have all the questions.Even the right answers is there with the questions.So in that case we need to do that here.
//      calculateScore()

    @GetMapping("generateQuestions")
    public ResponseEntity<List<Integer>> generateQuestionsFOrQuiz(
            @RequestParam String category, @RequestParam Integer numberOfQuestions)
    {
        // Now basically if you remember when we talk about quiz application it was getting questions.But this time, what if we don't return questions?
        // We just return the Id's. Anyway, all the calculations is going to happen on the question service, right?
        // So this getting score or getting the questions anyway it is happening in the question service. Then why. Quiz has to know about the questions.We can simply return the list of numbers, the IDs of the questions.
        return questionService.getQuestionsForQuiz(category, numberOfQuestions);
    }

    // If Quiz service is requesting for the questions for a particular ID we can do that with the help of get questions.
    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsFromId(@RequestBody List<Integer> questionIds) {
        return questionService.getQuestionsFromId(questionIds);
    }


    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        System.out.println(environment.getProperty("local.server.port"));
        // In fact it only needs a list of responses. So from the quiz service you will get a list of responses.
        return questionService.getScore(responses);
    }

}
