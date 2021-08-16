package com.youma.translate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.youma.translate.config.TranslateConfiguration;

/**
 * 开启转义功能
 * @author xuxu
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TranslateConfiguration.class)
public @interface EnableTranslate {
	
	/**
	 * 默认开启字典服务，若不使用字典服务，否则必须设置成false
	 * @Title: openDictionary   
	 * @Description:    
	 * @Date: 2021年7月21日 下午12:22:44
	 * @return     
	 * @throws
	 */
	boolean openDictionary() default true;

}
