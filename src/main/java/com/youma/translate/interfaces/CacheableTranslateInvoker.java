package com.youma.translate.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 带缓存的接口查询 (2021年7月21日废弃) 建议改为 CachedTranslateInvoker
 * @see com.youma.translate.interfaces.CachedTranslateInvoker<T, E>
 * @author xuxu
 * @param <T>
 * @param <E>
 */

@Deprecated
public abstract class CacheableTranslateInvoker<T, E> implements TranslateInvoker<T, E> {
	
	
	private  Cache<T, E> cache = CacheBuilder.newBuilder()
			.maximumSize(256)
			.initialCapacity(256)
			.weakKeys()
			.weakValues()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build();;
	
	
	/**
	 * 需要重写的接口查询逻辑
	 * @param params
	 * @return
	 */
    @Deprecated
	public abstract Map<T, E> invokeByParams0(Set<T> params);
	
	
	@Override
	public Map<T, E> invokeByParams(Set<T> params) {
		Set<T> sets = Sets.newHashSet();
		Map<T, E> maps = Maps.newHashMap();
		// 缓存有则从缓存中取，缓存没有则从接口查询
		for (T key : params) {
			if(key==null) {
				continue;
			}
			E value = cache.getIfPresent(key);
			if (Objects.nonNull(value)) {
				maps.put(key, value);
			} else {
				sets.add(key);
			}
		}
		if(!sets.isEmpty()) {
			// 查询结果和缓存合并
			maps.putAll(invokeByParams0(sets));
			cache.putAll(maps);
		}
		return maps;
		//计入缓存		
	}
    
	

}
