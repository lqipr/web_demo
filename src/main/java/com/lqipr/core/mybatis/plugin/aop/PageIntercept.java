package com.lqipr.core.mybatis.plugin.aop;

import com.lqipr.core.mybatis.plugin.PageThreadHelp;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 设置根据service名字自动鉴定设置分页方式  (object.., int pageIndex, int limit)
 * Created by lqipr on 2015/9/17.
 */
@Aspect
@Component
public class PageIntercept {

	Logger logger = Logger.getLogger(PageIntercept.class);

	@Around("execution (* com.lqipr.service.*.*(..))")
	public Object pointcut(ProceedingJoinPoint point) throws Throwable {
		if(point.getSignature().getName().endsWith("PageById") || point.getSignature().getName().endsWith("PageByPage")) {
			try{
				Object[] args = point.getArgs();
				if(args!=null && args.length >= 2) {
					int pageIndex = (int)args[args.length - 2];
					int limit = (int)args[args.length - 1];
					if (point.getSignature().getName().endsWith("PageById"))
						PageThreadHelp.setPageById(pageIndex, limit);
					if (point.getSignature().getName().endsWith("PageByPage"))
						PageThreadHelp.setPageByPage(pageIndex, limit);
				}
			}catch (Exception e){
				logger.error(e);
			}

		}
		return point.proceed();
	}

}
