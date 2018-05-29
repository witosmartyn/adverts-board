package com.witosmartyn.app.config.exceptions;

import com.google.common.base.Suppliers;
import com.witosmartyn.app.entities.Item;

import java.util.function.Supplier;

/**
 * Can be threw when user tried load non Exist Item by ID from the data store.
 *
 * @author Ken Krebs
 */
public class ItemNotFoundException extends Exception implements Supplier  {


    private Long id;

    public ItemNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    @Override
    public Object get() {
        return null;
    }
}
