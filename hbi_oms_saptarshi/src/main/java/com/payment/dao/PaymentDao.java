package com.payment.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payment.constant.PaymentConstantIfc;
import com.payment.data.PaymentData;
import com.payment.formatter.PaymentResponse;

@Repository
public class PaymentDao implements PaymentConstantIfc
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<PaymentData> getPaymentDatabyStatus()
    {
        List<PaymentData> paymentList = null;
        paymentList = jdbcTemplate.query(SQL_READ_ALL_PAYMENT, new BeanPropertyRowMapper(PaymentData.class));
        System.out.println(paymentList);
        return paymentList;
    }

    public void updateResponse(PaymentResponse paymentResponse)
    {

        jdbcTemplate.update(SQL_UPDATE_PAYMENT, new Object[] { paymentResponse.getResponse(),
                paymentResponse.getNcresponse(), paymentResponse.getPayment_id() });

    }

    public void insertPaymentData(PaymentData paymentData)
    {
        try
        {
            jdbcTemplate.update(SQL_INSERT_PAYMENT,
                new Object[] { paymentData.getCompany_Code(),paymentData.getAmount(), paymentData.getCurrency(), paymentData.getOperation(),
                        paymentData.getOrderid(), paymentData.getPayid(), paymentData.getPspid(), paymentData.getPswd(),
                        paymentData.getUserid() });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
