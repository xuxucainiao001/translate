package com.youma.translate.core;

import java.util.Optional;

import com.youma.translate.interfaces.Translation;

public class OptionalTranslation implements Translation {

	private Optional<?> optional;

	public OptionalTranslation(Optional<?> optional) {
		this.optional = optional;
	}

	@Override
	public  void  toTranslate() {
		optional.ifPresent(e->
		   TranslationFactory.createTranslation(e).toTranslate()
        );
	}

}
