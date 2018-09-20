CREATE TABLE STORE_COMPANY_CODE (company_code number(10) NOT NULL,  
  company_description varchar2(50),  
  currency_code varchar2(50),
  payment_integration varchar2(50),
  warehouse_number varchar2(50)  
);

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(1, 'Holland & Barrett UK','GBP','Ingenico','10002');

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(2, 'Holland & Barrett IE','EUR','Ingenico','30001');

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(3, 'Holland & Barrett NL','EUR','Adyen','20002');

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(4, 'Holland & Barrett BE','EUR','Adyen','11111');

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(5, 'Holland & Barrett SW','EUR','Ingenico','22222');

insert into STORE_COMPANY_CODE (company_code,company_description,
currency_code,payment_integration,warehouse_number)VALUES  
(6, 'MET-Rx','','Adyen','');
