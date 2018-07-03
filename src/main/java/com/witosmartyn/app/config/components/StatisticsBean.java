package com.witosmartyn.app.config.components;

import com.witosmartyn.app.entities.Category;

import com.witosmartyn.app.services.CategoryService;
import com.witosmartyn.app.services.ItemService;
import com.witosmartyn.app.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Vitalii Martyniuk
 * <p>
 * Collect site statistics
 */
@Component
@Scope(value = "singleton")
@Log4j
public class StatisticsBean {

    private ItemService items;
    private UserService users;
    private CategoryService categoryService;

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    private ItemService itemService;

    private final static String MSG_UPDATE_REGISTEREED_USERS_AMOUNT = "Update registereed amount users ";
    private final static String MSG_UPDATE_All_ITEMS_AMOUNT = "Update all amount items ";
    private final static String MSG_UPDATE_AVAILABLE_CATEGORIES = "Update available categories ";

    //STATISTICS values
    private Long advertsAmount;
    private Long usersRegisteredAmount;

//    private List<Category> availibleCategories = new ArrayList<>();
    private Map<Category, Long> countItemByCategory;



    private Map<String, String> ipStatistic;

    /**
     * @return map
     */
    public Map<Category, Long> getCountItemByCategory() {
        return countItemByCategory;
    }

    /**
     * Setter-Injection dependency by Spring IoC-Container
     */
    @Autowired
    public void setItems(ItemService items) {
        this.items = items;
    }

    /**
     * Setter-Injection dependency by Spring IoC-Container
     */
    @Autowired
    public void setUsers(UserService users) {
        this.users = users;
    }

    /**
     * Setter-Injection dependency by Spring IoC-Container
     */
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void init() {
        updateAvailableCategories();
        this.ipStatistic = new HashMap<>();
    }

//    /**
//     * Evaluates category list  @link {@link #availibleCategories}
//     */
    public void updateAvailableCategories() {
//        if (log.isDebugEnabled()) {
//            log.debug(MSG_UPDATE_AVAILABLE_CATEGORIES);
//        }
//        this.availibleCategories = (List<Category>) categoryService.findAll();
    }

    /**
     * Evaluates count  @link {@link #advertsAmount}
     */
    public void updateAdvertsOnSite() {
        if (log.isDebugEnabled()) {
            log.debug(MSG_UPDATE_All_ITEMS_AMOUNT);
        }
        this.advertsAmount = items.count();
    }

    /**
     * Evaluates count {@link #usersRegisteredAmount}
     * of registered @see {@link com.witosmartyn.app.entities.User} in System
     */
    public void updateRegisteredUsers() {
        if (log.isDebugEnabled()) {
            log.debug(MSG_UPDATE_REGISTEREED_USERS_AMOUNT);
        }
        this.usersRegisteredAmount = users.count();
    }

    /**
     * @return Map amount each category
     *///
    public void updateCountItemByCategory() {
        this.countItemByCategory = itemService.getItemCountInCategory();
        log.debug("updateCountOfCategory");
    }

    /**
     * Update all statistic fields
     */


    /**
     * @return amount of usersRegistered
     */

    public Long getUsersRegisteredAmount() {
        return usersRegisteredAmount;
    }

    public Collection<Category> getAvailibleCategories() {
        return categoryService.findAll();
//        return this.availibleCategories;
    }

    /**
     * @return  amount of all Item
     */
    public Long getAdvertsAmount() {
        return advertsAmount;
    }

    /**
     * Add to map all new guest
     */
    public void addGuestToStatistic(String requestedSessionId, String ipAndUsername) {
        this.ipStatistic.put(requestedSessionId, ipAndUsername);
    }
    /**
     * @return Map all guest (reqSessionID,ip+userName)
     */
    public Map<String, String> getIpStatistic() {
        return ipStatistic;
    }

}
