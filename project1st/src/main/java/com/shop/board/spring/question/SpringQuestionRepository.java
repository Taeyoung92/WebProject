package com.shop.board.spring.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringQuestionRepository extends JpaRepository<SpringQuestion, Integer> {
	SpringQuestion findBySubject(String subject);
	SpringQuestion findBySubjectAndContent(String subject, String content);
	List<SpringQuestion> findBySubjectLike(String subject);
	Page<SpringQuestion> findAll(Pageable pageable);
	Page<SpringQuestion> findAll(Specification<SpringQuestion> spec, Pageable pageable);
}
