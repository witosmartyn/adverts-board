package com.witosmartyn.app.controllers;

import com.witosmartyn.app.services.ImageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * vitali
 */
@Controller
@RequestMapping(path = "/files")
@Log4j
public class FileController {
    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    private ImageService imageService;


    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable String filename) {
        if (log.isDebugEnabled()){log.debug("image request id:"+filename);}
        HttpHeaders headers = new HttpHeaders();
        byte[] media = imageService.findByName(filename).getPic();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_JPEG);

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

}
