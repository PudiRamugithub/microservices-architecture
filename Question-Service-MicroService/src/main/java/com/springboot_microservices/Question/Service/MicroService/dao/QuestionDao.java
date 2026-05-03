package com.springboot_microservices.Question.Service.MicroService.dao;

import com.springboot_microservices.Question.Service.MicroService.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {

    boolean existsByquestionTitle(String questionTitle);

    @Query("SELECT q FROM Question q WHERE LOWER(q.category) = LOWER(?1)")
    List<Question> getQuestionsByCategory(String category);

    List<Question> getQuestionById(int id);

    // We just need to get the questions for the category based on the number of questions.
    @Query(value = "select q.id from question q where q.category=:category ORDER BY RANDOM() LIMIT :numOfQuesns", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int numOfQuesns);
}
