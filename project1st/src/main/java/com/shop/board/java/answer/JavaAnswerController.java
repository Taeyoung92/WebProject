package com.shop.board.java.answer;

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

import com.shop.board.java.question.JavaQuestion;
import com.shop.board.java.question.JavaQuestionService;
import com.shop.board.user.SiteUser;
import com.shop.board.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/java/answer")
@RequiredArgsConstructor
@Controller
public class JavaAnswerController {

	private final JavaQuestionService javaquestionService;
	private final JavaAnswerService answerService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content,
    	@Valid JavaAnswerForm javaanswerForm, BindingResult bindingResult, Principal principal) {
        JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id); 
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", javaquestion);
            return "java/question_detail";
        }
        JavaAnswer javaanswer = this.answerService.javacreate(javaquestion, 
                javaanswerForm.getContent(), siteUser);
        return String.format("redirect:/java/question/detail/%s#answer_%s", 
                javaanswer.getJavaquestion().getId(), javaanswer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(JavaAnswerForm javaanswerForm, @PathVariable("id") Integer id, Principal principal) {
        JavaAnswer javaanswer = this.answerService.getjavaAnswer(id);
        if (!javaanswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(javaanswer, javaanswerForm.getContent());
        return String.format("redirect:/java/question/detail/%s#answer_%s", 
                javaanswer.getJavaquestion().getId(), javaanswer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid JavaAnswerForm javaanswerForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "java/answer_form";
        }
        JavaAnswer javaanswer = this.answerService.getjavaAnswer(id);
        if (!javaanswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(javaanswer, javaanswerForm.getContent());
        return String.format("redirect:/java/question/detail/%s#answer_%s", javaanswer.getJavaquestion().getId(), javaanswer.getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        JavaAnswer javaanswer = this.answerService.getjavaAnswer(id);
        if (!javaanswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(javaanswer);
        return String.format("redirect:/java/question/detail/%s", javaanswer.getJavaquestion().getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        JavaAnswer javaanswer = this.answerService.getjavaAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(javaanswer, siteUser);
        return String.format("redirect:/java/question/detail/%s#answer_%s", 
        		javaanswer.getJavaquestion().getId(), javaanswer.getId());
    }
}
