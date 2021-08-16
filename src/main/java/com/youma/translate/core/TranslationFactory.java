package com.youma.translate.core;

import java.util.Collection;
import java.util.Optional;

import com.youma.translate.interfaces.Translation;


public class TranslationFactory {
		
	private TranslationFactory() {}
	

	public  static final Translation createTranslation(Object obj) {
		if(obj instanceof Collection) {
			return new CollectionTranslation((Collection<?>)obj);
		}
		if(obj instanceof Optional) {
			return new OptionalTranslation((Optional<?>)obj);
		}
		return new PojoTranslation(obj);
	}

}
