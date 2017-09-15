package com.theironyard.invoicify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

@Controller
@RequestMapping("/billing-records")
public class BillingRecordController {
	
	private BillingRecordRepository recordRepository;
	private CompanyRepository companyRepo;

	public BillingRecordController(BillingRecordRepository recordRepository, CompanyRepository companyRepo) {
		this.recordRepository = recordRepository;
		this.companyRepo = companyRepo;
	}

	@GetMapping("")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("billing-records/list");
		mv.addObject("records", recordRepository.findAll());
		mv.addObject("companies", companyRepo.findAll());
		return mv;
	}
	
}
