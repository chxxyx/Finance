package com.chxxyx.dividendproject.scraper;

import com.chxxyx.dividendproject.model.Company;
import com.chxxyx.dividendproject.model.ScrapedResult;

public interface Scraper {

	Company scrapCompanyByTicker(String ticker);
	ScrapedResult scrap(Company company);
}
