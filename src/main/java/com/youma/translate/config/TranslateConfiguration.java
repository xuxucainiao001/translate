package com.youma.translate.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.youma.translate.annotation.EnableTranslate;
import com.youma.translate.core.TranslationAspect;
import com.youma.translate.util.ApplicationContextUtil;

import cn.hutool.core.map.MapUtil;

/**
 * 转义配置
 * 
 * @author xuxu
 *
 */

@Configuration
public class TranslateConfiguration implements ImportSelector {

	
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Map<String, Object> attr = importingClassMetadata.getAnnotationAttributes(EnableTranslate.class.getName());
		if (attr == null) {
			return new String[] {
					TranslationAspect.class.getName(),
					ApplicationContextUtil.class.getName()
					};
		}
		//如果开启openDictionary，则创建 DictionaryConfiguration配置对象
		boolean openDictionary = MapUtil.getBool(attr, "openDictionary");
		if (openDictionary) {
			return new String[] {
					TranslationAspect.class.getName(),
					ApplicationContextUtil.class.getName(),
					DictionaryConfiguration.class.getName()
					};
		}
		return new String[] {
				TranslationAspect.class.getName(),
				ApplicationContextUtil.class.getName()
				};
	}

}
