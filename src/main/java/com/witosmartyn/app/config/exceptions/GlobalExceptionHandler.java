package com.witosmartyn.app.config.exceptions;

import com.witosmartyn.app.config.components.MyMessageSource;
import com.witosmartyn.app.config.constants.ATTR_NAME;
import com.witosmartyn.app.config.constants.VIEWS;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

/**
 * Handler used for global catch different exception
 *
 * @author Vitalii Martyniuk
 */
@ControllerAdvice
@Log4j
public class GlobalExceptionHandler {
    @Autowired
    @Qualifier("myMessageSource")
    private MyMessageSource msg;

    @Value("${spring.http.multipart.max-file-size:1024000}")
    private String maxUploadFileSize;

    @Value("${spring.http.multipart.max-request-size:1024000}")
    private String maxUploadRequestSize;
    /**
     * Catching MultipartException that throws when user loaded data exceeded aloved maximal request size
     * or maximal size each file.
     * When this will happened this advice will show error page with message
     * @see MultipartException
     * @author Vitalii Martyniuk
     */
    @ExceptionHandler(MultipartException.class)
    public String handleMultipartEx(MultipartException e, Model model) {
        if (e.getMessage().toLowerCase().contains("request was rejected because its size")) {
            model.addAttribute("error", msg.get("msg.validation.exceeded.max.request.size",maxUploadRequestSize));
            log.error(msg.get("msg.validation.exceeded.max.request.size", maxUploadRequestSize));

        }
        if (e.getMessage().toLowerCase().contains("field mpfiles exceeds its maximum permitted size of")) {
            model.addAttribute("error", msg.get("msg.validation.exceeded.max.file.size", maxUploadFileSize));
            log.error(msg.get("msg.validation.exceeded.max.file.size", maxUploadFileSize));
        }
        return VIEWS.ERROR_PAGE;

    }

    /**
     * Catching ItemNotFoundException that throws when client side probe load non Exist Item  by ID from the data store.
     * When this will happen this advice will show error page with message
     *
     * @see ItemNotFoundException
     *
     * @author Vitalii Martyniuk
     */
   @ExceptionHandler(ItemNotFoundException.class)
    public String handleItemNotFoundEx(ItemNotFoundException e, Model model) {
       model.addAttribute(ATTR_NAME.MSG_ERROR, e.getId()+"-> Item not found in DB");
        log.error(e.getId()+"-> Item not found in DB");


        return VIEWS.ERROR_PAGE;

    }



}
