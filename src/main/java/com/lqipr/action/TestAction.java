package com.lqipr.action;

import com.alibaba.fastjson.JSONObject;
import com.lqipr.exception.MyException;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Created by lqipr on 2015/9/8.
 */
@Controller
@Path("/test")
public class TestAction {

    @GET
    @Path(value = "/list")
    public String list(@PathParam("name") String name)throws MyException {
        if(name == null){
            throw new RuntimeException("test runtime exception");
        }

        JSONObject json = new JSONObject();
        json.put("name", name);
        return json.toJSONString();
    }

    @GET
    @Path(value = "/helloStr")
    public String helloStr(){
        throw new RuntimeException("test runtime exception");
//        return "hello";
    }

}
