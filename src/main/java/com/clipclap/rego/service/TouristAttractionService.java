package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;

import java.util.List;

public interface TouristAttractionService {

    // 모든 관광지 리스트
    List<TouristAttractionDTO> touristListAll();

    // 특정 도시에 있는 모든 관광지 리스트
    List<TouristAttractionDTO> countryAttractionList(City cityName);

    // 특정 도시에 타입으로 검색된 모든 관광지 리스트
    List<TouristAttractionDTO> cityContentTypeList(City cityName, String contentType);

    // 도시, 나라를 포함한 모든 관광지 리스트
    List<TouristAttractionFullDTO> getTouristAttractionsWithCityAndCountry();
}
