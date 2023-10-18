package com.clipclap.rego.repository;


import com.clipclap.rego.model.entitiy.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//Reprository는  Entity에 의하여 생성된 db에 접근하여
//작업하는 여러 메서드들로 구성된 interface이다
//=>CRUD

// JpaRepository인터페이스를 상속하고 있다
// <Repository의 대상이 되는 Entity타입,  Entity타입의 PK타입>
//여기에서는 <Question,Integer> Repository의 대상이 되는 Question의 PK는  Integer
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // 카테고리로 질문 조회
    Question findByCategory(String category);

    // 제목으로 질문 조회
    Question findBySubject(String subject);

    // 제목과 내용으로 질문 조회
    Question findBySubjectAndContent(String subject, String content);

    // 질문 제목에 검색 키워드가 포함된 질문 조회
    List<Question> findBySubjectContaining(String subject);

    // 페이징 기능이 있는 질문 목록 조회
    Page<Question> findAll(Pageable pageable);

    List<Question> findByWriter_Email(String email);
}
