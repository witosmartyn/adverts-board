package com.witosmartyn.app.entities.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * vitali
 */
@Data
@NoArgsConstructor

public class UserDto {

    @NotNull
    @NotEmpty
    @Size(min = 4,max = 64)
    private String username;


    @NotNull
    @NotEmpty
    @Size(min = 4,max = 20)
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 4,max = 20)
    private String confirm;


}
