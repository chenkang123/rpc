package com.rpc.core.producer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rpc.core.register.RegisterService;

/*****
 * 要暴露接口的bena,bean将由spring管理
 * 
 * @author chenkang
 *
 */
public class ProducerBean implements InitializingBean, ApplicationContextAware {

	/**** 应用上下文 ****/
	private ApplicationContext context;

	private RegisterService registerService;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
