package com.theironyard.invoicify.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.FlatFeeBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;

public class FlatFeeBillingRecordControllerTests {
	private FlatFeeBillingRecordController controller;
	private BillingRecordRepository recordRepo;
	private CompanyRepository companyRepo;
	private Authentication auth;
	
	@Before
	public void setup()	{
		auth = mock(Authentication.class);
		recordRepo = mock(BillingRecordRepository.class);
		companyRepo = mock(CompanyRepository.class);
		controller = new FlatFeeBillingRecordController(recordRepo, companyRepo);
	}

	@Test
	public void test_create_saves_new_FFBR_with_company_passed_in_and_redirects()	{
		FlatFeeBillingRecord ffbr = new FlatFeeBillingRecord();
		Company company = new Company();
		User user = new User();
		when(companyRepo.findOne(1L)).thenReturn(company);
		when(auth.getPrincipal()).thenReturn(user);
		when(recordRepo.save(ffbr)).thenReturn(ffbr);
		
		ModelAndView mvReturned = controller.create(ffbr, 1L, auth);
		
		verify(companyRepo).findOne(1L);
		verify(auth).getPrincipal();
		verify(recordRepo).save(ffbr);
		assertThat(mvReturned.getViewName()).isEqualTo("redirect:/billing-records");

	}

}
