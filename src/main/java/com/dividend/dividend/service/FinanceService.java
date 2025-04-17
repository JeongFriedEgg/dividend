package com.dividend.dividend.service;

import com.dividend.dividend.exception.impl.NoCompanyException;
import com.dividend.dividend.model.Company;
import com.dividend.dividend.model.Dividend;
import com.dividend.dividend.model.ScrapedResult;
import com.dividend.dividend.persist.CompanyRepository;
import com.dividend.dividend.persist.DividendRepository;
import com.dividend.dividend.persist.entity.CompanyEntity;
import com.dividend.dividend.persist.entity.DividendEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);

        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(NoCompanyException::new);

        List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(company.getId());

        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .toList();

        return new ScrapedResult(
                new Company(company.getTicker(), company.getName()),dividends
        );
    }
}
