package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JPA
public interface CityRepository extends JpaRepository<City,Integer> {

    @Query("SELECT DISTINCT c.cityName FROM City c")
    List<String> findAllDistinctCityNames();

    @Query("SELECT DISTINCT c.cityName FROM City c WHERE c.countryName.countryName = :countryName")
    List<String> findDistinctCityNamesByCountryName(@Param("countryName") String countryName);
}
