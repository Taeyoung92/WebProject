package com.shop.board.jpa.question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JpaQuestionForm {

	@NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

	@NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
