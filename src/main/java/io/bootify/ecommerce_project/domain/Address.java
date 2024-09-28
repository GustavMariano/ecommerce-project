package io.bootify.ecommerce_project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Addresses")
@Getter
@Setter
public class Address {

        @Id
        @Column(nullable = false, updatable = false)
        @SequenceGenerator(name = "primary_sequence", sequenceName = "primary_sequence", allocationSize = 1, initialValue = 10000)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
        private Long id;

        @Column(nullable = false, length = 20)
        private String cep;

        @Column(nullable = false)
        private String rua;

        @Column(nullable = false, length = 10)
        private String numero;

        @Column(nullable = false, length = 100)
        private String bairro;

        @Column
        private String complemento;

        @Column(nullable = false, length = 100)
        private String cidade;

        @Column(nullable = false, length = 2)
        private String estado;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id", nullable = false)
        private Customer customer;

}
