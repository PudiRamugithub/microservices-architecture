package com.springboot_microservices.Question.Service.MicroService.service;


import com.springboot_microservices.Question.Service.MicroService.dao.QuestionDao;
import com.springboot_microservices.Question.Service.MicroService.model.Question;
import com.springboot_microservices.Question.Service.MicroService.model.QuestionWrapper;
import com.springboot_microservices.Question.Service.MicroService.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

//    public ResponseEntity<List<Question>> getAllQuestions() {
//        try {
//            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
//    }
//
//    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
//        try {
//            return new ResponseEntity<>(questionDao.getQuestionsByCategory(category), HttpStatus.OK);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
//    }
//
//    public ResponseEntity<String> addQuestion(Question question) {
//        // Check if a question with the same title already exists
//        if (questionDao.existsByquestionTitle(question.getQuestionTitle())) {
//                return new ResponseEntity<>("Question with this title already exists", HttpStatus.CONFLICT);
//            }
//        questionDao.save(question);
//        return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
//    }

    private QuestionWrapper mapToWrapper(Question q) {
        return new QuestionWrapper(
                q.getId(),
                q.getQuestionTitle(),
                q.getOption1(),
                q.getOption2(),
                q.getOption3(),
                q.getOption4()
        );
    }

    public ResponseEntity<List<QuestionWrapper>> getAllQuestionsV2() {
        try {
            List<QuestionWrapper> wrapperList = questionDao.findAll()
                    .stream()
                    .map(this::mapToWrapper)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(wrapperList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsByCategoryV2(String category) {
        try {
            List<QuestionWrapper> wrapperList = questionDao.getQuestionsByCategory(category)
                    .stream()
                    .map(this::mapToWrapper)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(wrapperList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<QuestionWrapper> getQuestionByIdV2(int id) {
        return questionDao.findById(id)
                .map(q -> new ResponseEntity<>(mapToWrapper(q), HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> addQuestion(Question question) {
        // Check if a question with the same title already exists
        if (questionDao.existsByquestionTitle(question.getQuestionTitle())) {
            return new ResponseEntity<>("Question with this title already exists", HttpStatus.CONFLICT);
        }
        questionDao.save(question);
        return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
    }


    public ResponseEntity<String> updateQuestion(int id, Question updatedQuestion) {
        if (!questionDao.existsById(id)) {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
        updatedQuestion.setId(id);
        questionDao.save(updatedQuestion);
        return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteQuestionById(int id) {
        if (!questionDao.existsById(id)) {
            return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
        }
        try {
            questionDao.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting question", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Here we are returning the id's of the questions not the Questions
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numberOfQuestions) {
        List<Integer>  questions = questionDao.findRandomQuestionsByCategory(category, numberOfQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        // we can't send empty right. OfCourse we need to return questions based on the question ID.
        List<Question> questions = new ArrayList<>();
        for (Integer questionId : questionIds) {
            // For each id we need to get Question First
            questions.add(questionDao.findById(questionId).get());
        }
        // After getting the Questions we are not sending the Questions directly we are sending QuestionWrapper for that we need to iterate again and fill that wrappers
        for(Question question : questions) {
            wrappers.add(mapToWrapper(question));
        }
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }


    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int correctAnswer = 0;
        for (Response response : responses) {
            Optional<Question> question = questionDao.findById(response.getId());
            if (response.getResponse().equals(question.get().getRightAnswer())) {
                correctAnswer++;
            }
        }
        return new ResponseEntity<>(correctAnswer, HttpStatus.OK);
    }
}
