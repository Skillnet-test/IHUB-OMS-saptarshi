package com.oms.companycode.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oms.companycode.constants.CompanyCodeConstantIfc;
import com.oms.companycode.data.CompanyCodeData;


@Repository
public class CompanyCodeDao implements CompanyCodeConstantIfc {
	
	List<CompanyCodeData> companycodeList = null;
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final Logger logger = Logger.getLogger(CompanyCodeDao.class);
    
    
   
    @Transactional(readOnly = true)
    public void  getcompanyCodeData()
    {
        
        companycodeList = jdbcTemplate.query(SQL_READ_ALL_COMPANY_CODE, new BeanPropertyRowMapper(CompanyCodeData.class));
        logger.info("Getting data of company code at start up"+companycodeList);
        
        
    }
    
    public List<CompanyCodeData> getDataAsList(){
    	
    	return companycodeList;
    	
    }  

}
