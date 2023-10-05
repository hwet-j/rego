package com.clipclap.rego.model.dto;

import com.clipclap.rego.model.entitiy.Answer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeDTO {

    private  Integer id;//글번호. 1씩증가.pk

    private String subject;//제목

    private String content;//내용

    private LocalDateTime createDate;//작성일

    private List<Answer> answerList;


}
