package com.shop.board.jpa.answer;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.shop.board.spring.question.SpringQuestion;
import com.shop.board.spring.question.SpringQuestionService;
import com.shop.board.user.SiteUser;
import com.shop.board.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/jpa/answer")
@RequiredArgsConstructor
@Controller
public class JpaAnswerController {

	private final SpringQuestionService questionService;
	private final JpaAnswerService answerService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content,
    	@Valid JpaAnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        SpringQuestion question = this.questionService.getQuestion(id); 
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        JpaAnswer answer = this.answerService.create(question, 
                answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s", 
                answer.getQuestion().getId(), answer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(JpaAnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        JpaAnswer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "??????????????? ????????????.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", 
                answer.getQuestion().getId(), answer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid JpaAnswerForm answerForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        JpaAnswer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "??????????????? ????????????.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        JpaAnswer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "??????????????? ????????????.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        JpaAnswer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);
        return String.format("redirect:/question/detail/%s#answer_%s", 
                answer.getQuestion().getId(), answer.getId());
    }
}
