package com.youma.translate.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

public abstract class NoCachedTranslateInvoker <T, E> implements TranslateInvoker<T, E> {
	
	
	/**
	 * 需要重写的接口查询逻辑
	 * @param params
	 * @return
	 */
	public abstract Map<T, E> invokeByParams0(List<T> params);
	
	/**
	 * 
	 */
	@Override
	public Map<T, E> invokeByParams(Set<T> params) {
		if(CollectionUtils.isEmpty(params)) {
			return Collections.emptyMap();
		}
		return  invokeByParams0(new ArrayList<>(params));

	}
	

}
