package com.galasti.resumeservice.config;

import com.galasti.resumeservice.persistence.CompanyRepository;
import com.galasti.resumeservice.persistence.CompanyRepositoryFileSystemImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class ServiceConfig {

    @Bean
    public CompanyRepository companyRepository(@Value("${companies.dir:}")String dataDir, ObjectMapper objectMapper) {
        return new CompanyRepositoryFileSystemImpl(dataDir, objectMapper);
    }
}
