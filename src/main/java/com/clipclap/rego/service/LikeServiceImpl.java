package com.clipclap.rego.service;

import com.clipclap.rego.model.entitiy.LikeAttraction;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.model.entitiy.User;
import com.clipclap.rego.repository.LikeRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import com.clipclap.rego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TouristAttractionRepository attractionRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository,
                                     UserRepository userRepository,
                                     TouristAttractionRepository attractionRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.attractionRepository = attractionRepository;
    }

    @Override
    @Transactional
    public String addLike(String userEmail, Integer attractionId) {

        // 사용자와 관광지를 찾고 좋아요 추가 작업을 수행하는 코드를 작성합니다.
        // 필요에 따라 예외 처리 및 로깅도 수행할 수 있습니다.

        // 사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            return "User Not Found error";
        }

        // 관광지 정보 조회
        Optional<TouristAttraction> optionalTouristAttraction = attractionRepository.findByTouristAttractionId(attractionId);
        TouristAttraction touristAttraction = null;
        if (optionalTouristAttraction.isPresent()) {
            touristAttraction = optionalTouristAttraction.get();
        } else {
            return "touristAttraction Not Found error";
        }

        long likeCnt = likeRepository.countByUserAndAttraction(user, touristAttraction);



        if(likeCnt==0){
            // 좋아요 추가 작업을 수행
            LikeAttraction like = new LikeAttraction();
            like.setUser(user);
            like.setAttraction(touristAttraction);
            like.setAddTime(LocalDateTime.now());

            likeRepository.save(like);

            return "Like added successfully";
        }else{
            LikeAttraction likeAttraction =likeRepository.findByUserAndAttraction(user, touristAttraction);
            likeRepository.deleteByLikeId(likeAttraction.getLikeId());

            return "Like deleted successfully";
        }

    }

    @Override
    public List<LikeAttraction> getUserLikes(String userEmail) {
        // 사용자의 모든 좋아요 목록을 반환하는 코드를 작성합니다.
        // 실제로 목록을 조회하도록 구현해야 합니다.
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user != null) {
            return likeRepository.findByUser(user);
        }
        return null;  // 사용자를 찾을 수 없는 경우 빈 목록 또는 예외 처리를 고려
    }

    @Override
    public List<LikeAttraction> getAttractionLikes(Integer attractionId) {
        // 특정 관광지에 대한 모든 좋아요 목록을 반환하는 코드를 작성합니다.
        // 실제로 목록을 조회하도록 구현해야 합니다.
        Optional<TouristAttraction> attraction = attractionRepository.findById(attractionId);
        if (attraction != null) {
            return likeRepository.findByAttraction(attraction);
        }
        return null;  // 관광지를 찾을 수 없는 경우 빈 목록 또는 예외 처리를 고려
    }

    @Override
    public int countByAttactionIdAndUserEmail(Integer attractionId, String userEmail) {
        long likeCnt = 0;

        User user;
        TouristAttraction touristAttraction;

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()){
            return -1;
        } else {
            user = optionalUser.get();
        }

        Optional<TouristAttraction> optionalTouristAttraction = attractionRepository.findByTouristAttractionId(attractionId);
        if(optionalTouristAttraction.isEmpty()){
            return -1;
        }else {
            touristAttraction = optionalTouristAttraction.get();
        }

        likeCnt = likeRepository.countByUserAndAttraction(user,touristAttraction);
        return (int) likeCnt;
    }

    @Override
    public boolean toggleLike(Integer attractionId, String userEmail) {
        long likeCnt = 0;
        //user, toursitAttraction 객체 생성
        User user;
        TouristAttraction touristAttraction;
        
        //DB에 존재하지 않을시 에러이므로 false 존재하면 위의 빈객체에 담음
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty()){
            return false;
        } else {
            user = optionalUser.get();
        }

        Optional<TouristAttraction> optionalTouristAttraction = attractionRepository.findByTouristAttractionId(attractionId);
        if(optionalTouristAttraction.isEmpty()){
            return false;
        }else {
            touristAttraction = optionalTouristAttraction.get();
        }

        likeCnt = likeRepository.countByUserAndAttraction(user,touristAttraction);
        if(likeCnt==1){
            LikeAttraction likeAttraction = likeRepository.findByUserAndAttraction(user,touristAttraction);
            likeRepository.deleteByLikeId(likeAttraction.getLikeId());
            return true;
        }else{
            LikeAttraction likeAttraction = new LikeAttraction();
            likeAttraction.setAttraction(touristAttraction);
            likeAttraction.setUser(user);
            likeAttraction.setAddTime(LocalDateTime.now());
            likeRepository.save(likeAttraction);
            return true;
        }
    }
}
