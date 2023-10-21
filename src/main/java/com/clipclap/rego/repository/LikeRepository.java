package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.LikeAttraction;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.model.entitiy.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeAttraction, Integer> {

    // 특정 사용자에 대한 좋아요 목록 조회
    List<LikeAttraction> findByUser(Optional<User> user);

    // 특정 관광지에 대한 좋아요 목록 조회
    List<LikeAttraction> findByAttraction(Optional<TouristAttraction> attraction);

    // 특정 사용자와 관광지에 대한 좋아요 조회
    LikeAttraction findByUserAndAttraction(User user, TouristAttraction attraction);


    void deleteByLikeId(Integer likeId);

    long countByUserAndAttraction(User user, TouristAttraction touristAttraction);

    @Query("SELECT l.attraction FROM LikeAttraction l GROUP BY l.attraction ORDER BY COUNT(l.attraction) DESC")
    List<TouristAttraction> findTopAttractions(Pageable pageable);
}
