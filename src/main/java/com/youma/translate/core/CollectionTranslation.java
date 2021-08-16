package com.youma.translate.core;

import java.util.Collection;

import com.youma.translate.interfaces.Translation;

import cn.hutool.core.collection.CollUtil;



/**
 * 集合转义
 * 
 * @author xuxu
 *
 */
public class CollectionTranslation implements Translation {

	private Collection<?> coll;

	public CollectionTranslation(Collection<?> coll) {
		this.coll = coll;
	}

	@Override
	public void toTranslate() {
		if(CollUtil.isEmpty(coll)) {
			return ;
		}
		coll.forEach(e -> 
            TranslationFactory.createTranslation(e).toTranslate()
		);
	}

}
