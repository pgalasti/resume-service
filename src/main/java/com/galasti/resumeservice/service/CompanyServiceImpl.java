package com.galasti.resumeservice.service;

import com.galasti.resumeservice.dto.Company;
import com.galasti.resumeservice.persistence.CompanyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> getCompanies() {
        return this.companyRepository.findAllCompanies();
    }

    @Override
    public byte[] getLogo(String companyId) {
        return companyRepository.getLogo(companyId);
    }


}
