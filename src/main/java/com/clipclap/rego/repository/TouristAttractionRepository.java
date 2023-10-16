package com.clipclap.rego.repository;

import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TouristAttractionRepository extends JpaRepository<TouristAttraction, Integer> {

    List<TouristAttraction> findByCityName(City cityName);

    @Query("SELECT new com.clipclap.rego.model.dto.TouristAttractionFullDTO(" +
            "ta.touristAttractionId, ta.address, ta.name, ta.image, ta.introduction, " +
            "ta.latitude, ta.longitude, ta.contentType, c.cityName, co.countryName) " +
            "FROM TouristAttraction ta " +
            "JOIN ta.cityName c " +
            "JOIN c.countryName co")
    List<TouristAttractionFullDTO> getTouristAttractionsWithCityAndCountry();


    @Query("SELECT DISTINCT t.cityName.cityName FROM TouristAttraction t WHERE t.cityName.cityName != '-' ")
    List<String> findDistinctCityNames();

    List<TouristAttraction> findByCityNameAndContentType(City cityName, String contentType);

    List<TouristAttraction> findByLatitudeIsNotNullAndLongitudeIsNotNull();



}
