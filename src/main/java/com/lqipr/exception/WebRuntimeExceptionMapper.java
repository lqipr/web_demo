package com.lqipr.exception;


import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by lqipr on 2016/3/25.
 */
@Provider
public class WebRuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    Logger logger = Logger.getLogger(WebRuntimeExceptionMapper.class);
    @Override
    public Response toResponse(RuntimeException exception)
    {


        logger.error("RuntimeException:", exception);
        //默认错误处理方式
        return returnResult(exception.getMessage());
    }

    private Response returnResult(String msg){
        return Response.status(Response.Status.OK).entity(msg).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}
