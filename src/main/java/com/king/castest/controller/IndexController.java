package com.king.castest.controller;

import com.king.castest.cas.CASAutoConfig;
import com.king.castest.session.LoginSession;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String hello(){
        return "redirect:"+casAutoConfig.getServerUrlPrefix()+"/logout";
    }

    private void getUserFromCAS(){
        if (AssertionHolder.getAssertion().isValid()) {
            AttributePrincipal principal = AssertionHolder.getAssertion().getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            LoginSession.setUserName((String) attributes.get("loginName"));
        }
    }
}
