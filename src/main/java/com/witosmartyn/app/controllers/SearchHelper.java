package com.witosmartyn.app.controllers;


import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.ItemConfig;
import com.witosmartyn.app.config.constants.Msg;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.model.SearchRequest;
import com.witosmartyn.app.repositories.SearchRepository;
import com.witosmartyn.app.services.*;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


/**
 * User: vitali
 */
@Log4j
@Service
@NoArgsConstructor
public class SearchHelper {


    private SearchRepository searchRepository;
    private SearchService searchService;

    @Autowired
    public SearchHelper(SearchService searchService,
                        @Qualifier("hiber") SearchRepository searchRepository) {
        this.searchService = searchService;
        this.searchRepository = searchRepository;
    }

    public void process(SearchRequest sR, Model model) {
        addToModel(sR, model);
        fromPriceHandler(sR, model);
        toPriceHandler(sR, model);
        sR.setPageRequest(buildPageRequest(sR));
        //todo delete by example impl
        if (sR.isByExample()) {
            final Page<Item> resultPage = searchService.findByExample(sR);
            model.addAttribute(ATTR_NAME.ITEMS_PAGE, resultPage);
        } else {
            final Page<Item> list = searchRepository.searchByRequest(sR,sR.getPageRequest());
            model.addAttribute(ATTR_NAME.ITEMS_PAGE, list);
        }
    }

    private PageRequest buildPageRequest(SearchRequest sR) {
        Sort sort = new Sort(new Sort.Order(sR.getOrderBy(), sR.getSortBy().val()));
        return new PageRequest(sR.getPage(), sR.getCountOnPage(), sort);
    }

    private void fromPriceHandler(SearchRequest sR, Model model) {
        if (sR.getFromPrice() == null || sR.getFromPrice() == ItemConfig.PRICE_ZERO) {
            sR.setFromPrice(ItemConfig.PRICE_ZERO);
            model.addAttribute(ATTR_NAME.FROM_PRICE, Msg.EMPTY_STRING);
        } else {
            model.addAttribute(ATTR_NAME.FROM_PRICE, sR.getFromPrice());

        }
    }

    private void toPriceHandler(SearchRequest sR, Model model) {
        if (sR.getToPrice() == null || sR.getToPrice() == ItemConfig.PRICE_MAX) {
            sR.setToPrice(ItemConfig.PRICE_MAX);
            model.addAttribute(ATTR_NAME.TO_PRICE, Msg.EMPTY_STRING);
        } else {
            model.addAttribute(ATTR_NAME.TO_PRICE, sR.getToPrice());
        }
    }

    private void addToModel(SearchRequest sR, Model model) {
        model.addAttribute(ATTR_NAME.QUERY, sR.getQuery());
        model.addAttribute(ATTR_NAME.CURRENT_PAGE, sR.getPage());
        model.addAttribute(ATTR_NAME.COUNT_ON_PAGE, sR.getCountOnPage());
        model.addAttribute(ATTR_NAME.SELECTED_CITY, sR.getCity());
        model.addAttribute(ATTR_NAME.SELECTED_CATEGORY, sR.getCategory());
        model.addAttribute(ATTR_NAME.SEARCH_IN_DESCRIPTION, sR.isSearchInDescription());
        model.addAttribute(ATTR_NAME.IGNORE_CASE, sR.isIgnoreCase());
        model.addAttribute(ATTR_NAME.ORDER_BY, sR.getOrderBy());
        model.addAttribute(ATTR_NAME.SORT_BY, sR.getSortBy());
        model.addAttribute(ATTR_NAME.BY_EXAMPLE, sR.isByExample());
    }


}
