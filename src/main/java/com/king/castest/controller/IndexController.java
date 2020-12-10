package com.king.castest.controller;

import com.king.castest.cas.CASAutoConfig;
import com.king.castest.session.LoginSession;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private CASAutoConfig casAutoConfig;

    @RequestMapping("")
    public String index(){
        getUserFromCAS();
        return "success";
    }

    @RequestMapping("/logout")
    public String hello(HttpServletRequest request){
        if("true".equals(request.getParameter("isTimeout"))){
            System.out.println("消除本地session....登出");
        }
        return "redirect:"+casAutoConfig.getServerUrlPrefix()+"/logout?service="+casAutoConfig.getClientHostUrl();
    }

    private void getUserFromCAS(){
        if (AssertionHolder.getAssertion().isValid()) {
            AttributePrincipal principal = AssertionHolder.getAssertion().getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            LoginSession.setUserName((String) attributes.get("username"));
            System.out.println("登录账户：" + attributes.get("username"));
        }
    }
}
