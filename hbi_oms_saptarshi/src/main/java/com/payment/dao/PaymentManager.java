package com.payment.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oms.order.formatter.OMSOrderResponse;
import com.oms.order.formatter.schema.pickout.PickOutMessage;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader.PickDetails;
import com.oms.order.formatter.schema.pickout.PickOutMessage.PickHeader.PickDetails.PickDetail;
import com.payment.constant.PaymentConstantIfc;
import com.payment.data.PaymentData;
import com.payment.formatter.PaymentResponse;
import com.payment.service.PaymentService;

@Component
public class PaymentManager
{

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    PaymentService paymentService;
    
    @Value("${ingenico.pspid}")
    private String ingenicoPspid;

    @Value("${ingenico.userid}")
    private String ingenicoUserid;
    
    @Value("${ingenico.password}")
    private String ingenicoPassword;

    public void paymentSettlement()
    {
        List<PaymentData> paymentDataList = paymentDao.getPaymentDatabyStatus();
        PaymentResponse paymentResponse = null;

        for (PaymentData paymentData : paymentDataList)
        {
            paymentResponse = paymentService.processPayment(paymentData);
            paymentDao.updateResponse(paymentResponse);

        }
    }

    public PaymentData parsePaymentData(OMSOrderResponse orderFromOMS)
    {

        PaymentData paymentData = new PaymentData();
        PickOutMessage pickOutMessage = orderFromOMS.getPickOutMessage();
        PickHeader pickHeader = null;
        double sellingPriceExtended = 0.0;
        double freightamt = 0.0;
        String freightAmountString = null;
        if (pickOutMessage != null)
        {
            pickHeader = pickOutMessage.getPickHeader();
            if (pickHeader != null)
            {
                String orderNbr = pickHeader.getOrderNbr();
                String company_Code = pickHeader.getCompany();
                if (orderNbr != null)
                {
                    paymentData.setOrderid(orderNbr);
                }
                if( company_Code != null)
                {
                    paymentData.setCompany_Code(company_Code);
                }
                freightAmountString = pickHeader.getFreightAmt();
                if (freightAmountString != null)
                {
                    freightamt += Double.parseDouble(freightAmountString);
                }

                PickDetails pickDetails = pickHeader.getPickDetails();
                if (pickDetails != null)
                {
                    List<PickDetail> pickDetailList = pickDetails.getPickDetail();
                    sellingPriceExtended = 0.0;
                    for (PickDetail pickDetail : pickDetailList)
                    {
                        sellingPriceExtended += Double.parseDouble(pickDetail.getSellingPriceExtended());

                    }
                }

            }

        }

        paymentData.setPspid(ingenicoPspid);
        paymentData.setUserid(ingenicoUserid);
        paymentData.setPswd(ingenicoPassword);
        paymentData.setOperation(PaymentConstantIfc.SAL);
        paymentData.setCurrency("EUR"); // Logic to get the Currency Code.
        double amount = sellingPriceExtended + freightamt;
        paymentData.setAmount(String.valueOf(amount));

        return paymentData;

    }

    public void insertPaymentData(PaymentData paymentData)
    {
        paymentDao.insertPaymentData(paymentData);

    }
}
