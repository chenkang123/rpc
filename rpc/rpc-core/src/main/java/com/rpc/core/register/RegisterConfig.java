package com.rpc.core.register;
/******
 *  注册配置项
 * @author chenkang
 *
 */
public class RegisterConfig {

	/**** 主机地址*******/
	private String host;

	/**** 主机端口*******/
	private int port;

	/**** 健康检查地址*******/
	private String healthCheckAddress;

	/**** 健康检查间隔*******/
	private int healthInterval;

	/**** 刷新间隔*******/
	private int refreshInterval;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getHealthCheckAddress() {
		return healthCheckAddress;
	}

	public int getHealthInterval() {
		return healthInterval;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHealthCheckAddress(String healthCheckAddress) {
		this.healthCheckAddress = healthCheckAddress;
	}

	public void setHealthInterval(int healthInterval) {
		this.healthInterval = healthInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}


	
	
	
	
	
	
	
	

}
