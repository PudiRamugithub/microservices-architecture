package com.quiz_service_microservice.Quiz_Service_MicroService.service;


import com.quiz_service_microservice.Quiz_Service_MicroService.dao.QuizDao;
import com.quiz_service_microservice.Quiz_Service_MicroService.feign.QuizInterface;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.QuestionWrapper;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.Quiz;
import com.quiz_service_microservice.Quiz_Service_MicroService.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    public QuizDao quizDao;

    @Autowired
    public QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numOfQuesns, String title) {

        // I want the list of the questions here where the questions are on QuestionDao not QuizDao
//        List<Question>  questions = questionDao.findRandomQuestionsByCategory(category, numOfQuesns);
//
//            Quiz quiz = new Quiz();
//        // setId is auto generated. we are setting title and finally quiz also wants questions right.
//            quiz.setTitle(title);
//            // Questions are actually coming from db. to get the questions we have to ask Dao layer in our case it is QuestionDao
//            quiz.setQuestions(questions);
//            quizDao.save(quiz);

        // Initially we were asking Dao which is interacting with the question database to basically create a question.
        // But this time it needs to interact with the Question Service to get the questions but this time we are not getting actual question what we are getting is List of Integers
        // We don't want to store question data we want id's. From the quiz service we have to send a request to the question microservice. for this we have to use something called a Rest template
        // RestTemplate is class basically it is used to send request to other server(service)
        // http://localhost:8080/question/generate (Not exactly we can do like this because the problem is that if you have in some other machine okay you will say okay, we can use an IP address.
        // But then if you understand microservices properly we are not sure where we are running the instance. Right. So I don't want to depend on the IP address.
        // Next I don't want to hardcode the port, the port number here.
        // At production stage we don't know the values that's where we have to introduce new services. if they want to interact, we need two things.
        // 1. Feign Client - which is same as HTTP Web Request the difference is it provides declarative way of requesting the other service
        // We no need to hard code the values It will help you to declare what you want. And what are the APIs you want to expose. I mean, what are the APIs you want to hit
        // 2. if quiz service is trying to search for the question service. It means the question service need to be discoverable. To discover a particular API or a particular microservice.
        // So it is from Netflix, which is Netflix Eureka. For that we have we need two things.
        // 1. We need a Eureka server where all the microservices have to register themselves to the Eureka server.
        // 2. And then one microservice can search another microservice from the Eureka server using Eureka client. So every microservice which want to search will also be having Eureka client.
        // So they will search basically and by doing this we are solving a problem of IP address and port number.
        List<Integer> quizIds = quizInterface.generateQuestionsFOrQuiz(category, numOfQuesns).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(quizIds);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
//        Optional<Quiz> quiz = quizDao.findById(id);
//        // This findById might not return a data if id is not present so that's why we are mentioning Optional
//        // We will get the quiz with quiz we will get questions (quiz object which actually have questions)
//        // But we have to convert those questions into QuestionWrapper because we are returning QuestionWrapper
//        List<Question>  questionsFromDb = quiz.get().getQuestions(); // I will get the questions from the quiz whenever we use Optional we have to fetch object then questions
//        // we cannot return questionsFromDb directly because List is of QuestionWrapper not of Question
//        // So we are converting each question into QuestionWrapper
//        List<QuestionWrapper> questionsForUser = new ArrayList<>();
//        for(Question q : questionsFromDb){
//            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
//            questionsForUser.add(qw);
//            // Basically we were able to convert, or we were able to fill the questionsForUser with the help of the questionsFromDb.
//        }

        // ------what we are trying to achieve here is for a particular quiz. So let's say we have an ID right.So what happens is every time I can create multiple quiz for the same topic.
        // So every quiz will have a different name and different IDs. Now for that particular ID. So let's say if I create a quiz for fifth quiz. So the ID will be five.
        // And in that particular quiz I have let's say ten questions. So for that quiz I want all the questions. First of all we are not storing the questions in the quiz table.
        // So for a quiz let's say we have quiz number two for this quiz which is which has five questions. In db we can see it only have numbers. But then if you want to conduct the quiz I need questions as well.
        // for that we have to again interact between the quiz and the question service.
        // Firstly this particular id i need quiz
        Quiz quiz = quizDao.findById(id).get();
        // Before we get the questions from question service we need id's of it. To get the id's if we go back to Quiz which has all the questionID's
        List<Integer> ids = quiz.getQuestionIds();
        // Once we get th Question ID's. I can request my question service to give me the question for this Id's which will return ResponseEntity<List<QuestionWrapper>>
        return quizInterface.getAllQuestionsFromId(ids);
    }

//    public ResponseEntity<Integer> submitQuiz(int id, List<Response> responses) {
//        Quiz quiz = quizDao.findById(id).get();
//        List<Question> questions = quiz.getQuestions();
//        int count = 0;
//        int i = 0;
//        for(Response response : responses){
//            if(response.getResponse().equals(questions.get(i).getRightAnswer())){
//                    count++;
//                i++;
//            }
//        }
//        return  new ResponseEntity<>(count, HttpStatus.OK);
//    }

    public ResponseEntity<Integer> submitQuiz(int id, List<Response> responses) {
//        Quiz quiz = quizDao.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
//        List<Question> questions = quiz.getQuestions();
//
//        Map<Integer, String> questionAnswers = questions.stream().collect(Collectors.toMap(Question::getId, Question::getRightAnswer));
//        int correctAnswer = 0;
//        for (Response response : responses) {
//            String questionAnswer = questionAnswers.get(response.getId());
//            if (questionAnswer != null && questionAnswer.equals(response.getResponse())) {
//                correctAnswer++;
//            }
//        }
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }

}
