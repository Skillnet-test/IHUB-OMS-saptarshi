package com.oms.companycode.dao;

import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oms.companycode.data.CompanyCodeData;
//import org.apache.log4j.Logger;

@Component
public class CompanyCodeManager {
	
	
	@Autowired
    Messages messages;
		
	@Autowired
    CompanyCodeDao companyDao;	
	
	Hashtable hTableCompanyCode=null;
	
	private static final Logger logger = Logger.getLogger(CompanyCodeManager.class.getName());
	
	/*@Value("#{field1}")
	public String message;
	*/
	//CompanyCodeData companyCodeData;
	
	
	
	public CompanyCodeManager(){
		
	}
	
	

	public void getCompanyCode()
    {
		
		
		List<CompanyCodeData> companyDataList = companyDao.getDataAsList();
		hTableCompanyCode = new Hashtable();
		if(companyDataList!=null){
		
		logger.info("getting full list of data in company code manager"+companyDataList);
		//putting all company code data in hash table
		
		for (int i = 0; i < companyDataList.size(); i++) {
			
			
			hTableCompanyCode.put(companyDataList.get(i).getCompany_code(), companyDataList.get(i));
			
		}
		
		
		
		}
    	
    }
	
	// for getting company description
	public String getCompanyDescription(Integer comCode){
		
			
		if(hTableCompanyCode.get(comCode)instanceof CompanyCodeData && hTableCompanyCode.size()>0){
			return ((CompanyCodeData)hTableCompanyCode.get(comCode)).getCompany_description();
		}else
		
		return messages.get("default.title");
		
	}
	
	// for getting company currency code
	public String getCurrencyCode(Integer comCode){
		
		if(hTableCompanyCode.get(comCode)instanceof CompanyCodeData && hTableCompanyCode.size()>0){
			return ((CompanyCodeData)hTableCompanyCode.get(comCode)).getCurrency_code();
		}else
		
			return messages.get("default.title");
	}
	
	// for getting payment integration
	public String getPaymentIntegration(Integer comCode){
		
		if(hTableCompanyCode.get(comCode)instanceof CompanyCodeData && hTableCompanyCode.size()>0){
			return ((CompanyCodeData)hTableCompanyCode.get(comCode)).getPayment_integration();
		}else
		
			return messages.get("default.title");
		
	}
	
	// for getting ware house number
	public String getWareHouseNumber(Integer comCode){
		
		if(hTableCompanyCode.get(comCode)instanceof CompanyCodeData && hTableCompanyCode.size()>0){
			return ((CompanyCodeData)hTableCompanyCode.get(comCode)).getWarehouse_number();
		}else
		
			return messages.get("default.title");
	}
	
	

}
