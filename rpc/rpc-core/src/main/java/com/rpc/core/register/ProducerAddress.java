package com.rpc.core.register;

/******
 * 生产者服务地址
 * 
 * @author chenkang
 *
 */
public class ProducerAddress {

	private String host;
	private int port;
	private String name;
	private String version;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
