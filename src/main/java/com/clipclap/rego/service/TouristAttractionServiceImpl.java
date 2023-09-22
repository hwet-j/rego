package com.clipclap.rego.service;

import com.clipclap.rego.mapper.TouristAttractionMapper;
import com.clipclap.rego.model.dto.TouristAttractionDTO;
import com.clipclap.rego.model.dto.TouristAttractionFullDTO;
import com.clipclap.rego.model.entitiy.City;
import com.clipclap.rego.model.entitiy.TouristAttraction;
import com.clipclap.rego.repository.TouristAttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TouristAttractionServiceImpl implements TouristAttractionService {

    private final TouristAttractionRepository touristAttractionRepository;

    @Override
    public List<TouristAttractionDTO> touristListTest() {
        List<TouristAttraction>  touristAttractions = touristAttractionRepository.findAll();

        List<TouristAttractionDTO> touristAttractionDTOs = touristAttractions.stream()
                .map(TouristAttractionMapper::entityToDto) // Entity를 DTO로 변환
                .collect(Collectors.toList());

        return touristAttractionDTOs;
    }


    @Override
    public List<TouristAttractionDTO> coutryAttractionList(City cityName) {

        List<TouristAttraction>  touristAttractions = touristAttractionRepository.findByCityName(cityName);

        List<TouristAttractionDTO> touristAttractionDTOs = touristAttractions.stream()
                .map(TouristAttractionMapper::entityToDto) // Entity를 DTO로 변환
                .collect(Collectors.toList());

        return touristAttractionDTOs;
    }

    @Override
    public List<TouristAttractionFullDTO> getTouristAttractionsWithCityAndCountry() {

        List<TouristAttractionFullDTO> touristAttractions = touristAttractionRepository.getTouristAttractionsWithCityAndCountry();

        return touristAttractions;
    }

}
