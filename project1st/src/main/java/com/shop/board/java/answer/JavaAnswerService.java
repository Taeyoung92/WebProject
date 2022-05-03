package com.shop.board.java.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shop.board.DataNotFoundException;
import com.shop.board.java.question.JavaQuestion;
import com.shop.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JavaAnswerService {

	private final JavaAnswerRepository answerRepository;


    public JavaAnswer javacreate(JavaQuestion javaquestion, String content, SiteUser author) {
        JavaAnswer javaanswer = new JavaAnswer();
        javaanswer.setContent(content);
        javaanswer.setCreateDate(LocalDateTime.now());
        javaanswer.setJavaquestion(javaquestion);
        javaanswer.setAuthor(author);
        this.answerRepository.save(javaanswer);
        return javaanswer;
    }
    
    public JavaAnswer getjavaAnswer(Integer id) {
        Optional<JavaAnswer> javaanswer = this.answerRepository.findById(id);
        if (javaanswer.isPresent()) {
            return javaanswer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(JavaAnswer javaanswer, String content) {
    	javaanswer.setContent(content);
    	javaanswer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(javaanswer);
    }
    
    public void delete(JavaAnswer javaanswer) {
        this.answerRepository.delete(javaanswer);
    }
    
    public void vote(JavaAnswer javaanswer, SiteUser siteUser) {
    	javaanswer.getVoter().add(siteUser);
        this.answerRepository.save(javaanswer);
    }
}
