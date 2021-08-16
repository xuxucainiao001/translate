package com.youma.translate.core;

import java.lang.reflect.Field;

import com.youma.translate.annotation.InterfaceFeild;
import com.youma.translate.interfaces.FieldHandler;
import com.youma.translate.interfaces.TranslateInvoker;

import cn.hutool.core.util.ReflectUtil;

public class InterfaceFeildHandler implements FieldHandler {

	private Class<? extends TranslateInvoker<?, ?>> invokeClass;

	/**
	 * 接口参数
	 */
	private Object param;

	/**
	 * 去除接口返回结果的字段值
	 */
	private String resultFeild;

	/**
	 * 需要被赋值的字段
	 */
	private Field field;

	/**
	 * 需要被转义赋值的对象
	 */
	private Object target;

	/**
	 * 
	 * @param interfaceFeild
	 * @param field          需要赋值的字段
	 * @Param Object target 需要赋值的对象
	 */
	public InterfaceFeildHandler(InterfaceFeild interfaceFeild, Field field, Object target) {
		this.param = ReflectUtil.getFieldValue(target, interfaceFeild.sourceFeild());
		this.invokeClass = interfaceFeild.invokeClass();
		this.resultFeild = interfaceFeild.resultFeild();
		this.field = field;
		this.target = target;
	}

	@Override
	public void doHandler() {
		// do nothing
	}

	public Class<? extends TranslateInvoker<?, ?>> getInvokeClass() {
		return invokeClass;
	}

	public Object getParam() {
		return param;
	}

	public String getResultFeild() {
		return resultFeild;
	}

	public void setValue(Object value) {
		ReflectUtil.setFieldValue(target, this.field, value);
	}

	public Field getField() {
		return field;
	}

}
