package com.witosmartyn.app.config.methodSecurity;

import com.witosmartyn.app.services.ItemService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

/**
 * vitali
 */
@Configuration
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private ItemService itemService;
    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    public void setReturnObject(Object returnObject, EvaluationContext ctx) {
        ((CustomMethodSecurityExpressionRoot) ctx.getRootObject().getValue()).setReturnObject(returnObject);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                              MethodInvocation invocation) {
        final CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setItemService(this.itemService);

        return root;
    }
}
