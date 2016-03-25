package com.lqipr.action;

import com.lqipr.po.User;
import com.lqipr.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lqipr on 2015/11/13.
 */
@Controller
@RequestMapping("/user")
public class UserAction {
    Logger logger = Logger.getLogger(UserAction.class);

    @Autowired
    UserService userService;

    @RequestMapping("/add.do")
    public ModelAndView add(User user){
        ModelAndView mav = new ModelAndView("success");
        try {
            userService.add(user);
            mav.addObject("msg","新增用户成功");
        }catch (Exception e){
            mav.setViewName("error");
            mav.addObject("msg",e.toString());
            logger.error(e);
        }
        return mav;
    }


    @RequestMapping("/update.do")
    public ModelAndView update(User user){
        ModelAndView mav = new ModelAndView("success");
        try {
            userService.update(user);
            mav.addObject("msg","修改用户成功");
        }catch (Exception e){
            mav.setViewName("error");
            mav.addObject("msg",e.toString());
            logger.error(e);
        }
        return mav;
    }
}
