package com.dividend.dividend.scraper;

import com.dividend.dividend.model.Company;
import com.dividend.dividend.model.Dividend;
import com.dividend.dividend.model.ScrapedResult;
import com.dividend.dividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s";
    private static final long START_TIME = 86400L;

    @Override
    public ScrapedResult scrap(Company company) {
        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        long end = System.currentTimeMillis() / 1000;
        String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, end);


        Connection connection = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/122.0.0.0 Safari/537.36")
                .timeout(10 * 1000);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Elements parsingDivs = document.select("table.table.yf-1jecxey.noDl.hideOnPrint");
        Element tableEle = parsingDivs.get(0);

        Element tbody = tableEle.children().get(1);

        List<Dividend> dividends = new ArrayList<>();
        for (Element e : tbody.children()) {
            String txt = e.text();
            if (!txt.endsWith("Dividend")){
                continue;
            }

            String[] splits = txt.split(" ");
            int month = Month.strToNumber(splits[0]);
            int day = Integer.valueOf(splits[1].replace(",",""));
            int year = Integer.valueOf(splits[2]);
            String dividend = splits[3];

            if (month < 0){
                throw new RuntimeException("Unxepected Month enum value -> " + splits[0]);
            }

            dividends.add(
                    new Dividend(LocalDateTime.of(year, month, day, 0 , 0),dividend)
            );

        }
        scrapedResult.setDividends(dividends);
        return scrapedResult;
    }


    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker);

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/114.0.5735.133 Safari/537.36")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element titleEle = document.select("head title").first();
        String title = titleEle.text().split(" \\(")[0].trim();
        return new Company(ticker,title);
    }
}
