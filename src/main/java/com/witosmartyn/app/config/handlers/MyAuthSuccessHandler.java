package com.witosmartyn.app.config.handlers;

/**
 * @author Vitalii Martyniuk
 * Handle process after user input valid credentials
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("myAuthSuccessHandler")
public class MyAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${app.config.authentication.admin.redirect.enabled:false}")
    private boolean adminRedirectEnabled;

    @Value("${app.config.authentication.admin.redirect.url:/}")
    private String adminRedirectUrl;
    @Value("${app.config.authentication.user.redirect.url:/}")
    private String userRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("AT onAuthenticationSuccess(...) function!");

        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);


        redirectProcess(response, authentication);
    }

    /**
     * Resolve redirect URL
     */
    private void redirectProcess(HttpServletResponse response, Authentication authentication) throws IOException {
         boolean isAdmin = false;

         if (adminRedirectEnabled) {
            isAdmin = isAdmin(authentication);
        }
        if (!isAdmin) {
            response.sendRedirect(userRedirectUrl);
        } else {
            response.sendRedirect(adminRedirectUrl);
        }
    }

    /**
     * @return boolean result true if user has role admin.
     */
    private boolean isAdmin(Authentication authentication) {
        boolean result = false;
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ADMIN".equals(auth.getAuthority())) {
                result = true;
            }
        }
        return result;
    }
}
