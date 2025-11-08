package com.galasti.resumeservice.persistence;

import com.galasti.resumeservice.dto.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    Optional<Company> findCompanyById(String id);

    Optional<Company> findCompanyByName(String name);

    List<Company> findAllCompanies();

    byte[] getLogo(String id);
}
