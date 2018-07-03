package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.Image;
import com.witosmartyn.app.entities.Item;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service()
@Log4j
public class ImageProcessor {
    private final String IMG_TEMPLATE_ID = "img";
    @Value("${app.config.item.image.compress-image-enabled:false}")
    private boolean compresImageEnabled;

    @Value("#{'${app.config.item.image.aloved-media-types}'.split(',')}")
    private List<String> alovedMediaTypes;


    public Set<Image> convertFiles(@NotNull Item item, MultipartFile[] mpFiles) {
        Set<Image> newImages = new HashSet<>();
        if (mpFiles != null && mpFiles.length > 0) {
            for (MultipartFile file : mpFiles) {

                if (file.getSize() > 0
                        && alovedMediaTypes.contains(file.getContentType())) {//minimal file_size in bytes
                    byte[] image=null;
                    if (compresImageEnabled) {
                        image= compresImage(file);
                    }else {
                        try {
                            image = file.getBytes();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    final Image img = Image.builder()
                            .name(String.valueOf(IMG_TEMPLATE_ID+System.nanoTime()))
                            .size(file.getSize())
                            .type(file.getContentType())
                            .pic(image)
                            .item(item)//setParent
                            .build();
                    newImages.add(img);//add each image to collection
                }

            }
        }
        return newImages;
    }

    private byte[] compresImage(MultipartFile mfile)  {
        byte[] result =null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)){

            BufferedImage image = ImageIO.read(mfile.getInputStream());

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.3f);  // Change the quality value you prefer

            writer.write(null, new IIOImage(image, null, null), param);
            result = baos.toByteArray();
            writer.dispose();

        } catch (IOException e) {
            result = mfile.getBytes();
            log.error("error process compress image");
        }finally {
            return  result;
        }


    }

}
