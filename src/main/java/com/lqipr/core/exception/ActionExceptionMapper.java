package com.lqipr.core.exception;


import com.lqipr.core.util.ResponseUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Action捕获所有Error
 * 必须基于 Rest easy
 * 1.xml配置
 * <context:component-scan base-package="com.lqipr.core.exception">
 * <context:include-filter type="annotation" expression="javax.ws.rs.ext.Provider"/>
 * </context:component-scan>
 * 2.注解注入
 * @Autowired
 * public void setActionExceptionMapper(){
 *      ResteasyProviderFactory.getInstance().register(new ActionExceptionMapper());
 * }
 *
 * Created by lqipr on 2016/3/25.
 */
@Provider
public class ActionExceptionMapper implements ExceptionMapper<Exception> {
    Logger logger = Logger.getLogger(ActionExceptionMapper.class);
    @Override
    public Response toResponse(Exception exception)
    {
        //业务异常
        if(exception instanceof BusinessException){
            return returnResult(ResponseUtil.getResponseMessage(exception.getMessage()));
        }
        //业务错误状态码
        if(exception instanceof StatusException){
            StatusException e = (StatusException)exception;
            return returnResult(ResponseUtil.getResponseMessage(e.getStatus(), e.getMessage()));
        }

        logger.error("ActionExceptionMapper:", exception);
        //默认错误处理方式
        return returnResult(ResponseUtil.getDefaultMsg());
    }

    private Response returnResult(String json){
        return Response.status(Response.Status.OK).entity(json).type(MediaType.APPLICATION_JSON).build();
    }

}
