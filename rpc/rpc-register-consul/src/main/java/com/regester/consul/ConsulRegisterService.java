package com.regester.consul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.ecwid.consul.v1.health.model.HealthService;
import com.rpc.core.register.AbstractRegister;
import com.rpc.core.register.ProducerAddress;
import com.rpc.core.register.RegisterConfig;

public class ConsulRegisterService extends AbstractRegister {

	private RegisterConfig config = new RegisterConfig();

	private ConsulClient client;

	private static final Logger logger = LoggerFactory.getLogger(ConsulRegisterService.class);

	private Lock lock = new ReentrantLock();

	@PostConstruct
	public void init() {
		logger.info("init consul start");
		client = new ConsulClient(config.getHost(), config.getPort());
		// 刷新consul列表
		new Thread(new RefreshConsul());
	}

	class RefreshConsul implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(config.getRefreshInterval());
					refresh();
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}

		}
	}

	@Value("${rpc.register.host}")
	public void setHost(String host) {
		this.config.setHost(host);
	}

	@Value("${rpc.register.port}")
	public void setPort(int port) {
		this.config.setPort(port);
	}

	@Value("${rpc.register.healthCheckAddress}")
	public void setHealthCheckAddress(String healthCheckAddress) {
		this.config.setHealthCheckAddress(healthCheckAddress);
	}

	@Value("${rpc.register.healthInterval}")
	public void setHealthInterval(int healthInterval) {
		this.config.setHealthInterval(healthInterval);
	}

	@Value("${rpc.register.refreshInterval}")
	public void setRefreshInterval(int refreshInterval) {
		this.config.setRefreshInterval(refreshInterval);
	}

	public List<ProducerAddress> findAvailableAddresses(String name, String version) {
		// 先从缓存中拿
		List<ProducerAddress> addresses = getAddressFromCache(name, version);
		// 没有,从consul查询
		if (CollectionUtils.isEmpty(addresses)) {
			addresses = findConsulAvailableAddresses(name, version);
			putCache(name, version, addresses);
		}
		return addresses;
	}

	private List<ProducerAddress> findConsulAvailableAddresses(String name, String version) {
		Response<List<HealthService>> services = client.getHealthServices(name, version, true, QueryParams.DEFAULT);
		List<ProducerAddress> addresses = new ArrayList<ProducerAddress>();
		if (services != null && !CollectionUtils.isEmpty(services.getValue())) {
			for (HealthService service : services.getValue()) {
				ProducerAddress address = new ProducerAddress();
				address.setHost(service.getService().getAddress());
				address.setPort(service.getService().getPort());
				address.setName(name);
				address.setVersion(version);
				addresses.add(address);
				addProducerAddressToCache(address);
			}
		}
		putCache(name, version, addresses);
		return addresses;
	}

	public void register(ProducerAddress address) {

		logger.info("服务注册开始(" + address.toString() + ")");
		/*** 如果已经存在的情况 ***/
		if (exist(address)) {
			addProducerAddressToCache(address);
			return;
		}
		/*** 如果已经不存在,重新添加 ***/
		List<String> version = new ArrayList<String>();
		version.add(address.getVersion());
		NewService newService = new NewService();
		newService.setId(String.valueOf(System.currentTimeMillis()));
		newService.setName(address.getName());
		newService.setPort(address.getPort());
		newService.setAddress(address.getHost());
		newService.setTags(version);
		NewService.Check serviceCheck = new NewService.Check();
		serviceCheck.setHttp(config.getHealthCheckAddress());
		serviceCheck.setInterval(config.getHealthInterval() + "s");
		newService.setCheck(serviceCheck);
		client.agentServiceRegister(newService);
		addProducerAddressToCache(address);
		logger.info("服务注册结束(" + address.toString() + ")");

	}

	/*****
	 * 判断地址是否存在
	 * 
	 * @param address
	 * @return
	 */
	private boolean exist(ProducerAddress address) {
		List<ProducerAddress> producerAddresses = findAllAddresses(address.getName(), address.getVersion());
		if (CollectionUtils.isEmpty(producerAddresses)) {
			return false;
		}
		for (ProducerAddress producerAddress : producerAddresses) {
			if (producerAddress != null && address.getHost().equals(producerAddress.getHost())
					&& address.getPort() == producerAddress.getPort()) {
				return true;
			}
		}
		return false;
	}

	/******
	 * 查出所以可用服务和版本
	 * 
	 * @param name
	 * @param version
	 * @return
	 */
	public List<ProducerAddress> findAllAddresses(String name, String version) {
		Response<List<CatalogService>> services = client.getCatalogService(name, version, QueryParams.DEFAULT);
		List<ProducerAddress> addresses = new ArrayList<ProducerAddress>();
		if (services != null && !CollectionUtils.isEmpty(services.getValue())) {
			for (CatalogService service : services.getValue()) {
				ProducerAddress address = new ProducerAddress();
				address.setHost(service.getServiceAddress());
				address.setPort(service.getServicePort());
				address.setName(name);
				address.setVersion(version);
				addresses.add(address);
			}
		}
		return addresses;
	}

	public void refresh() {
		lock.lock();
		try {
			Set<Map<String, String>> keys = getCacheKeys();
			for (Map<String, String> key : keys) {
				String name = key.get("name");
				String version = key.get("version");
				deleteCacheValueByKey(name, version);
				putCache(name, version, findAvailableAddresses(name, version));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			lock.unlock();
		}
	}

}
