package com.clipclap.rego.repository;

import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// JPA
public interface CountryRepository extends JpaRepository<Country,String> {

    @Query("SELECT DISTINCT c.countryName FROM Country c")
    List<String> findAllDistinctCountryNames();
}
