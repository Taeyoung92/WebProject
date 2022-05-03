package com.shop.board.spring.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shop.board.DataNotFoundException;
import com.shop.board.spring.question.SpringQuestion;
import com.shop.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SpringAnswerService {

	private final SpringAnswerRepository answerRepository;


    public SpringAnswer create(SpringQuestion question, String content, SiteUser author) {
        SpringAnswer answer = new SpringAnswer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }
    
    public SpringAnswer getAnswer(Integer id) {
        Optional<SpringAnswer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(SpringAnswer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    
    public void delete(SpringAnswer answer) {
        this.answerRepository.delete(answer);
    }
    
    public void vote(SpringAnswer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
