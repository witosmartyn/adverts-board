package com.witosmartyn.app.controllers;

import com.witosmartyn.app.config.components.MyMessageSource;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.PagesID;
import com.witosmartyn.app.config.constants.Redirect;
import com.witosmartyn.app.config.constants.VIEWS;
import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.config.exceptions.ItemNotFoundException;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.services.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * vitali
 */
@Controller
@RequestMapping(path = "/profile")
@Log4j
public class Profile {

    @Autowired
    public Profile(CityService cityService,
                   UserService userService,
                   CategoryService categoryService,
                   Search search,
                   ItemService itemService
                   ) {
        this.cityService = cityService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.search = search;
        this.itemService = itemService;
    }

    private CityService cityService;
    private Validator itemValidator;
    private UserService userService;
    private CategoryService categoryService;
    private Search search;
    @Autowired
    @Qualifier("myMessageSource")
    private MyMessageSource msg;

    private ItemService itemService;

    @ModelAttribute
    public void addAttrib(Model model,
                          HttpSession session,
    @Value("${spring.http.multipart.max-file-size:1024000}")String maxUploadFileSize) {
        String maxUploadFileSizeStr = maxUploadFileSize.replace("KB","");

//        final Long maxIntUploadFileSize = Long.valueOf(maxUploadFileSizeInt);
        model.addAttribute(ATTR_NAME.MAX_UPLOAD_FILE_SIZE, maxUploadFileSizeStr);
        model.addAttribute(ATTR_NAME.MSG_ERROR, msg.get("msg.validation.exceeded.max.file.size",maxUploadFileSize));
        model.addAttribute(ATTR_NAME.PAGE_TITLE, PagesID.PROFILE);

        if (session.getAttribute(ATTR_NAME.USER_ITEMS_COUNT) == null) {
            Long userItemsCount = itemService.countUseritems(userService.principalUser());
            session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT, userItemsCount);
        }

    }


    @GetMapping(value = {"/", "", "/items"})
    public String showAllItems(
            @RequestParam(required = false, value = ATTR_NAME.PAGE, defaultValue = "0") int page,
            @RequestParam(required = false, value = ATTR_NAME.COUNT_ON_PAGE, defaultValue = "4") int countOnPage,
            @RequestParam(required = false, value = ATTR_NAME.SORT_BY, defaultValue = "updated") ItemField sortBy,
            @RequestParam(required = false, value = ATTR_NAME.ORDER_BY, defaultValue = "DESC") Sort.Direction orderBy,
            Model model) {
        final PageRequest pageRequest = new PageRequest(page, countOnPage, new Sort(new Sort.Order(orderBy, sortBy.val())));

        model.addAttribute(ATTR_NAME.ITEMS, itemService.findByUserPageable(userService.principalUser(), pageRequest));
        model.addAttribute(ATTR_NAME.CURRENT_PAGE, page);
        model.addAttribute(ATTR_NAME.COUNT_ON_PAGE, countOnPage);
        model.addAttribute(ATTR_NAME.ORDER_BY, orderBy);
        model.addAttribute(ATTR_NAME.SORT_BY, sortBy);
        return VIEWS.USER_PROFILE_PAGE;
    }

    @GetMapping(value = "/items/{id}")
    public String showItem(@PathVariable("id") long id, Model model) throws ItemNotFoundException {

        return search.showById(id, model);
    }

    @GetMapping(value = "/items/new")
    public String addItem(Model model) {
        model.addAttribute(ATTR_NAME.ITEM, new Item());
        model.addAttribute(ATTR_NAME.ALL_CATEGORIES, categoryService.findAll());
        model.addAttribute(ATTR_NAME.ALL_CITIES, cityService.findAll());
        model.addAttribute(ATTR_NAME.PAGE_ID, PagesID.ADD_ITEM);

        return VIEWS.EDIT_ITEM_PAGE;
    }

    @PreAuthorize("isOwnerOrAdmin(#id)")
    @GetMapping(value = "/items/{id}/edit")
    public String editItem(@PathVariable("id") long id, Model model, HttpSession session) throws ItemNotFoundException {
        final Item item = itemService.findById(id);

        session.setAttribute(ATTR_NAME.ITEM, item);
        model.addAttribute(ATTR_NAME.ITEM, item);
        model.addAttribute(ATTR_NAME.ALL_CATEGORIES, categoryService.findAll());
        model.addAttribute(ATTR_NAME.ALL_CITIES, cityService.findAll());
        model.addAttribute(ATTR_NAME.PAGE_ID, PagesID.EDIT_ITEM);
        return VIEWS.EDIT_ITEM_PAGE;
    }

    @PreAuthorize("isOwnerOrAdmin(#id)")
    @PostMapping("/items/new")
    public String update(
            @RequestParam(value = "id", required = false) Long id,
            /*@Valid*/ Item edited,
            @RequestParam(value = "mpFiles", required = false) MultipartFile[] mpFiles,
            BindingResult bindingResult, Model model, RedirectAttributes flash, HttpSession session) {
            itemValidator.validate(edited, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTR_NAME.ITEM, edited);
            model.addAttribute(ATTR_NAME.ALL_CATEGORIES, categoryService.findAll());
            model.addAttribute(ATTR_NAME.ALL_CITIES, cityService.findAll());
            model.addAttribute(ATTR_NAME.PAGE_TITLE, PagesID.ADD_ITEM);
            return VIEWS.EDIT_ITEM_PAGE;
        }


//       if item new
        if (edited.getId() == null) {
            Item savedItem = itemService.saveItemWithImages(edited, mpFiles);
                if (log.isDebugEnabled()) {log.debug("item "+savedItem.getName()+"success saved in database");}
            final String flashMessage = msg.get("msg.adv.success.added", savedItem.getName());
            flash.addFlashAttribute(ATTR_NAME.MSG_SUCCESS, flashMessage);
            session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT,(Long )session.getAttribute(ATTR_NAME.USER_ITEMS_COUNT)+1);
            return Redirect.TO_PROFILE;
        }

        //else item is exist
        final Item itemOld = (Item) session.getAttribute(ATTR_NAME.ITEM);

        itemService.updateItemWithImages(itemOld, edited, mpFiles);
        String flashMessage;
        log.info("item with id " + itemOld.getId() + " successfully_updated");
        flashMessage = msg.get("msg.adv.success.updated", itemOld.getName());
        flash.addFlashAttribute(ATTR_NAME.MSG_SUCCESS, flashMessage);
        return Redirect.TO_PROFILE;
    }

    @PreAuthorize("isOwnerOrAdmin(#id)")
    @PostMapping("/items/delete")
    public String delete(@RequestParam("id") long id,HttpSession session) {
        System.out.println(id);
        itemService.delete(id);
        session.setAttribute(ATTR_NAME.USER_ITEMS_COUNT,(Long )session.getAttribute(ATTR_NAME.USER_ITEMS_COUNT)-1);

        return Redirect.TO_PROFILE;
    }

    @PostMapping("/items/deleteAll")
    public String deleteAll( RedirectAttributes flash,HttpSession session) {
        List<Long> idsDeleted = itemService.deleteByUser(userService.principalUser());

        log.info("items cont" + idsDeleted.size() + " successfully_DELETED");
        String flashMessage;
        flashMessage = msg.get("msg.adv.success.updated", idsDeleted.size());
        flash.addFlashAttribute(ATTR_NAME.MSG_SUCCESS, flashMessage);
        session.removeAttribute(ATTR_NAME.USER_ITEMS_COUNT);
        return Redirect.TO_PROFILE;
    }

    @Autowired
    @Qualifier("itemValidator")
    public void setItemValidator(Validator itemValidator) {
        this.itemValidator = itemValidator;
    }
}
