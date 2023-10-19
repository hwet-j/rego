package com.clipclap.rego.service;

import com.clipclap.rego.model.entitiy.LikeAttraction;

import java.util.List;

public interface LikeService {

    String addLike(String userEmail, Integer attractionId);


    List<LikeAttraction> getUserLikes(String userEmail);

    List<LikeAttraction> getAttractionLikes(Integer attractionId);

    int countByAttactionIdAndUserEmail(Integer attractionId, String userEmail);

    boolean toggleLike(Integer attractionId, String userEmail);
}
