package com.github.devsjh.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile {

    // DB에 Profile 정보를 저장하기 위한 DTO

    private String employeeCode;
    private String employeeName;
    private String profileName;

    public Profile() { }

    public Profile(String employeeCode, String employeeName, String profileName) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.profileName = profileName;
    }
}