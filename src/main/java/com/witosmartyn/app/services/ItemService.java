package com.witosmartyn.app.services;

import com.witosmartyn.app.config.constants.Errors;
import com.witosmartyn.app.config.exceptions.ItemNotFoundException;
import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.Image;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;


@Service()
@Slf4j
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ItemService {
    private ItemRepository itemRepository;
    private UserService userService;
    private CategoryService categoryService;

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item saveItemWithImages(@NotNull Item item, MultipartFile[] mpFiles) {
        Set<Image> newImages = mpFilesToListImages(item, mpFiles);
        if (!newImages.isEmpty()) {
            item.setAvatarId(new ArrayList<>(newImages).get(0).getName());
        }


        final Set<Image> oldImages = item.getImages();
        if (oldImages != null) {
            oldImages.addAll(newImages);
        } else {
            item.setImages(newImages);
            item.setUser(userService.principalUser());
        }
        log.info(" transactional? before saved");
        final Item saved = save(item);
        log.info(" transactional? before after saved");
        return saved;
    }

    private Set<Image> mpFilesToListImages(@NotNull Item item, MultipartFile[] mpFiles) {
        Set<Image> newImages = new HashSet<>();
        if (mpFiles != null && mpFiles.length > 0) {
            for (MultipartFile file : mpFiles) {
                if (file.getSize() > 0) {//minimal file_size in bytes
                    try {
                        final Image img = Image.builder()
                                .name(file.getOriginalFilename() + System.nanoTime())
                                .size(file.getSize())
                                .type(file.getContentType())
                                .pic(file.getBytes())
                                .item(item)//setParent
                                .build();
                        newImages.add(img);//add each image to collection
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return newImages;
    }

    public Item updateItemWithImages(@NotNull Item old, Item edited, MultipartFile[] mpFiles) {
        old.setName(edited.getName());
        old.setDescription(edited.getDescription());
        old.setCategory(edited.getCategory());
        old.setPrice(edited.getPrice());
        old.setCity(edited.getCity());
        old.setPhone(edited.getPhone());
        return saveItemWithImages(old, mpFiles);
    }

    public List<Item> findAll() {
        log.error("\n################################### ATTENTION ################################"+
                "\n##### INVOKED METHOD findAll Items - WITHOUT PAGE REQUEST  from storage ######");
        return new ArrayList<>();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public Item findByName(String name) {
        return itemRepository.findByName(name);
    }

    public Long count() {
        return itemRepository.count();
    }

    public Long countUseritems(User user) {
        return itemRepository.countItemByUser(user);
    }

    public Map<Category, Long> getItemCountInCategory() {
        log.debug("invoke countByCategoryMethod");
        Map<Category, Long> categoryAndCount = new HashMap<>();
        categoryService.findAll()
                .stream()
                .forEach(category -> {
                    categoryAndCount.put(category,itemRepository.countItemByCategory(category));
                });
        return categoryAndCount;
    }

    public Item findById(long id) throws ItemNotFoundException {

        final Optional<Item> optItem = itemRepository.findByid(id);

        if (optItem.isPresent()) {
            return optItem.get();
        } else {
            throw new ItemNotFoundException(Errors.ITEM_NOT_FOUND, id);
        }
    }


    public Page<Item> findByUserPageable(User user, PageRequest pageRequest) {
        return itemRepository.findByUser(user, pageRequest);
    }

    public void delete(Long id) {
        log.debug("delete itemid " + id);
        itemRepository.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Long> deleteByUser(User user) {
        log.debug("delete all Items witch have userID " + user.getUsername());
        return itemRepository.deleteItemsByUser(user);

    }
}
