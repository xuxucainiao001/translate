package com.youma.translate.interfaces;

import java.util.Map;

/**
 * 字典数据提供方实现
 * @author xuxu
 *
 */
public interface DictionaryLoadService {
	
	/**
	 * 加载所有字典
	 * @return
	 */
	Map<String,Map<String,String>> loadAllDictionaries();

}
