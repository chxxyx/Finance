package com.chxxyx.dividendproject.service;

import com.chxxyx.dividendproject.exception.impl.NoCompanyException;
import com.chxxyx.dividendproject.model.Company;
import com.chxxyx.dividendproject.model.Dividend;
import com.chxxyx.dividendproject.model.ScrapedResult;
import com.chxxyx.dividendproject.model.constants.CacheKey;
import com.chxxyx.dividendproject.persist.CompanyRepository;
import com.chxxyx.dividendproject.persist.DividendRepository;
import com.chxxyx.dividendproject.persist.entity.CompanyEntity;
import com.chxxyx.dividendproject.persist.entity.DividendEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	@Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
	public ScrapedResult getDividendByCompanyName(String companyName) {

		log.info("serach company -> " + companyName);

		// 회사 명을 기준으로 회사 정보를 조회
		CompanyEntity company = this.companyRepository.findByName(companyName)
												.orElseThrow(() -> new NoCompanyException());

		// 조회된 회사 아이디로 배당금 조회
		List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

		// 조회된 회사 정보, 배당금 정보를 조합해서 ScrapedResult로 반환
		List<Dividend> dividends = dividendEntities.stream()
			.map(e -> new Dividend(e.getDate(), e.getDividend()))
			.collect(Collectors.toList());


		return new ScrapedResult(new Company(company.getTicker(), company.getName()),
								dividends);
	}
}
