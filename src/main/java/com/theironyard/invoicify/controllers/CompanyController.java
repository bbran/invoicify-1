package com.theironyard.invoicify.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/admin/companies")
public class CompanyController {
	private CompanyRepository companyRepo;
	private InvoiceRepository invoiceRepo;
	
	public CompanyController(CompanyRepository companyRepo, InvoiceRepository invoiceRepo)	{
		this.companyRepo = companyRepo;
		this.invoiceRepo = invoiceRepo;
	}
	
	@GetMapping("")
	public ModelAndView list()	{
		ModelAndView modelAndView = new ModelAndView("companies/list");
		modelAndView.addObject("companies", companyRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
		return modelAndView;
	}
	
	@PostMapping("")
	public String create(String name)	{
		Company company = new Company();
		company.setName(name);
		companyRepo.save(company);
		return "redirect:/admin/companies";
	}
	
}
