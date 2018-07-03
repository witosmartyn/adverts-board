package com.witosmartyn.app.controllers;

import com.witosmartyn.app.config.components.StatisticsBean;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.PagesID;
import com.witosmartyn.app.config.constants.VIEWS;
import com.witosmartyn.app.services.ItemService;
import com.witosmartyn.app.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * User: vitali
 */
@Log4j
@Controller
public class Home {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatisticsBean statisticsBean;


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest httpSReq) {
        statisticsBean.addGuestToStatistic(httpSReq.getRequestedSessionId(),httpSReq.getRemoteUser()+" : "+httpSReq.getRemoteAddr());
    }

    @RequestMapping({"/", ""})
    public String getMainPage(Model model, HttpSession session)  {

        if (session.getAttribute(ATTR_NAME.USER_ITEMS_COUNT) == null) {
            Long userItemsCount = itemService.countUseritems(userService.principalUser());
            session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT, userItemsCount);
        }
        return "index";
    }

    @RequestMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,

                                Model model) {


        model.addAttribute(ATTR_NAME.MSG_ERROR, error != null);
        model.addAttribute(ATTR_NAME.MSG_LOGOUT, logout != null);
        return VIEWS.LOGIN_PAGE;
    }


}
