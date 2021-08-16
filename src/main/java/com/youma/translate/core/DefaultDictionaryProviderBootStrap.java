package com.youma.translate.core;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youma.translate.interfaces.DictionaryLoadService;
import com.youma.translate.interfaces.DictionaryProviderBootStrap;

/**
 * 默认的字典加载逻辑
 * @author xuxu
 *
 */
public class DefaultDictionaryProviderBootStrap implements DictionaryProviderBootStrap {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String DICTIONARY_MAP = "DICTIONARY_MAP";
    
	/**
	 * 需要用户自己实现 DictionaryLoadService
	 */
	@Resource
	private DictionaryLoadService dictionaryLoadService;
    
   /**
    * 依赖redisson, 字典提供方需要通过redisson把字典全部上传到名为DICTIONARY_MAP 的 hash结构中
    */
	@Resource
	private RedissonClient redissonClient;
    
	@Override
	@PostConstruct
	public void initailiaze() {
		logger.info("字典库加载开始!");
		Map<String, Map<String, String>> dictionary = dictionaryLoadService.loadAllDictionaries();
		RMap<String, Map<String, String>> distributionMap = redissonClient.getMap(DICTIONARY_MAP);
		distributionMap.clear();
		distributionMap.putAll(dictionary);
		logger.info("字典库加载结束!distributionMap size:{} ",distributionMap.size());
	}

	@Override
	public void refresh() {
		initailiaze();
	}

}
