package com.clipclap.rego.model.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlannerDTO {

    private Integer planId;

    private String userEmail;

    @NotBlank(message = "주제를 작성해주세요.")
    private String content;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Min(1)
    private int numberOfPeople;

    @NotBlank
    private String type;

    // startDate가 endDate보다 이전인지 검사하는 메서드
    @AssertTrue(message = "종료 날짜는 시작 날짜보다 앞설수없습니다.")
    public boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }

}
