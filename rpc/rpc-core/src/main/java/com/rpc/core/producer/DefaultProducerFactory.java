package com.rpc.core.producer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.rpc.core.config.ProducerConfig;
import com.rpc.core.constant.RpcConstant;

public class DefaultProducerFactory implements ProducerFactory {

	private static final ConcurrentMap<String, ProducerConfig> REGISTER_PRODUCERS = new ConcurrentHashMap<String, ProducerConfig>();

	public Object getProducer(String interfaceName) {
		return REGISTER_PRODUCERS.get(interfaceName + "_" + RpcConstant.LASTEST_VERSION);
	}

	public Object getProducer(String interfaceName, String version) {
		return REGISTER_PRODUCERS.get(interfaceName + "_" + version);

	}

	public ProducerConfig getProducerConfig(String interfaceName, String version) {
		return REGISTER_PRODUCERS.get(interfaceName + "_" + version);
	}

	public void register(ProducerConfig producerConfig) {
		REGISTER_PRODUCERS.putIfAbsent(producerConfig.getInterfaceName()+"_"+producerConfig.getVersion(), producerConfig);
	}

}
