package com.chxxyx.dividendproject.web;

import com.chxxyx.dividendproject.model.Company;
import com.chxxyx.dividendproject.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping("/company/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
		return null;
	}

	@GetMapping("/company")
	public ResponseEntity<?> searchCompany() {
		return null;
	}

	@PostMapping("/company")
	public ResponseEntity<?> addCompany(@RequestBody Company request) {
		String ticker = request.getTicker().trim();
		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = this.companyService.save(ticker);

		return ResponseEntity.ok(company);
	}

	@DeleteMapping("/company")
	public ResponseEntity<?> deleteCompany() {
		return null;
	}
}
