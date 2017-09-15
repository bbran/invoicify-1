package com.theironyard.invoicify.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.theironyard.invoicify.models.BillingRecord;

public interface BillingRecordRepository extends JpaRepository<BillingRecord, Long> {
	
	public ArrayList<BillingRecord> findByClientIdEqualsAndLineItemIsNull(long clientId);
	
	public ArrayList<BillingRecord> findByIdIn(long[] recordIds);

}
