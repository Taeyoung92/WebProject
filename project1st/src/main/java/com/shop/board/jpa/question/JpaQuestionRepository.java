package com.shop.board.jpa.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionRepository extends JpaRepository<JpaQuestion, Integer> {
	JpaQuestion findBySubject(String subject);
	JpaQuestion findBySubjectAndContent(String subject, String content);
	List<JpaQuestion> findBySubjectLike(String subject);
	Page<JpaQuestion> findAll(Pageable pageable);
	Page<JpaQuestion> findAll(Specification<JpaQuestion> spec, Pageable pageable);
}
