package com.clipclap.rego.service;


import com.clipclap.rego.model.dto.QuestionDTO;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.QuestionRepository;
import com.clipclap.rego.validation.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionReprository;


    //DTO -> Entity로 변환
    public Question toEntity(QuestionDTO qeustionDTO){
        Question question = new Question();
        question.setSubject(qeustionDTO.getSubject());
        question.setContent(qeustionDTO.getContent());
        question.setCreateDate(qeustionDTO.getCreateDate());
        return question;
    }
    //Entity->DTO 로 변환
    public QuestionDTO toDTO(Question question){
        QuestionDTO qeustionDTO = new QuestionDTO();
        qeustionDTO.setSubject(question.getSubject());
        qeustionDTO.setContent(question.getContent());
        qeustionDTO.setCreateDate(question.getCreateDate());
        return qeustionDTO;
    }

    //삭제처리
    public void delete(Question question) {
        questionReprository.delete(question);
    }

    //질문수정처리
    public void modify(Question question) {
        question.setSubject(question.getSubject());
        question.setContent(question.getContent());
        question.setModifyDate(LocalDateTime.now());
        questionReprository.save(question);
    }

    //질문등록처리
    public void addDTO(QuestionDTO qeustionDTO) {
        Question question = toEntity(qeustionDTO);
        questionReprository.save(question);
//        Question question = new Question();
//        question.setCategory(qeustionDTO.getCategory());
//        question.setSubject(qeustionDTO.getSubject());
//        question.setContent(qeustionDTO.getContent());
//        question.setCreateDate(qeustionDTO.getCreateDate());

    }



    //질문등록처리
    //SiteUser siteUser : 질문작성자의 정보
    public void add(QuestionForm questionForm,  User user, String imagePath){
        Question question = new Question();
        question.setCategory(questionForm.getCategory());

        if(questionForm.getQuestionId() != null){     // 수정할 질문의 글번호 설정
            question.setId(questionForm.getQuestionId());
        } else {

        }

        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setCreateDate(LocalDateTime.now());
        question.setWriter(user);
        if(imagePath != null){
            question.setImagePath(imagePath);
        } else {
            if( question.getId() == null){
                question.setImagePath(null);
            } else {
                Optional<Question> optionalQuestion = questionReprository.findById(question.getId());
                if(optionalQuestion.isPresent()){     // 원래 이미지 경로로 재저장
                    question.setImagePath(optionalQuestion.get().getImagePath());
                }
            }

        }
        questionReprository.save(question);
    }


    //질문상세조회
/*    public QuestionDTO getQuestion(Integer id){
        Optional<Question> question = questionReprository.findById(id);
        if(question.isPresent()){
            //return question.get();
            Question question1= question.get();
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setSubject(question1.getSubject());
            questionDTO.setContent(question1.getContent());
            return questionDTO;
        }else{
            throw new DataNotFoundException("Question not Found");
        }
    }
*/
    //질문상세조회
    public Question getQuestion(Integer id){
        Optional<Question> question = questionReprository.findById(id);
        if(question.isPresent()){
            return question.get();
        }else{
            throw new DataNotFoundException("Question not Found");
        }
    }

    //페이징기능이 있는 질문목록조회
    //파라미터 int page- 보고싶은 페이지번호
    public Page<Question> getList(int page){
        //질문목록조회 : findAll()
        //페이징기능이 있는 질문목록조회-Page<Question> findAll(Pageable pageable);
        /*파라미터 int page- 보고싶은 페이지번호
          int size -  1page당 출력할 게시물수
          Sort sort- 정렬 */
        List<Sort.Order> sorts = new ArrayList();
        //Question entity의 createDate속성을 내림차순정렬
        //정렬조건을 추가하고 싶다면  sorts.add()한다
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,5,Sort.by(sorts));
        return questionReprository.findAll(pageable);
    }


}