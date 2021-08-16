package com.youma.translate.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * 具体转义需要根据业务实现
 * @author xuxu
 *
 * @param <T,E>
 */
public interface TranslateInvoker<T,E> {
    /**
     * 
     * @param params 
     * @return T 参数的类型 E返回实体的类型
     */
	Map<T,E> invokeByParams(Set<T> params);

}
