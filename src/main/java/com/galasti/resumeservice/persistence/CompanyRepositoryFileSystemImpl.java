package com.galasti.resumeservice.persistence;

import com.galasti.resumeservice.dto.Company;
import com.galasti.resumeservice.exception.CompanyDataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@Slf4j
public class CompanyRepositoryFileSystemImpl implements CompanyRepository {

    private final Path baseDir;
    private final ObjectMapper objectMapper;

    public CompanyRepositoryFileSystemImpl(@Value("${companies.dir:}") String dataDir, ObjectMapper objectMapper) {
        this.baseDir = this.resolvePath((dataDir));
        this.objectMapper = objectMapper;
    }

    /*
        Since this is a small amount of data, I'm just deriving all of my look-ups off findAllCompanies()
        and filtering through. If I used a relation datastore, the implementation would be more straight forward
        against the datastore.
    */

    @Override
    public Optional<Company> findCompanyByName(final String name) {
        final List<Company> companies = this.findAllCompanies();
        return companies.stream()
                .filter(company -> String.CASE_INSENSITIVE_ORDER.compare(company.getName(), name) == 0)
                .findFirst();
    }

    @Override
    public List<Company> findAllCompanies() {

        if(!Files.exists(baseDir)) {
            return new ArrayList<>();
        }

        try (Stream<Path> children = Files.list(baseDir)) {
            return children
                    .filter(Files::isDirectory)
                    .map(dir -> dir.resolve("company.json"))
                    .filter(Files::isRegularFile)
                    .map(this::safeReadCompany)
                    .flatMap(Optional::stream)
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public Optional<Company> findCompanyById(final String id) {
        final List<Company> companies = this.findAllCompanies();
        return companies.stream()
                .filter(company -> String.CASE_INSENSITIVE_ORDER.compare(company.getId(), id) == 0)
                .findFirst();
    }

    @Override
    public byte[] getLogo(String id) {
        Path logoPath = this.baseDir.resolve(id).resolve("logo.png").normalize();
        if(!Files.exists(logoPath)) {
            throw new CompanyDataNotFoundException("Unable to find company logo for " + id);
        }

        try {
            return Files.readAllBytes(logoPath);
        } catch (IOException e) {
            throw new CompanyDataNotFoundException("Unable to read company logo for " + id, e);
        }
    }

    private Path resolvePath(String baseDirectory) {
        try {
            Path dir = Paths.get(baseDirectory).toAbsolutePath();
            Path companiesPath = dir.resolve("companies");

            if (!Files.exists(companiesPath)) {
                log.error("Companies directory does not exist: {}", companiesPath);
                throw new CompanyDataNotFoundException(
                        String.format("Companies directory does not exist: %s", companiesPath)
                );
            }

            return companiesPath.normalize();
        } catch (Exception e) {
            throw new CompanyDataNotFoundException("Failed to resolve companies directory", e);
        }
    }

    private Optional<Company> safeReadCompany(Path jsonPath) {
        try {
            Company c = objectMapper.readValue(jsonPath.toFile(), Company.class);

            String folderName = jsonPath.getParent().getFileName().toString();
            if (c.getId() == null || c.getId().isBlank()) {
                c = Company.builder()
                        .id(folderName)
                        .name(c.getName())
                        .build();
            }
            return Optional.of(c);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
