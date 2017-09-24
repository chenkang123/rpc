package com.rpc.core.producer;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rpc.core.config.ProducerConfig;
import com.rpc.core.register.RegisterService;
import com.rpc.core.util.ReflectionUtil;
import com.sun.tracing.ProviderFactory;

/*****
 * 要暴露接口的bena,bean将由spring管理
 * 
 * @author chenkang
 *
 */
public class ProducerBean implements InitializingBean, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ProducerBean.class);

	/**** 应用上下文 ****/
	private ApplicationContext context;

	private RegisterService registerService;

	private AtomicBoolean inited = new AtomicBoolean(false);

	/*** 生产者配置 ******/
	private ProducerConfig config = new ProducerConfig();

	@Value("${rpc.producer.host}")
	public void setHost(String host) {
		config.setHost(host);
	}

	@Value("${rpc.producer.port}")
	public void setPort(int port) {
		config.setPort(port);
	}

	public void setVersion(String version) {
		config.setVersion(version);
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void afterPropertiesSet() throws Exception {
		init();
	}

	public void init() throws Exception {
		if (!inited.compareAndSet(false, true)) {
			return;
		}
		// 检查接口配置项目
		checkConfig();
		try {
			// 祖册服务
			register();
			logger.info("接口[" + config.getInterfaceName() + "]版本[" + config.getVersion() + "]发布为RPC服务成功！");
		} catch (Exception e) {
			logger.error("接口[" + config.getInterfaceName() + "]版本[" + config.getVersion() + "]发布为RPC服务失败", e);
			throw e;
		}
	}

	private void checkConfig() {
		String serviceInterface = config.getInterfaceName();
		Object target = config.getTarget();
		if (null == target) {
			invalidDeclaration("未配置需要发布为服务的Object，服务名为: " + config.getInterfaceName());
		}
		// 声明的接口类是否存在
		Class<?> interfaceClass = null;
		try {
			interfaceClass = Class.forName(serviceInterface);
			config.setInterfaceClazz(interfaceClass);
		} catch (ClassNotFoundException cnfe) {
			invalidDeclaration("指定的接口类不存在[" + serviceInterface + "]");
		}
		// 必须声明接口类型
		if (!interfaceClass.isInterface()) {
			invalidDeclaration("指定的服务类型不是接口[" + serviceInterface + "]");
		}
		// 真实的服务是否实现了服务接口
		if (!interfaceClass.isAssignableFrom(target.getClass())) {
			invalidDeclaration("服务[" + target + "]没有实现指定接口[" + serviceInterface + "]");
		}
		// 接口方法的输入参数和返回类型
		for (Method serviceMethod : interfaceClass.getMethods()) {
			checkMethodParamAndReturnTypes(serviceMethod);
		}
		registerService = context.getBean(RegisterService.class);
		if (registerService == null) {
			invalidDeclaration("未指定服务注册中心!");
		}
		// providerFactory =
		// ExtensionLoader.getExtensionLoader(ProviderFactory.class).getDefaultExtension();
		// if (providerFactory == null) {
		// invalidDeclaration("未指定服务工厂!");
		// }
	}

	public void register() {

	}

	private void invalidDeclaration(String msg) {
		throw new IllegalArgumentException(msg);
	}

	public void setServiceInterface(String serviceInterface) {
		config.setInterfaceName(serviceInterface);
	}

	public void setTarget(Object target) {
		config.setTarget(target);
	}

	public void setServiceVersion(String serviceVersion) {
		config.setVersion(serviceVersion);
	}
	/***
	 * 校验方法参数和返回类型
	 * @param method
	 */
	private void checkMethodParamAndReturnTypes(Method method) {
		final String methodName = method.getName();
		final Class<?> returnType = method.getReturnType();
		final Class<?>[] paramTypes = method.getParameterTypes();
		String serviceInterface = config.getInterfaceName();
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("接口[").append(serviceInterface).append("], ");
		errorMsg.append("方法[").append(methodName).append("], ");
		if (ReflectionUtil.isAbstract(returnType)) {
			errorMsg.append("有抽象返回类型[").append(returnType.getName()).append("]. ");
			errorMsg.append("请不要返回客户端没有的实现类型.");
		}
		for (Class<?> paramType : paramTypes) {
			if (paramType.isArray()) {
				paramType = paramType.getComponentType();
			}
			if (ReflectionUtil.isAbstract(paramType)) {
				errorMsg.append("有抽象参数类型[").append(paramType.getName()).append("]. ");
				errorMsg.append("请确保服务端有实现类型.");
			}
		}
	}

}
