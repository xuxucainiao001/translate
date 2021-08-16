package com.youma.translate.interfaces;

import java.util.Map;

/**
 * 字典查询接口，不同业务方可以自己实现，也可以使用默认实现 DefaultDictionary
 * @author xuxu
 *
 */
public interface Dictionary {
		
	   public Map<String,String> queryDict(String dictCode);

}
