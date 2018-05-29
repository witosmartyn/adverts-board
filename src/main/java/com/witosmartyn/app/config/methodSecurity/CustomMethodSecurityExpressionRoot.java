package com.witosmartyn.app.config.methodSecurity;

import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.services.ItemService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * vitali
 */
public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private ItemService itemService;


    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }


    public boolean isOwner(Long itemId) {

        if (itemIsNew(itemId)) return true; //if itemId is null then item is new and need allow access

        User user = (User) this.getPrincipal();
        final Long ownerId = itemService.findOne(itemId).getUser().getId();
        final boolean isOwner = ownerId.equals(user.getId());
        return isOwner;
    }

    private boolean itemIsNew(Long itemId) {
        if (itemId == null) {
            return true;
        }
        return false;
    }

    public boolean isOwnerOrAdmin(Long itemId) {
        return isOwner(itemId) || isAdmin();
    }

    public boolean isAdmin() {
        boolean isAdmin = false;
        for (GrantedAuthority grantedAuth : this.getAuthentication().getAuthorities()) {
            if (grantedAuth.getAuthority().toUpperCase().contains("ADMIN")) {
                isAdmin = true;
            }
        }

        return isAdmin;
    }


    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}
