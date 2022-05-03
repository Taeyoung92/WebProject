package com.shop.board.java.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shop.board.DataNotFoundException;
import com.shop.board.java.answer.JavaAnswer;
import com.shop.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JavaQuestionService {

	private final JavaQuestionRepository questionRepository;
	
	private Specification<JavaQuestion> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<JavaQuestion> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<JavaQuestion, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<JavaQuestion, JavaAnswer> a = q.join("javaanswerList", JoinType.LEFT);
                Join<JavaAnswer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
            }
        };
    }

    public List<JavaQuestion> getList() {
        return this.questionRepository.findAll();
    }
    
    public JavaQuestion getjavaQuestion(Integer id) {  
        Optional<JavaQuestion> javaquestion = this.questionRepository.findById(id);
        if (javaquestion.isPresent()) {
            return javaquestion.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    
    public Page<JavaQuestion> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<JavaQuestion> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }
    
    public void create(String subject, String content, SiteUser user) {
        JavaQuestion q = new JavaQuestion();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }
    
    public void modify(JavaQuestion javaquestion, String subject, String content) {
    	javaquestion.setSubject(subject);
    	javaquestion.setContent(content);
    	javaquestion.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(javaquestion);
    }
    
    public void delete(JavaQuestion javaquestion) {
        this.questionRepository.delete(javaquestion);
    }
    
    public void vote(JavaQuestion javaquestion, SiteUser siteUser) {
    	javaquestion.getVoter().add(siteUser);
        this.questionRepository.save(javaquestion);
    }
}
