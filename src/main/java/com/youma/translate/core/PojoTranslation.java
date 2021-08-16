package com.youma.translate.core;

import java.lang.reflect.Field;
import java.util.Objects;

import com.youma.translate.annotation.DictionaryFeild;
import com.youma.translate.annotation.InterfaceFeild;
import com.youma.translate.annotation.Translate;
import com.youma.translate.interfaces.Translation;
import com.youma.translate.util.ApplicationContextUtil;

import cn.hutool.core.util.ReflectUtil;

public class PojoTranslation implements Translation {

	private Object target;

	public PojoTranslation(Object target) {
		this.target = target;
	}

	/**
	 * projo类的处理
	 */
	@Override
	public void toTranslate() {
		if(Objects.isNull(this.target)) {
			return ;
		}
		Class<?> tragetClass = this.target.getClass();
		Field[] fields = ReflectUtil.getFields(tragetClass);
		for (Field field : fields) {
			Object fieldValue = ReflectUtil.getFieldValue(this.target, field);
			Class<?> fieldType = field.getType();
			// 若字段有注解@Translate 或者 字段的class上有注解 @Translate
			if (field.isAnnotationPresent(Translate.class) || fieldType.isAnnotationPresent(Translate.class)) {
				TranslationFactory.createTranslation(fieldValue).toTranslate();
			}
			// 字段有则无需再次转义
			if (fieldValue != null) {
				continue;
			}
			if (field.isAnnotationPresent(DictionaryFeild.class)) {
				DictionaryFeild dictionaryFeildAnnotation = field.getAnnotation(DictionaryFeild.class);
				ApplicationContextUtil.getDictionaryFieldHandlerThreadLocal().get()
						.add(new DictionaryFieldHandler(dictionaryFeildAnnotation, field, this.target));
			}
			if (field.isAnnotationPresent(InterfaceFeild.class)) {
				InterfaceFeild interfaceFeildAnnotation = field.getAnnotation(InterfaceFeild.class);
				// 不要调用doHandler()，接口需要批量处理,handler直接放入threadlocal
				ApplicationContextUtil.getInterfaceFeildHandlerThreadLocal().get()
						.add(new InterfaceFeildHandler(interfaceFeildAnnotation, field, this.target));
			}
		}

	}

}
