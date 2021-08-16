package com.youma.translate.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youma.translate.annotation.DictionaryFeild;
import com.youma.translate.enums.TransType;
import com.youma.translate.exception.TranslationException;
import com.youma.translate.interfaces.Dictionary;
import com.youma.translate.interfaces.FieldHandler;
import com.youma.translate.util.ApplicationContextUtil;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

public class DictionaryFieldHandler implements FieldHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String dictCode;

	/**
	 * 字典的key必须为string类型
	 */
	private String key;

	private String dictJson;

	private TransType tansType;

	private Object target;

	private Field field;

	/**
	 * 
	 * @param dictionaryFeildAnnotation 注解
	 * @param field                     被注解的字段
	 * @param target                    对象
	 */
	public DictionaryFieldHandler(DictionaryFeild dictionaryFeildAnnotation, Field field, Object target) {
		this.target = target;
		this.field = field;
		this.dictCode = dictionaryFeildAnnotation.dictCode();
		this.key = StrUtil.toString(ReflectUtil.getFieldValue(this.target, dictionaryFeildAnnotation.sourceFeild()));
		this.dictJson = dictionaryFeildAnnotation.dictJson();
		this.tansType = dictionaryFeildAnnotation.transType();
	}

	/**
	 * 字典处理逻辑
	 */
	@Override
	public void doHandler() {
		if (StrUtil.isNotBlank(dictJson)) {
			ReflectUtil.setFieldValue(this.target, field, readFromLocal());
			return;
		}
		ReflectUtil.setFieldValue(this.target, field, readFromDictory());
	}

	/**
	 * 通过TransType的属性来获取字典值
	 * 
	 * @param map
	 * @return
	 */
	private Object disposByTransType(Map<String, String> map) {
		if (tansType.equals(TransType.VALUE_TO_KEY)) {
			// 字典 key 与 value 反转
			Map<String, String> reverseMap = new HashMap<>(16);
			map.entrySet().forEach(e -> reverseMap.put(e.getValue(), e.getKey()));
			map = reverseMap;
		}
		return MapUtil.get(map, this.key, this.field.getType());
	}

	/**
	 * 从字典里解析
	 * 
	 * @return
	 */
	private Object readFromDictory() {
		Dictionary dictionary = ApplicationContextUtil.getApplicationContext().getBean(Dictionary.class);
		Map<String, String> map = dictionary.queryDict(this.dictCode);
		return disposByTransType(map);
	}

	/**
	 * 从dictJson属性解析值
	 * 
	 * @return
	 */
	private Object readFromLocal() {
		if (!JSONUtil.isJsonObj(this.dictJson)) {
			logger.error("dictJson属性必须为json格式:{}", this.dictJson);
			throw new TranslationException("dictJson属性必须为json格式");
		}
		Map<String, String> map = JSONUtil.parse(dictJson).toBean(new TypeReference<Map<String, String>>() {
		});
		return disposByTransType(map);
	}

}
