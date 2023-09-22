package com.clipclap.rego.service;

import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;

import java.util.List;

public interface TouristAttractionService {

    // Test
    List<TouristAttractionDTO> touristListTest();

    List<TouristAttractionDTO> coutryAttractionList(City cityName);

    List<TouristAttractionFullDTO> getTouristAttractionsWithCityAndCountry();
}
