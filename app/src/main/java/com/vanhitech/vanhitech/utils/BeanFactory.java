package com.vanhitech.vanhitech.utils;

import java.io.IOException;
import java.util.Properties;

public class BeanFactory {
	private static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取配置文件中指定的实例对象
	 * @param clazz
	 * @return
	 */
	public static<T>  T getImpl(Class<T> clazz) {
//		String name = properties.getProperty("UserEngine");
		String name = properties.getProperty(clazz.getSimpleName());

		try {
			return (T) Class.forName(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
