package com.shop.board.java.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JavaQuestionRepository extends JpaRepository<JavaQuestion, Integer> {
	JavaQuestion findBySubject(String subject);
	JavaQuestion findBySubjectAndContent(String subject, String content);
	List<JavaQuestion> findBySubjectLike(String subject);
	Page<JavaQuestion> findAll(Pageable pageable);
	Page<JavaQuestion> findAll(Specification<JavaQuestion> spec, Pageable pageable);
}
