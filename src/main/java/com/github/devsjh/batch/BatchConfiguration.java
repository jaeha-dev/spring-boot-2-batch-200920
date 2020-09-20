package com.github.devsjh.batch;

import com.github.devsjh.model.Employee;
import com.github.devsjh.model.Profile;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing // 배치 활성화
@EnableScheduling // 스케줄러 활성화
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean // 파일을 읽기 위한 Reader
    public FlatFileItemReader<Employee> reader() {
        // JdbcCursorItemReader, JdbcPagingItemReader도 있다.
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeItemReader") // 이름
                .resource(new ClassPathResource("sample-employee.csv")) // CSV 파일 이름
                .delimited()
                .names("employeeCode", "employeeName", "expirationInYears")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                    setTargetType(Employee.class);
                }})
                .linesToSkip(1)
                .build();
    }

    @Bean // 데이터를 처리하기 위한 Processor
    public ItemProcessor<Employee, Profile> processor() {
        return new EmployeeItemProcessor();
    }

    @Bean // DB 또는 파일에 쓰기 위한 Writer
    public JdbcBatchItemWriter<Profile> writer(DataSource dataSource) {
        // FlatFileItemWriter도 있다.
        return new JdbcBatchItemWriterBuilder<Profile>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO profile (employeeCode, employeeName, profileName) VALUES (:employeeCode, :employeeName, :profileName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job createEmployeeJob(EmployeeJobExecutionListener listener, Step step1) {
        return jobBuilderFactory
                .get("creatEmployeeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(ItemReader<Employee> reader,
                      ItemWriter<Profile> writer,
                      ItemProcessor<Employee, Profile> processor) {
        return stepBuilderFactory
                .get("step1")
                // 한 번에 하나씩 데이터를 읽어 Chunk를 만든 뒤, Chunk 단위로 트랜잭션을 수행한다.
                // Chunk 단위로 Commit or Rollback된다.
                .<Employee, Profile> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public DataSource dataSource() {
        // h2 console 실행 후, http://localhost:8082 열기
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:tcp://localhost/~/test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}