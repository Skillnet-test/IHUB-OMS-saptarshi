package com.oms.companycode.data;

import java.io.Serializable;

public class CompanyCodeData implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private int company_code;    
	
	private String company_description;

    private String currency_code;

    private String payment_integration;

    private String warehouse_number;

    
	
    public int getCompany_code() {
		return company_code;
	}

	public void setCompany_code(int company_code) {
		this.company_code = company_code;
	}

	public String getCompany_description() {
		return company_description;
	}

	public void setCompany_description(String company_description) {
		this.company_description = company_description;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getPayment_integration() {
		return payment_integration;
	}

	public void setPayment_integration(String payment_integration) {
		this.payment_integration = payment_integration;
	}

	public String getWarehouse_number() {
		return warehouse_number;
	}

	public void setWarehouse_number(String warehouse_number) {
		this.warehouse_number = warehouse_number;
	}

	@Override
	public String toString() {
		return "CompanyCodeData [company_code=" + company_code
				+ ", company_description=" + company_description
				+ ", currency_code=" + currency_code + ", payment_integration="
				+ payment_integration + ", warehouse_number="
				+ warehouse_number + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + company_code;
		result = prime
				* result
				+ ((company_description == null) ? 0 : company_description
						.hashCode());
		result = prime * result
				+ ((currency_code == null) ? 0 : currency_code.hashCode());
		result = prime
				* result
				+ ((payment_integration == null) ? 0 : payment_integration
						.hashCode());
		result = prime
				* result
				+ ((warehouse_number == null) ? 0 : warehouse_number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyCodeData other = (CompanyCodeData) obj;
		if (company_code != other.company_code)
			return false;
		if (company_description == null) {
			if (other.company_description != null)
				return false;
		} else if (!company_description.equals(other.company_description))
			return false;
		if (currency_code == null) {
			if (other.currency_code != null)
				return false;
		} else if (!currency_code.equals(other.currency_code))
			return false;
		if (payment_integration == null) {
			if (other.payment_integration != null)
				return false;
		} else if (!payment_integration.equals(other.payment_integration))
			return false;
		if (warehouse_number == null) {
			if (other.warehouse_number != null)
				return false;
		} else if (!warehouse_number.equals(other.warehouse_number))
			return false;
		return true;
	}

	
	

}
