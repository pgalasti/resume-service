package com.galasti.resumeservice.service;

import com.galasti.resumeservice.dto.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getCompanies();

    byte[] getLogo(String company);
}
