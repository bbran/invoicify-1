package com.theironyard.invoicify.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.UserRepository;

@ControllerAdvice
public class UserInformationAdvice {
	
	@ModelAttribute
	public void addUserToModel(Model model, Authentication auth, HttpServletRequest request)	{
		if	(auth == null)	{
			model.addAttribute("notAUser", true);
		} else	{
		model.addAttribute("user", auth.getPrincipal());
		model.addAttribute("showBilling", request.isUserInRole("ADMIN") || request.isUserInRole("CLERK"));
		model.addAttribute("showInvoices", request.isUserInRole("ADMIN") || request.isUserInRole("ACCOUNTANT"));
		model.addAttribute("showAdmin", request.isUserInRole("ADMIN"));
		}
	}
	
}
