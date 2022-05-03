package com.shop.board.java.question;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
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

import com.shop.board.java.answer.JavaAnswerForm;
import com.shop.board.user.SiteUser;
import com.shop.board.user.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/java/question")
@RequiredArgsConstructor
@Controller
public class JavaQuestionController {
	
	private final JavaQuestionService javaquestionService;
	private final UserService userService;

	@RequestMapping("/list")
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<JavaQuestion> paging = this.javaquestionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "javaboard/question_list";
    }
	
	@RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, JavaAnswerForm javaanswerForm) {
		JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id);
        model.addAttribute("javaquestion", javaquestion);
        return "javaboard/question_detail";
    }
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
    public String questionCreate(JavaQuestionForm javaquestionForm) {
        return "javaboard/question_form";
    }
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
    public String questionCreate(@Valid JavaQuestionForm javaquestionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "javaboard/question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
		this.javaquestionService.create(javaquestionForm.getSubject(), javaquestionForm.getContent(), siteUser);
        return "redirect:java/question/list";
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(JavaQuestionForm javaquestionForm, @PathVariable("id") Integer id, Principal principal) {
        JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id);
        if(!javaquestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        javaquestionForm.setSubject(javaquestion.getSubject());
        javaquestionForm.setContent(javaquestion.getContent());
        return "javaboard/question_form";
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid JavaQuestionForm javaquestionForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "javaboard/question_form";
        }
        JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id);
        if (!javaquestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.javaquestionService.modify(javaquestion, javaquestionForm.getSubject(), javaquestionForm.getContent());
        return String.format("redirect:javaboard/question/detail/%s", id);
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id);
        if (!javaquestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.javaquestionService.delete(javaquestion);
        return "redirect:/java/";
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        JavaQuestion javaquestion = this.javaquestionService.getjavaQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.javaquestionService.vote(javaquestion, siteUser);
        return String.format("redirect:java/question/detail/%s", id);
    }
}
