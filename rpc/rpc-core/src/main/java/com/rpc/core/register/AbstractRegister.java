package com.rpc.core.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractRegister {
	/**** 本地内存缓存的生产者 *****/
	private static final ConcurrentMap<String, List<ProducerAddress>> REGISTER_PROVIDERS = new ConcurrentHashMap<String, List<ProducerAddress>>();

	/**** 添加至缓存中 *****/
	protected void addProducerAddressToCache(ProducerAddress address) {
		List<ProducerAddress> addresses = REGISTER_PROVIDERS.get(address.getName() + "_" + address.getVersion());
		if (CollectionUtils.isEmpty(addresses)) {
			addresses = new ArrayList<ProducerAddress>();
			addresses.add(address);
			REGISTER_PROVIDERS.put(address.getName() + "_" + address.getVersion(), addresses);
		} else {
			addresses.add(address);
		}
	}

	/**** 查询 *****/
	protected List<ProducerAddress> getAddressFromCache(String name, String version) {
		return REGISTER_PROVIDERS.get(name + "_" + version);
	}

	/**** 删除 *****/
	protected void deleteCacheValueByKey(String name, String version) {
		REGISTER_PROVIDERS.remove(name + "_" + version);
	}

	/**** 添加 *****/
	protected void putCache(String name, String version, List<ProducerAddress> addresses) {
		REGISTER_PROVIDERS.putIfAbsent(name + "_" + version, addresses);
	}
	
	protected Set<Map<String, String>> getCacheKeys() {
		Set<String> keys = REGISTER_PROVIDERS.keySet();
		Set<Map<String, String>> values = new HashSet<Map<String, String>>();
		for (String key : keys) {
			Map<String, String> nameVersion = new HashMap<String, String>();
			nameVersion.put("name", key.substring(0, key.indexOf("_")));
			nameVersion.put("version", key.substring(key.indexOf("_") + 1));
			values.add(nameVersion);
		}
		return values;
	}

}
