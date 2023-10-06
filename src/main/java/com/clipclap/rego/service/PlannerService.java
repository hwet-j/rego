package com.clipclap.rego.service;

import java.time.LocalDate;

public interface PlannerService {

    LocalDate findStartTimeByPlanId(Integer planId);

}
