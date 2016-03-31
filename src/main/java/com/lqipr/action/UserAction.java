package com.lqipr.action;

import com.lqipr.po.User;
import com.lqipr.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by lqipr on 2015/11/13.
 */
@Controller
@Path("/user")
public class UserAction {
    Logger logger = Logger.getLogger(UserAction.class);

    @Autowired
    UserService userService;

    @GET
    @Path(value = "/add.do")
    public String add(User user){
        try {
            userService.add(user);
        }catch (Exception e){
            logger.error(e);
        }
        return "add";
    }


    @GET
    @Path(value = "/update.do")
    public String update(User user){
        try {
            userService.update(user);
        }catch (Exception e){
            logger.error(e);
        }
        return "update";
    }
}
