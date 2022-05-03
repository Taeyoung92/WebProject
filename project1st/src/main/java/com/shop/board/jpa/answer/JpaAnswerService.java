package com.shop.board.jpa.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shop.board.DataNotFoundException;
import com.shop.board.spring.question.SpringQuestion;
import com.shop.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JpaAnswerService {

	private final JpaAnswerRepository answerRepository;


    public JpaAnswer create(SpringQuestion question, String content, SiteUser author) {
        JpaAnswer answer = new JpaAnswer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }
    
    public JpaAnswer getAnswer(Integer id) {
        Optional<JpaAnswer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(JpaAnswer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    
    public void delete(JpaAnswer answer) {
        this.answerRepository.delete(answer);
    }
    
    public void vote(JpaAnswer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}
