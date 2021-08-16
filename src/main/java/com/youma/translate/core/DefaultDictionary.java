package com.youma.translate.core;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youma.translate.interfaces.Dictionary;

/**
 * 字典查询默认实现
 * @author xuxu
 *
 */
public class DefaultDictionary implements Dictionary {

	private Logger logger = LoggerFactory.getLogger(DefaultDictionary.class);
	
	@Resource
	private RedissonClient redissonClient;
	
	private RLocalCachedMap<String, Map<String, String>> dictionaryCache;
    
	/**
	 * redis Hash 的key名
 	 */
	public static final String DICTIONARY_MAP = "DICTIONARY_MAP";
	
	/**
	 * 需要字典库服务先启动，数据加载到 名为DICTIONARY_MAP 的 hash数据结构里
	 * 本方法建立本地服务和字典redis的连接
	 */
	@PostConstruct
	public void init() {
		logger.info("连接字典缓存开始...");
		LocalCachedMapOptions<String, Map<String, String>> option = LocalCachedMapOptions.defaults();
		option.maxIdle(5, TimeUnit.MINUTES)
		     .cacheSize(5120)
		     .timeToLive(10,TimeUnit.MINUTES)
		     .syncStrategy(SyncStrategy.UPDATE);
		this.dictionaryCache=this.redissonClient.getLocalCachedMap(DICTIONARY_MAP,option);
		logger.info("连接字典缓存结束...");
		if(this.dictionaryCache != null) {
			logger.info("字典大小：{}" , dictionaryCache.size());
		}
	}
	
	/**
	 * 从缓存中读取字典
	 */
	@Override
	public Map<String, String> queryDict(String dictCode) {
		return this.dictionaryCache.get(dictCode);	
	}

}
