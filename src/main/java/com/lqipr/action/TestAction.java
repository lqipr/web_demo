package com.lqipr.action;

import com.lqipr.core.util.ResponseUtil;
import com.lqipr.po.User;
import com.lqipr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created by lqipr on 2015/9/8.
 */
@Controller
@Path("/test")
public class TestAction {

    @Autowired
    UserService userService;

    @GET
    @Path(value = "/list/{id}")
    public String list(@QueryParam("name") String name, @PathParam("id") int id) {
        if(name == null){
            throw new RuntimeException("test runtime exception");
        }
        List<User> result = userService.getUserPageById(id, name, 0, 20);
        return ResponseUtil.getResponseMessage(ResponseUtil.SUCCESS_STATUS, "成功", result);
    }

    @GET
    @Path(value = "/test_error")
    public String testError(){
        throw new RuntimeException("test runtime exception");
    }

}
