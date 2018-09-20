/**
 * 
 */
package com.oms.rtlog.common;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Jigar
 *
 */

@Component
public class ProxyConfig
{

	@Value("${proxy.enabled}")
	private boolean proxyEnabled; 
	
	@Value("${proxy.hostname}")
    private String hostName;
	
	@Value("${proxy.username}")
    private transient String userName;
	
	@Value("${proxy.port}")
    private int port;
	
	@Value("${proxy.password}")
    private transient String password;
	
	@Value("${proxy.domain}")
    private String domain;

    /**
     * @return the domain
     */
    public String getDomain() {

        return domain;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {

        return hostName;
    }

    /**
     * @return the password
     */
    public String getPassword() {

        return password;
    }

    /**
     * @return the port
     */
    public int getPort() {

        return port;
    }

    /**
     * @return the userName
     */
    public String getUserName() {

        return userName;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain) {

        this.domain = domain;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {

        this.hostName = hostName;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {

        this.port = port;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    public boolean isProxyEnabled() {
		return proxyEnabled;
	}

	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "ProxyConfig [hostName=" + hostName + ", userName=" + userName + ", port=" + port
                + ", password=" + "******" + ", domain=" + domain + "]";
    }



}
