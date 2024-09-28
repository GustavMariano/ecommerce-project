package io.bootify.ecommerce_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String cep;

    @NotNull
    private String rua;

    @NotNull
    @Size(max = 10)
    private String numero;

    @NotNull
    @Size(max = 100)
    private String bairro;

    private String complemento;

    @NotNull
    @Size(max = 100)
    private String cidade;

    @NotNull
    @Size(max = 2)
    private String estado;
}
