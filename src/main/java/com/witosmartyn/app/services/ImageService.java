package com.witosmartyn.app.services;

import com.witosmartyn.app.entities.Image;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service()
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ImageService {

    private ImageRepository imageRepository;

    public Image save(Image image) {
        return imageRepository.save(image);
    }
    public Collection<Image> save(Collection<Image> images) {
        return imageRepository.save(images);
    }

    public Image findByName(String filename) {
        return imageRepository.findByName(filename);
    }

    public List<String> getStringsList(Item item) {
        final List<Image> images= imageRepository.findByItem(item);
        final List<String> names = new ArrayList<>();

        images.forEach(image -> names.add(image.getName()));
        return names;
    }

}
