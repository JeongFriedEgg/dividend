package com.dividend.dividend.scraper;

import com.dividend.dividend.model.Company;
import com.dividend.dividend.model.ScrapedResult;

public interface Scraper {
    ScrapedResult scrap(Company company);
    Company scrapCompanyByTicker(String ticker);
}
