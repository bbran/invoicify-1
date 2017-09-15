package com.theironyard.invoicify.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.RateBasedBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

public class RateBasedBillingRecordControllerTests {
	private RateBasedBillingRecordController controller;
	private BillingRecordRepository recordRepo;
	private CompanyRepository companyRepo;
	private Authentication auth;
	
	@Before
	public void setup()	{
		auth = mock(Authentication.class);
		recordRepo = mock(BillingRecordRepository.class);
		companyRepo = mock(CompanyRepository.class);
		controller = new RateBasedBillingRecordController(recordRepo, companyRepo);
	}

	@Test
	public void test_create_saves_new_FFBR_with_company_passed_in_and_redirects()	{
		RateBasedBillingRecord rbbr = new RateBasedBillingRecord();
		Company company = new Company();
		User user = new User();
		when(companyRepo.findOne(1L)).thenReturn(company);
		when(auth.getPrincipal()).thenReturn(user);
		when(recordRepo.save(rbbr)).thenReturn(rbbr);
		
		ModelAndView mvReturned = controller.create(rbbr, 1L, auth);
		
		verify(companyRepo).findOne(1L);
		verify(auth).getPrincipal();
		verify(recordRepo).save(rbbr);
		assertThat(mvReturned.getViewName()).isEqualTo("redirect:/billing-records");

	}

}