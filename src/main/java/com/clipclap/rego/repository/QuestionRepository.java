package com.clipclap.rego.repository;


import com.clipclap.rego.model.entitiy.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//Reprository는  Entity에 의하여 생성된 db에 접근하여
//작업하는 여러 메서드들로 구성된 interface이다
//=>CRUD

// JpaRepository인터페이스를 상속하고 있다
// <Repository의 대상이 되는 Entity타입,  Entity타입의 PK타입>
//여기에서는 <Question,Integer> Repository의 대상이 되는 Question의 PK는  Integer
public interface QuestionRepository extends JpaRepository<Question,Integer> {

    //subject(제목)으로 Question가져오기
    Question findBySubject(String subject);

    //제목=? and 내용=?에 해당하는 Question 조회
    /*예문
    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
    select u from User u where u.emailAddress = ?1 and u.lastname = ?2
     */
    Question findBySubjectAndContent(String subject, String content);

    //질문제목에   검색키워드가 포함된 Qustion Entity조회
   /*                           ~~~~ where x.firstname like %?%
   Like연산자 예문
   findByFirstnameLike          ~~~~ where x.firstname like ?1
  *NotLike산자 예문
   findByFirstnameNotLike       ~~~ where x.firstname not like ?1
   */
    List<Question> findBySubjectLike(String subject);

    //페이징기능이 있는 질문목록조회
    Page<Question> findAll(Pageable pageable);
}




