package com.youma.translate.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.youma.translate.exception.TranslationException;

/**
 * 带缓存的接口查询，旧的CachedTranslateInvoker已经废弃，不建议再使用
 * @author xuxu
 *
 * @param <T>
 * @param <E>
 */
public abstract class CachedTranslateInvoker<T, E> implements TranslateInvoker<T, E> {
	
	
	private  Cache<T, E> cache;
	
	
    @PostConstruct
	public void initCache() {
    	cache = definedCache();
		if(cache == null) {
			cache = defaultCache();
		}
		if(cache == null) {
			throw new TranslationException(getClass().getName()+"的缓存不能为 null");
		}
	}

	/**
	 * 用户cache的默认实现，由子类覆盖
	 * @Title: definedCache   
	 * @Description:    
	 * @Date: 2021年7月21日 上午9:52:02
	 * @return     
	 * @throws
	 */
	protected Cache<T, E> definedCache() {
		return null;
	}
	
	/**
	 * 需要重写的接口查询逻辑
	 * @param params
	 * @return
	 */
	public abstract Map<T, E> invokeByParams0(List<T> params);
	
	
	/**
	 * 默认提供的缓存，满足大部分业务场景
	 * 用户可以通过覆盖 definedCache()实现自己的缓存
	 * @Title: defaultCache   
	 * @Description:    
	 * @Date: 2021年7月21日 上午9:55:38
	 * @return     
	 * @throws
	 */
	private  Cache<T, E> defaultCache(){
		 return CacheBuilder.newBuilder()
			.maximumSize(256)
			.initialCapacity(256)
			.weakKeys()
			.weakValues()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build();
	}

	
	@Override
	public Map<T, E> invokeByParams(Set<T> params) {
		Set<T> sets = Sets.newHashSet();
		Map<T, E> maps = Maps.newHashMap();
		// 缓存有则从缓存中取，缓存没有则从接口查询
		for (T key : params) {
			if(key==null) {
				continue;
			}
			E value = this.cache.getIfPresent(key);
			if (Objects.nonNull(value)) {
				maps.put(key, value);
			} else {
				sets.add(key);
			}
		}
		if(!sets.isEmpty()) {
			// 查询结果和缓存合并
			maps.putAll(invokeByParams0(new ArrayList<>(sets)));
			//计入缓存	
			this.cache.putAll(maps);
		}
		return maps;			
	}
    

}
