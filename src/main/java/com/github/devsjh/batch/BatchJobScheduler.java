package com.github.devsjh.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchJobScheduler {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 3000) // 3초마다 배치 작업이 호출된다.
    public void runBatchJob() {
        JobParameters params = new JobParametersBuilder()
                .addLong("=== jobId", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(job, params);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("=== JobInstanceAlreadyCompleteException");
        } catch (JobRestartException e) {
            log.info("=== JobRestartException");
        } catch (JobParametersInvalidException e) {
            log.info("=== JobParametersInvalidException");
        } catch (JobExecutionAlreadyRunningException e) {
            log.info("=== JobExecutionAlreadyRunningException");
        }
    }
}