package com.chxxyx.dividendproject.web;

import com.chxxyx.dividendproject.model.Company;
import com.chxxyx.dividendproject.model.constants.CacheKey;
import com.chxxyx.dividendproject.persist.entity.CompanyEntity;
import com.chxxyx.dividendproject.service.CompanyService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CompanyController {

	private final CompanyService companyService;
	private final CacheManager redisCacheManager;

	@GetMapping("/company/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
		var result = this.companyService.getCompanyNamesByKeyword(keyword);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/company")
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<?> searchCompany(final Pageable pageable) {
		Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
		return ResponseEntity.ok(companies);
	}

	@PostMapping("/company")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> addCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();
		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = this.companyService.save(ticker);
		this.companyService.addAutocompleteKeyword(company.getName());

		return ResponseEntity.ok(company);
	}

	@DeleteMapping("/company/{ticker}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
		String companyName = this.companyService.deleteCompany(ticker);
		this.clearFinanceCache(companyName);
		return ResponseEntity.ok(companyName);
	}

	// 캐시 삭제
	public void clearFinanceCache(String companyName) {
		this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
	}
}
