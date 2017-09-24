package com.rpc.core.register;

import java.util.List;

/*****
 * 注册服务接口
 * 
 * @author chenkang
 *
 */
public interface RegisterService {

	/***发现可用服务端列表 *****/
	public List<ProducerAddress> findAvailableAddresses(String name, String version);

	/***注册服务端 *****/
	public void register(ProducerAddress address);

	/***刷新服务端 *****/
	public void refresh();

}
