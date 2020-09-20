package com.github.devsjh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @title  : 스프링 배치 연습
 * @author : jaeha-dev (Git)
 * @refer  : https://www.concretepage.com/spring-5/spring-batch-h2-database (참고 코드)
 *           https://spring.io/guides/gs/batch-processing (공식 문서)
 *           https://github.com/spring-guides/gs-batch-processing (공식 문서)
 *           https://jeong-pro.tistory.com/186 (스케줄러)
 */
@SpringBootApplication
public class DevsjhApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevsjhApplication.class, args);

        // 배치 후 종료되도록 한다.
        // System.exit(SpringApplication.exit(SpringApplication.run(DevsjhApplication.class, args)));
    }
}