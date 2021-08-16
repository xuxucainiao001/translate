package com.youma.translate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.youma.translate.enums.TransType;

/**
 * 字典属性转义注解
 * @author xuxu
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryFeild {
	
	/**
	 * 需要转义的属性字段
	 * @return 
	 */
	String sourceFeild();
	
	/**
	 * 字典组编码code,dictJson不为空时dictCode必须有值
	 * @return
	 */
	String dictCode() default "";
	
	/**
	 * 字典组描述，json格式  eg:{"1":"男","0":"女"}
	 * 若填写该字段，则不会从字典中取值，改为从json中取对应的值
	 * @return
	 */
	String dictJson() default "";
	
	/**
	 * 转义方式
	 * @return
	 */
	TransType transType() default TransType.KEY_TO_VALUE;

}



