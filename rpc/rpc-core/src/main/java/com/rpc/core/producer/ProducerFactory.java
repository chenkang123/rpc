package com.rpc.core.producer;

import com.rpc.core.config.ProducerConfig;
import com.rpc.core.core.extention.SPI;

/*****
 * 生产者工厂模式
 * 
 * @author chenkang
 *
 */
@SPI("default")
public interface ProducerFactory {

	/****
	 * 根据接口名字,得到接口实例
	 * 
	 * @param interfaceName
	 * @return
	 */
	public Object getProducer(String interfaceName);

	/****
	 * 根据接口名字和版本号,得到接口实例
	 * 
	 * @param interfaceName
	 * @return
	 */
	public Object getProducer(String interfaceName, String version);

	/*****
	 * 获取生产者配置
	 * 
	 * @param interfaceName
	 * @param version
	 * @return
	 */
	public ProducerConfig getProducerConfig(String interfaceName, String version);

	/******
	 * 在内存中注册配置项实例
	 * 
	 * @param ProducerConfig
	 */
	public void register(ProducerConfig ProducerConfig);

}
