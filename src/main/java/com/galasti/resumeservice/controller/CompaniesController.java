package com.galasti.resumeservice.controller;

import com.galasti.resumeservice.dto.Company;
import com.galasti.resumeservice.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@AllArgsConstructor
public class CompaniesController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> getCompanies() {
        return ResponseEntity.ok(companyService.getCompanies());
    }

    @GetMapping("/{company}/logo")
    public ResponseEntity<byte[]> getLogo(@PathVariable("company") final String company) {

        final byte[] image = companyService.getLogo(company);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);

    }
}
