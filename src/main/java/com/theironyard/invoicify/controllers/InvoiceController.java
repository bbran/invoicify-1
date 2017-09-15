package com.theironyard.invoicify.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.BillingRecord;
import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.Invoice;
import com.theironyard.invoicify.models.InvoiceLineItem;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
	private CompanyRepository companyRepo;
	private BillingRecordRepository recordRepo;
	private InvoiceRepository invoiceRepo;
	
	public InvoiceController(CompanyRepository companyRepo, BillingRecordRepository recordRepo, InvoiceRepository invoiceRepo)	{
		this.companyRepo = companyRepo;
		this.recordRepo = recordRepo;
		this.invoiceRepo = invoiceRepo;
		
	}

	@GetMapping("")
	public ModelAndView list(Authentication auth) {
		User user = (User) auth.getPrincipal();
		ModelAndView mv = new ModelAndView("invoices/list");
		mv.addObject("invoices", invoiceRepo.findAll());
		mv.addObject("user", user);
		return mv;
	}
	
	@GetMapping("new")
	public ModelAndView selectInvoiceCompany()	{
		ModelAndView mv = new ModelAndView("invoices/step-1");
		mv.addObject("companies", companyRepo.findAll());
		return mv;
	}
	
	@PostMapping("new")
	public ModelAndView goToBillingRecordSelect(long clientId)	{
		ModelAndView mv = new ModelAndView("invoices/step-2");
		mv.addObject("clientId", clientId);
		mv.addObject("selectedCompany", companyRepo.findOne(clientId));
		mv.addObject("billingRecords", recordRepo.findByClientIdEqualsAndLineItemIsNull(clientId));
		return mv;
	}
	
	@PostMapping("create")
	public ModelAndView createInvoice(Invoice invoice, long clientId, long[] recordIds, Authentication auth)	{
		ModelAndView mv = new ModelAndView();
		try	{
			List<BillingRecord> records = recordRepo.findByIdIn(recordIds);
			Company company = companyRepo.findOne(clientId);
			User creator = (User) auth.getPrincipal();
			long nowish = Calendar.getInstance().getTimeInMillis();
			Date now = new Date(nowish);
			
			List<InvoiceLineItem> items = new ArrayList<InvoiceLineItem>();
			for (BillingRecord record: records)	{
				InvoiceLineItem item = new InvoiceLineItem();
				item.setBillingRecord(record);
				item.setCreatedBy(creator);
				item.setCreatedOn(now);
				item.setInvoice(invoice);
				items.add(item);
			}
			invoice.setLineItems(items);
			invoice.setCompany(company);
			invoice.setCreatedOn(now);
			invoice.setCreatedBy(creator);
			invoiceRepo.save(invoice);
			mv.setViewName("redirect:/invoices");
		} catch (InvalidDataAccessApiUsageException idaaue) {
			mv.setViewName("invoices/step-2");
			mv.addObject("errorMessage", "Please select at least one billing record.");
			mv.addObject("clientId", clientId);
			mv.addObject("selectedCompany", companyRepo.findOne(clientId));
			mv.addObject("billingRecords", recordRepo.findByClientIdEqualsAndLineItemIsNull(clientId));
		}
		
		return mv;
	}
	
}
