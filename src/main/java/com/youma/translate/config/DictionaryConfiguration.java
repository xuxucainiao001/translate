package com.youma.translate.config;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.youma.translate.core.DefaultDictionary;
import com.youma.translate.core.DefaultDictionaryProviderBootStrap;
import com.youma.translate.interfaces.Dictionary;
import com.youma.translate.interfaces.DictionaryLoadService;
import com.youma.translate.interfaces.DictionaryProviderBootStrap;

@Configuration("dictionaryConfiguration")
public class DictionaryConfiguration{
	
	/**
	 * 字典提使用方
	 * @Title: dictionaryBootStrap   
	 * @Description:    
	 * @Date: 2021年7月21日 下午5:21:13
	 * @return     
	 * @throws
	 */
	@Bean
	@ConditionalOnBean({RedissonClient.class})
	public Dictionary dictionary() {
		return new DefaultDictionary();
	}
	
	/**
	 * 字典提供方
	 * @Title: dictionaryBootStrap   
	 * @Description:    
	 * @Date: 2021年7月21日 下午5:21:13
	 * @return     
	 * @throws
	 */
	@Bean
	@ConditionalOnBean({ DictionaryLoadService.class, RedissonClient.class })
	public DictionaryProviderBootStrap dictionaryBootStrap() {
		return new DefaultDictionaryProviderBootStrap();
	}
}