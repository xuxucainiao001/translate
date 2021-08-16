package com.youma.translate.util;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.youma.translate.core.DictionaryFieldHandler;
import com.youma.translate.core.InterfaceFeildHandler;

public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;
	
	//ThreadLocal统一保存
    private static final ThreadLocal<List<InterfaceFeildHandler>> I_THREAD_LOCAL = new ThreadLocal<>();
    
    private static final ThreadLocal<List<DictionaryFieldHandler>> D_THREAD_LOCAL = new ThreadLocal<>();
   
	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		ApplicationContextUtil.context = applicationContext;

	}

	public static final ApplicationContext getApplicationContext() {
		return ApplicationContextUtil.context;
	}
    
	public static ThreadLocal<List<InterfaceFeildHandler>> getInterfaceFeildHandlerThreadLocal() {
		return ApplicationContextUtil.I_THREAD_LOCAL;
	}
	
	public static ThreadLocal<List<DictionaryFieldHandler>> getDictionaryFieldHandlerThreadLocal() {
		return ApplicationContextUtil.D_THREAD_LOCAL;
	}
	
	public static boolean isOpenDictionary() {
		return context.containsBean("dictionaryConfiguration");
	}
	
   
}
