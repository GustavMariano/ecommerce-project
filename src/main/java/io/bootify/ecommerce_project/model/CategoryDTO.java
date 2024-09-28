package io.bootify.ecommerce_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

}
