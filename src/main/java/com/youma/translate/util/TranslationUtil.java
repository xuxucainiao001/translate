package com.youma.translate.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.youma.translate.core.DictionaryFieldHandler;
import com.youma.translate.core.InterfaceFeildHandler;
import com.youma.translate.core.TranslationFactory;
import com.youma.translate.interfaces.TranslateInvoker;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

/**
 * 转义工具类
 * 
 * @author xuxu
 *
 */
public class TranslationUtil {

	private static final Logger logger = LoggerFactory.getLogger(TranslationUtil.class);

	private TranslationUtil() {
	}

	/**
	 * 转义方法
	 * 
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static final <T> T translate(T obj) {
		if(Objects.isNull(obj)) {
			return obj;
		}
		// 获取threadlocal保存 InterfaceFeildHandler集合，稍后批量处理
		ThreadLocal<List<InterfaceFeildHandler>> itl = ApplicationContextUtil.getInterfaceFeildHandlerThreadLocal();
		ThreadLocal<List<DictionaryFieldHandler>> dtl = ApplicationContextUtil.getDictionaryFieldHandlerThreadLocal();
		try {
			itl.set(Lists.newArrayList());
			dtl.set(Lists.newArrayList());
			//转义预处理
			TranslationFactory.createTranslation(obj).toTranslate();
			List<InterfaceFeildHandler> interfaceFeildHandlers = itl.get();
			List<DictionaryFieldHandler> dictionaryFieldHandlers = dtl.get();
			// 转义统一处理
			batchInvokeInterfaceFeildhandlers(interfaceFeildHandlers);
			//字典处理放在最后
			if(ApplicationContextUtil.isOpenDictionary()) {
				 batchInvokeDictionaryFieldHandlers(dictionaryFieldHandlers);
			}
		   
				
		} catch (Exception e) {
			logger.error("Translation转义发生异常:", e);
		} finally {
			// 清空 threadlocal 防止oom
			itl.remove();
			dtl.remove();
		}
		return obj;
	}

   /**
    * 批量调用字典
    * @param dictionaryFieldHandlers
    */
	private static void batchInvokeDictionaryFieldHandlers(List<DictionaryFieldHandler> dictionaryFieldHandlers) {
		if(CollectionUtils.isEmpty(dictionaryFieldHandlers)) {
			return;
		}
		dictionaryFieldHandlers.forEach(DictionaryFieldHandler::doHandler);		
	}

	/**
	 * 批量调用接口
	 * 
	 * @param handlers
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final void batchInvokeInterfaceFeildhandlers(List<InterfaceFeildHandler> handlers) {
		if (CollUtil.isEmpty(handlers)) {
			return;
		}
		HashMultimap<TranslateInvoker<?, ?>, Object> map = HashMultimap.create();
		// 从spring中获取接口invoke对象
		handlers.forEach(h -> {
			TranslateInvoker<?, ?> translateInvokeClass = ApplicationContextUtil.getApplicationContext()
					.getBean(h.getInvokeClass());
			map.put(translateInvokeClass, h.getParam());
		});
		// 开始批量调用
		map.keySet().forEach(invoke -> {
			// 获取invoke需要用的参数
			Set params = map.get(invoke);
			if (CollUtil.isNotEmpty(params)) {
				// 调用接口
				try {
					Map<Object, Object> resultMap = invoke.invokeByParams(params);								
					// 从接口中取值渲染
					handlers.forEach(h -> {
						if (Objects.equals(h.getInvokeClass(), invoke.getClass())) {
							Object obj = resultMap.get(h.getParam());
							if (Objects.nonNull(obj)) {
								// 获取结果对应属性的值
								Object value = ReflectUtil.getFieldValue(obj, h.getResultFeild());
								// 最后赋值
								h.setValue(value);
							}
						}
					});
				}catch(Exception e) {
					logger.error("转义接口调用发生异常:{},invokeClass:{}",e,invoke.getClass());
				}
			}
		});
	}
}
