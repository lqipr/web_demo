package com.lqipr.action;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lqipr on 2015/9/8.
 */

@Controller
@RequestMapping( "/test" )
public class TestAction {

    @RequestMapping("/hello.do")
    public ModelAndView hello(){
        ModelAndView mav = new ModelAndView("hello");
        return mav;
    }

    @RequestMapping("/hello2.do")
    public ModelAndView hello2(){
        ModelAndView mav = new ModelAndView("hello");
        return mav;
    }

    @RequestMapping("/hello_str.do")
    public String helloStr(){
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/list.do")
    public String list(
//            @RequestHeader("name")
            String name){
        JSONObject json = new JSONObject();
        json.put("name", name);
        return json.toJSONString();
    }

}
