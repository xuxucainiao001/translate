package com.youma.translate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.youma.translate.interfaces.TranslateInvoker;

/**
 * 接口转义属性
 * @author xuxu
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceFeild {
	
	/**
	 * 需要转义的属性字段
	 * @return 
	 */
	String sourceFeild();
	
	/**
	 * 转义使用的接口
	 * @return
	 */
	Class<? extends TranslateInvoker<?,?>> invokeClass();
	
	
	/**
	 * 接口返回对象属的具体属性值,为""时就是对象本身
	 * @return
	 */
	String  resultFeild();

}
