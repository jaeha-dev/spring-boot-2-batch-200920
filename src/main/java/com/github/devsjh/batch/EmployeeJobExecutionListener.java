package com.github.devsjh.batch;

import com.github.devsjh.model.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class EmployeeJobExecutionListener implements JobExecutionListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // 배치 작업 전, 실행된다.
        log.info("=== [BeforeJob] Executing job id: " + jobExecution.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // 배치 작업 후, 실행된다.
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            List<Profile> results = jdbcTemplate.query("SELECT employeeCode, employeeName, profileName FROM profile",
                    new RowMapper<Profile>() {
                        @Override
                        public Profile mapRow(ResultSet rs, int i) throws SQLException {
                            return new Profile(rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3)
                            );
                        }
                    });

            log.info("=== [AfterJob] Number of Records: " + results.size());
        }
    }
}