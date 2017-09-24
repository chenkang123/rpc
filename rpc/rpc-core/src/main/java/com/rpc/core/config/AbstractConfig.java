package com.rpc.core.config;

import com.rpc.core.constant.RpcConstant;

import lombok.Data;

/*****
 * 一些通用配置项目
 * 
 * @author chenkang
 *
 */
public class AbstractConfig {

	/*** 接口名字 ***/
	private String interfaceName;

	/*** 接口版本 ***/
	private String version = RpcConstant.LASTEST_VERSION;

	/*** 接口class ***/
	private Class<?> interfaceClazz;

	/*** 线程数 ***/
	private int threads = RpcConstant.Rpc_threads;

	/*** 队列个数 ***/
	private int queues = RpcConstant.Rpc_queues;

	/*** 核心线程数 ***/
	private int cores = RpcConstant.Rpc_cores;

	/*** 线程存活时间 ***/
	private int alive = RpcConstant.Rpc_alive;
	/*** 序列化协议 ***/
	private String serialization = RpcConstant.Serialization_TYPE;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Class<?> getInterfaceClazz() {
		return interfaceClazz;
	}

	public void setInterfaceClazz(Class<?> interfaceClazz) {
		this.interfaceClazz = interfaceClazz;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getQueues() {
		return queues;
	}

	public void setQueues(int queues) {
		this.queues = queues;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	public int getAlive() {
		return alive;
	}

	public void setAlive(int alive) {
		this.alive = alive;
	}

	public String getSerialization() {
		return serialization;
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

}
