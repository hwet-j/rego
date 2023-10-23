package com.clipclap.rego.model.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

// notice 테이블 관련 데이터를 처리하기위한 클래스
/* id  int   primary key auto_increment,
 subject  varchar(200),
 content  text,
 create_date  datetime*/

@Getter
@Setter
@Entity
public class Notice {
    //field
    //@Id : pk
    //@GeneratedValue : 1씩 자동증가
    //GenerationType.IDENTITY : 해당 컬럼만의 독립적인 자동증가번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;//글번호. 1씩증가.pk

    //@Column : 컬럼
    @Column(length = 200)
    private String subject;//제목

    @Column(columnDefinition = "TEXT")
    private String content;//내용

    //필드명이 대소문자섞인형태는 _, 컬럼명은 전부소문자로 처리
    @Column
    private LocalDateTime createDate;//작성일
    @Column
    private LocalDateTime modifyDate;//수정일

    private String imagePath;           //이미지경로



    //한 명의 USER가 여러 개의 글을 작성할 수 있다
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer; //user정보(id,username,email,password)

}

