package com.quiz_service_microservice.Quiz_Service_MicroService.dao;


import com.quiz_service_microservice.Quiz_Service_MicroService.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

}
