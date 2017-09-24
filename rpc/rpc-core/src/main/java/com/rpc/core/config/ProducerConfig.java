package com.rpc.core.config;

import lombok.Data;

/*****
 * 生产者配置.
 * 
 * @author chenkang
 *
 */
public class ProducerConfig extends AbstractConfig {

	/**** 暴露接口 ****/
	private Object target;

	/**** 远程主机 ****/
	private String host;
	/**** 远程主机端口 ****/
	private int port;

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

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

	@Override
	public String toString() {
		return "ProducerConfig [target=" + target + ", host=" + host + ", port=" + port + "]";
	}

}
