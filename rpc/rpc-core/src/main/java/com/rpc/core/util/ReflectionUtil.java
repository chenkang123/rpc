package com.rpc.core.util;

import java.lang.reflect.Modifier;
/*****
 * 反射工具类
 * @author chenkang
 *
 */
public class ReflectionUtil {

	/***
	 * 判断一个类是否为抽象
	 * @param clazz
	 * @return
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}
	/****
	 * 判断一个类是否为final
	 * @param clazz
	 * @return
	 */
	public static boolean isFinal(Class<?> clazz) {
		return Modifier.isFinal(clazz.getModifiers());
	}

}
