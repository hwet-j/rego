package com.clipclap.rego.service;


import com.clipclap.rego.model.entitiy.Answer;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    // 답변 삭제
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    // 답변 수정
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now()); // 답변 수정일
        answerRepository.save(answer);
    }

    // 답변 상세 조회
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("Answer not found");
        }
    }

    // 답변 등록 처리
    public void add(Question question, String content, User User) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question); // fk에 해당하는 질문 객체
        answer.setWriter(User);
        answerRepository.save(answer);
    }

}
