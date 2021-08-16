package com.youma.translate.core;

import java.lang.reflect.Parameter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.youma.translate.annotation.Translate;
import com.youma.translate.util.TranslationUtil;

/**
 * Translate 注解拦截器
 * @author xuxu
 *
 */
@Aspect
public class TranslationAspect {


	@Pointcut("@annotation(com.youma.translate.annotation.Translate)")
	public void pointCut() {
		// do nothing
	}

	/**
	 * 方法返回拦截结果
	 * 
	 * @param joinPoint
	 * @param result
	 * @return
	 */
	@AfterReturning(returning = "result", pointcut = "pointCut()")
	public Object after(JoinPoint joinPoint, Object result) {
			TranslationUtil.translate(result);
		return result;
	}

	/**
	 * 方法参数拦截
	 * 
	 * @param joinPoint
	 */
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
		Parameter[] parameters = ms.getMethod().getParameters();
		for (int i = 0; i < parameters.length; i++) {
			//  参数上必须要有 @Translate 即可
			if (parameters[i].isAnnotationPresent(Translate.class)) {
				// 获取被注解的参数进行转义
				Object param = joinPoint.getArgs()[i];
				TranslationUtil.translate(param);
			}
		}
	}

}
