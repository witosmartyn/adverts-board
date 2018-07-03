package com.witosmartyn.app.controllers;


import com.witosmartyn.app.config.components.StatisticsBean;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.PagesID;
import com.witosmartyn.app.config.constants.VIEWS;
import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.config.exceptions.ItemNotFoundException;
import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.City;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.entities.model.SearchRequest;
import com.witosmartyn.app.services.*;
import lombok.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;


/**
 * User: vitali
 */
@Log4j
@Controller
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class Search {

    private CityService cityService;
    private CategoryService categoryService;
    private ItemService itemService;
    private UserService userService;
    private ImageService imageService;
    private SearchHelper searchHelper;

    private StatisticsBean statisticsBean;


    @ModelAttribute
    public void addAttributes(Model model,HttpSession session) {
        model.addAttribute(ATTR_NAME.ALL_CATEGORIES, categoryService.findAll());
        model.addAttribute(ATTR_NAME.ALL_CITIES, cityService.findAll());
        model.addAttribute(ATTR_NAME.PAGE_TITLE, PagesID.SEARCH);
        Optional userOptional = userService.userOptional();
        if (userOptional.isPresent()) {
                Long userItemsCount = itemService.countUseritems((User) userOptional.get());
                session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT, userItemsCount);
            }

    }

    @GetMapping(value = "/search")
    public String searchByParams(
            @RequestParam(required = false, value = ATTR_NAME.PAGE, defaultValue = "0") int page,
            @RequestParam(required = false, value = ATTR_NAME.COUNT_ON_PAGE, defaultValue = "6") int countOnPage,
            @RequestParam(required = false, value = ATTR_NAME.QUERY, defaultValue = "") String query,
            @RequestParam(required = false, value = ATTR_NAME.SELECTED_CITY) City city,
            @RequestParam(required = false, value = ATTR_NAME.SELECTED_CATEGORY) Category category,
            @RequestParam(required = false, value = ATTR_NAME.BY_EXAMPLE, defaultValue = "false") boolean byExample,
            @RequestParam(required = false, value = ATTR_NAME.SORT_BY, defaultValue = "updated") ItemField sortBy,
            @RequestParam(required = false, value = ATTR_NAME.ORDER_BY, defaultValue = "DESC") Sort.Direction orderBy,
            @RequestParam(required = false, value = ATTR_NAME.SEARCH_IN_DESCRIPTION) boolean searchInDescription,
            @RequestParam(required = false, value = ATTR_NAME.IGNORE_CASE) boolean ignoreCase,

            @RequestParam(required = false, value = ATTR_NAME.FROM_PRICE, defaultValue = "0") Double fromPrice,
            @RequestParam(required = false, value = ATTR_NAME.TO_PRICE, defaultValue = "9999999") Double toPrice,
            @RequestParam Map<String, String> allRequestParams,

            Model model) {
        if (log.isDebugEnabled()){
        log.debug("Request Params " + allRequestParams);
        }
        final SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .fromPrice(fromPrice)
                .toPrice(toPrice)
                .page(page)
                .orderBy(orderBy)
                .sortBy(sortBy)
                .category(category)
                .city(city)
                .countOnPage(countOnPage)
                .searchInDescription(searchInDescription)
                .ignoreCase(ignoreCase)
                .byExample(byExample)
                .build();//buiild searchRequest object

        searchHelper.process(searchRequest, model);
        model.addAttribute("itemsStatisticCount", statisticsBean.getAdvertsAmount());
        model.addAttribute("usersStatisticCount", statisticsBean.getUsersRegisteredAmount());
        return VIEWS.SEARCH_PAGE;

    }


    @GetMapping("/search/items/{id}")
    public String showById(
            @PathVariable(value = "id", required = true) long id,
            Model model) throws  ItemNotFoundException{
        final Item item = itemService.findById(id);
        model.addAttribute(ATTR_NAME.ITEM, item);
        model.addAttribute(ATTR_NAME.IMAGE_STRING_LIST, imageService.getStringsList(item));

        return VIEWS.SHOW_ITEM_PAGE;

    }


}
