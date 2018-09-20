/**
 * 
 */
package com.oms.order.connector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.order.common.OrderConstantIfc;
import com.oms.order.formatter.OMSOrderRequest;
import com.oms.rtlog.common.ProxyConfig;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Jigar
 *
 */
@Service
public class OMSOrderConnector
{
	private static final Logger logger = Logger.getLogger(OMSOrderConnector.class);

	@Autowired
	private ProxyConfig proxyConfig;
    /**
     * 
     */
    public OMSOrderConnector()
    {
       
    }
    
    public Object processRequest(OMSOrderRequest omsOrderRequest) throws IOException
    {
    	String postmanResponseStr ="";
    	OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    	
    	builder.sslSocketFactory(new ConnectionFactory().getTlsContext().getSocketFactory(),new SecureTrustManager());
    	
    	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHostName(),proxyConfig.getPort()));
    	if(proxyConfig!=null && proxyConfig.isProxyEnabled())
    		builder.proxy(proxy);
    	
    	OkHttpClient client = builder.build();
      
        MediaType mediaType = MediaType.parse(omsOrderRequest.getContentTypeValue());
        
        Request request = null;
        
        if( omsOrderRequest.getFormBody() != null)
        {
            
            request = new Request.Builder()
                    .url(omsOrderRequest.getEndpoint())
                    .post(omsOrderRequest.getFormBody())
                    .addHeader(OrderConstantIfc.REQUEST_CONTENT_TYPE_TOKEN_NAME,omsOrderRequest.getContentTypeValue())
                    .build();
        }
        else 
        {
            RequestBody body = RequestBody.create(mediaType,omsOrderRequest.getRequest());
            request = new Request.Builder()
                    .url(omsOrderRequest.getEndpoint())
                    .post(body)
                    .addHeader(OrderConstantIfc.REQUEST_AUTHORIZATION_TOKEN_NAME, omsOrderRequest.getAuthorizationValue())
                    .addHeader(OrderConstantIfc.REQUEST_CONTENT_TYPE_TOKEN_NAME,omsOrderRequest.getContentTypeValue())
                    .build();
        }
        

        logger.info("REQUEST : \n " + omsOrderRequest.getRequest());
           
        Response postmanResponse = null;
        postmanResponse = client.newCall(request).execute();
        
        if(postmanResponse!=null && postmanResponse.body()!=null)
        {
        	postmanResponseStr= postmanResponse.body().string();
        	if(StringUtils.isNotEmpty(postmanResponseStr))
        	{
        		postmanResponseStr=postmanResponseStr.trim();
        	}
            logger.info("RESPONSE : \n " + postmanResponseStr);
        }
        
        return postmanResponseStr;
    }
    
    
    
    
    public class ConnectionFactory implements HttpURLConnectionFactory {

        Proxy proxy;

        String proxyHost = null;

        Integer proxyPort = 0;

        SSLContext sslContext;
        
        SSLContext tlsContext;

        public ConnectionFactory() {
        }

        public ConnectionFactory(String proxyHost, Integer proxyPort) {
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
        }

        public void initializeProxy() {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        }

        @Override
        public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
            initializeProxy();
            HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
            if (con instanceof HttpsURLConnection) {
                HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection(proxy);
                httpsCon.setHostnameVerifier(getHostnameVerifier());
                httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
                return httpsCon;
            } else {
                return con;
            }

        }

        public SSLContext getSslContext() {
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[]{new SecureTrustManager()}, new SecureRandom());
            } catch (NoSuchAlgorithmException ex) {
               // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyManagementException ex) {
               // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            return sslContext;
        }
        
        public SSLContext getTlsContext() {
            try {
             tlsContext = SSLContext.getInstance("TLSv1.2");
             tlsContext.init(null, new TrustManager[]{new SecureTrustManager()}, new SecureRandom());
            } catch (NoSuchAlgorithmException ex) {
               // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyManagementException ex) {
               // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
            return tlsContext;
        }

        private HostnameVerifier getHostnameVerifier() {
            return new HostnameVerifier() {
                @Override
                public boolean verify(String hostname,
                        javax.net.ssl.SSLSession sslSession) {
                    return true;
                }
            };
        }

        
        
      
    }  
    
    class SecureTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public boolean isClientTrusted(X509Certificate[] arg0) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate[] arg0) {
            return true;
        }
 
 
    }

}
