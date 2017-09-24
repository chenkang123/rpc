package com.rpc.core.constant;

/*****
 * 常量类
 * 
 * @author chenkang
 *
 */
public class RpcConstant {

	public static final String LASTEST_VERSION = "Lastest";

	public static final String CONTEXT_PATH = "/";

	public static final int DEFAULT_RETRY_COUNT = 3;

	public static final int DEFAULT_connectTimeoutMillis = 3000;

	public static final int DEFAULT_readTimeoutMillis = 3000;

	public static final int DEFAULT_healthInterval = 2;

	public static final int DEFAULT_refreshInterval = 5;

	public static final String LB_RANDOM = "random";

	public static final String HA_FAILFAST = "failfast";

	public static final String HA_FAILOVER = "failover";

	public static final String Rpc_ROOT_PATH = "Rpc-service";

	public static final String HOST = "HOST";

	public static final String PORT = "PORT";

	public static final String Rpc_threadpool = "fixed";

	public static final int Rpc_threads = 100;

	public static final int Rpc_queues = 0;

	public static final int Rpc_cores = 0;

	public static final int Rpc_alive = 60 * 1000;

	public static final String DEFAULT_THREAD_NAME = "Rpc";

	public static final String CLUSTER_TYPE = "default";

	public static final String INVOKER_TYPE = "default";

	public static final String Serialization_TYPE = "fastjson";

	public static final String DEFAULT_TRUST_STORE = "META-INF/truststore.jks";

	public static final String DEFAULT_TRUST_PWD = "123456";

}
