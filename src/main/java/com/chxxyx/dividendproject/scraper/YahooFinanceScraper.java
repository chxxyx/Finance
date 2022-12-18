package com.chxxyx.dividendproject.scraper;

import com.chxxyx.dividendproject.model.Company;
import com.chxxyx.dividendproject.model.Dividend;
import com.chxxyx.dividendproject.model.ScrapedResult;
import com.chxxyx.dividendproject.model.constants.Month;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class YahooFinanceScraper implements Scraper{

	private static final  String STATIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
	private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
	private static final long START_TIME = 86400; // 60 * 60 * 24

	@Override
	public ScrapedResult scrap(Company company) {

		var scrapResult = new ScrapedResult();
		scrapResult.setCompany(company);

		try{
			long now = System.currentTimeMillis() / 1000;

			String url = String.format(STATIC_URL, company.getTicker(), START_TIME, now);
			Connection connection = Jsoup.connect(url);
			Document document = connection.get();

			Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element tableEle = parsingDivs.get(0);

			Element tbody = tableEle.children().get(1);
			List<Dividend> dividends = new ArrayList<>();

			for(Element e: tbody.children()) {
				String txt = e.text();
				if(!txt.endsWith("Dividend")) {
					continue;
				}
				String[] splits = txt.split(" ");
				int month = Month.strToNumber(splits[0]);
				int day = Integer.valueOf(splits[1].replace(",", ""));
				int year = Integer.valueOf(splits[2]);
				String dividend = splits[3];

				if (month < 0) {
					throw  new RuntimeException("Unexpected Month enum value -> " + splits[0]);
				}

				dividends.add(Dividend.builder()
									.date(LocalDateTime.of(year, month, day, 0, 0))
									.dividend(dividend)
									.build());
			}
			scrapResult.setDividends(dividends);

		}catch(IOException e) {
			e.printStackTrace();
		}

		return scrapResult;
	}

	/**
	 	회사의 메타 정보를 ticker명 코드를 받아서 ticker에 해당하는 회사의 메타 정보를 반환하는 메소드
	 */
	@Override
	public Company scrapCompanyByTicker(String ticker) {
		String url = String.format(SUMMARY_URL, ticker, ticker);

		try {
			Document document = Jsoup.connect(url).get();
			Element titleEle = document.getElementsByTag("h1").get(0);
			String title = titleEle.text().split(" - ")[1].trim(); // - 를 기준으로 문자열이 쪼개진다

			return Company.builder()
							.ticker(ticker)
							.name(title)
							.build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
