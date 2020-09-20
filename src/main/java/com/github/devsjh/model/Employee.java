package com.github.devsjh.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {

    // CSV 파일의 Employee 정보를 읽어 오기 위한 DTO

    private String employeeCode;
    private String employeeName;
    private int expirationInYears;

    public Employee() { }

    public Employee(String employeeCode, String employeeName, int expirationInYears) {
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.expirationInYears = expirationInYears;
    }
}