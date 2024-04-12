package com.example.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Embeddable // 다른 엔티티에서 이객체를 포함해서 사용할거다
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
