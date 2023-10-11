package com.clipclap.rego.service;


import com.clipclap.rego.model.dto.NoticeDTO;
import com.clipclap.rego.model.entitiy.Notice;
import com.clipclap.rego.model.entitiy.Question;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.NoticeRepository;
import com.clipclap.rego.validation.NoticeForm;
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
public class NoticeService {

    private final NoticeRepository noticeReprository;

    //DTO -> Entity로 변환
    public Notice toEntity(NoticeDTO noticeDTO){
        Notice notice = new Notice();
        notice.setSubject(noticeDTO.getSubject());
        notice.setContent(noticeDTO.getContent());
        notice.setCreateDate(noticeDTO.getCreateDate());
        return notice;
    }
    //Entity->DTO 로 변환
    public NoticeDTO toDTO(Notice notice){
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setSubject(notice.getSubject());
        noticeDTO.setContent(notice.getContent());
        noticeDTO.setCreateDate(notice.getCreateDate());
        return noticeDTO;
    }

    //삭제처리
    public void delete(Notice notice) {
        noticeReprository.delete(notice);
    }

    //공지수정처리
    public void modify(Notice notice, String subject, String content) {
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setModifyDate(LocalDateTime.now());
        noticeReprository.save(notice);
    }

    //공지등록처리
    public void addDTO(NoticeDTO noticeDTO) {
        Notice notice = toEntity(noticeDTO);
        noticeReprository.save(notice);
//        Notice notice = new Notice();
//        notice.setSubject(noticeDTO.getSubject());
//        notice.setContent(noticeDTO.getContent());
//        notice.setCreateDate(noticeDTO.getCreateDate());

    }


    //공지등록처리
    //SiteUser siteUser : 공지작성자의 정보
    public void add(NoticeForm noticeForm, User user){
        Notice notice = new Notice();

        if(noticeForm.getNoticeId() != null){     // 수정할 공지의 글번호 설정
            notice.setId(noticeForm.getNoticeId());
        }

        notice.setSubject(noticeForm.getSubject());
        notice.setContent(noticeForm.getContent());
        notice.setCreateDate(LocalDateTime.now());
        notice.setWriter(user);
        noticeReprository.save(notice);
    }


    //공지상세조회
/*    public NoticeDTO getNotice(Integer id){
        Optional<Notice> notice = noticeReprository.findById(id);
        if(notice.isPresent()){
            //return notice.get();
            Notice notice1= notice.get();
            NoticeDTO noticeDTO = new NoticeDTO();
            noticeDTO.setSubject(notice1.getSubject());
            noticeDTO.setContent(notice1.getContent());
            return noticeDTO;
        }else{
            throw new DataNotFoundException("Notice not Found");
        }
    }
*/
    //공지상세조회
    public Notice getNotice(Integer id){
        Optional<Notice> notice = noticeReprository.findById(id);
        if(notice.isPresent()){
            return notice.get();
        }else{
            throw new DataNotFoundException("Notice not Found");
        }
    }

    //페이징기능이 있는 질문목록조회
    //파라미터 int page- 보고싶은 페이지번호
    public Page<Notice> getList(int page){
        //질문목록조회 : findAll()
        //페이징기능이 있는 질문목록조회-Page<Notice> findAll(Pageable pageable);
        /*파라미터 int page- 보고싶은 페이지번호
          int size -  1page당 출력할 게시물수
          Sort sort- 정렬 */
        List<Sort.Order> sorts = new ArrayList();
        //Notice entity의 createDate속성을 내림차순정렬
        //정렬조건을 추가하고 싶다면  sorts.add()한다
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));
        return noticeReprository.findAll(pageable);
    }



}