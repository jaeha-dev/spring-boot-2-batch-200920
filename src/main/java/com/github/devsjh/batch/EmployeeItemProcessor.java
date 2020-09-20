package com.github.devsjh.batch;

import com.github.devsjh.model.Employee;
import com.github.devsjh.model.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class EmployeeItemProcessor implements ItemProcessor<Employee, Profile> {

    @Override
    public Profile process(final Employee employee) throws Exception {
        String profileName = "";

        // Employee 데이터(CSV)를 Profile 데이터로 변환한다. (Profile 데이터는 DB에 저장한다.)
        if (employee.getExpirationInYears() < 5) {
            profileName = "Developer";
        } else if (employee.getExpirationInYears() >= 5 && employee.getExpirationInYears() <= 10) {
            profileName = "Team Leader";
        } else if (employee.getExpirationInYears() > 10) {
            profileName = "Project Manager";
        }

        Profile profile = Profile.builder()
                .employeeCode(employee.getEmployeeCode())
                .employeeName(employee.getEmployeeName())
                .profileName(profileName)
                .build();

        log.info("=== Employee Code: " + profile.getEmployeeCode());
        log.info("=== Employee Name: " + profile.getEmployeeName());
        log.info("=== Profile Code: " + profile.getProfileName());

        return profile;
    }
}
