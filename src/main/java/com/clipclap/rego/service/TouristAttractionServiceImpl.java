package com.clipclap.rego.service;

import com.clipclap.rego.mapper.TouristAttractionMapper;
import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.LikeAttraction;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.repository.LikeRepository;
import com.clipclap.rego.repository.TouristAttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TouristAttractionServiceImpl implements TouristAttractionService {

    private final TouristAttractionRepository touristAttractionRepository;
    private final LikeService likeService;
    private final LikeRepository likeRepository;

    @Override
    public List<TouristAttractionDTO> touristListAll() {
        List<TouristAttraction> touristAttractions = touristAttractionRepository.findByLatitudeIsNotNullAndLongitudeIsNotNull();

        List<TouristAttractionDTO> touristAttractionDTOs = touristAttractions.stream()
                .map(entity -> {
                    TouristAttractionDTO dto = TouristAttractionMapper.entityToDto(entity);
                    if (entity.getCityName() != null){
                        dto.setCityName(entity.getCityName().getCityName()); // cityName 설정
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        return touristAttractionDTOs;
    }



    @Override
    public List<TouristAttractionDTO> countryAttractionList(City cityName) {

        List<TouristAttraction>  touristAttractions = touristAttractionRepository.findByCityName(cityName);

        for(TouristAttraction item: touristAttractions){
            System.out.println(item.getTouristAttractionId());
        }

        List<TouristAttractionDTO> touristAttractionDTOs = touristAttractions.stream()
                .map(entity -> {
                    TouristAttractionDTO dto = TouristAttractionMapper.entityToDto(entity);
                    dto.setCityName(entity.getCityName().getCityName()); // cityName 설정
                    return dto;
                })
                .collect(Collectors.toList());

        return touristAttractionDTOs;
    }
    @Override
    public List<TouristAttractionDTO> cityContentTypeList(City cityName, String contentType) {

        List<TouristAttraction>  touristAttractions = touristAttractionRepository.findByCityNameAndContentType(cityName,contentType);

        List<TouristAttractionDTO> touristAttractionDTOs = touristAttractions.stream()
                .map(entity -> {
                    TouristAttractionDTO dto = TouristAttractionMapper.entityToDto(entity);
                    dto.setCityName(entity.getCityName().getCityName()); // cityName 설정
                    return dto;
                })
                .collect(Collectors.toList());

        return touristAttractionDTOs;
    }

    @Override
    public List<TouristAttractionFullDTO> getTouristAttractionsWithCityAndCountry() {

        List<TouristAttractionFullDTO> touristAttractions = touristAttractionRepository.getTouristAttractionsWithCityAndCountry();

        return touristAttractions;
    }

    @Override
    public List<TouristAttractionDTO> getAttractionsLike(String email) {

        List<LikeAttraction> userAttractionLikes = likeService.getUserLikes(email);

        List<TouristAttractionDTO> touristAttractions = new ArrayList<>();

        for (LikeAttraction like : userAttractionLikes){
            TouristAttractionDTO dto = new TouristAttractionDTO();
            dto.setAddress(like.getAttraction().getAddress());
            dto.setTouristAttractionId(like.getAttraction().getTouristAttractionId());
            dto.setCityName(like.getAttraction().getCityName().getCityName());
            dto.setIntroduction(like.getAttraction().getIntroduction());
            dto.setImage(like.getAttraction().getImage());
            dto.setLatitude(like.getAttraction().getLatitude());
            dto.setLongitude(like.getAttraction().getLongitude());
            dto.setContentType(like.getAttraction().getContentType());
            dto.setName(like.getAttraction().getName());
            touristAttractions.add(dto);
        }

        return touristAttractions;
    }

    @Override
    public List<TouristAttraction> getTop5AttractioinByLike() {
        Pageable pageable = PageRequest.of(0,5);
        List<TouristAttraction> touristAttractionList = likeRepository.findTopAttractions(pageable);
        return touristAttractionList;
    }

}
