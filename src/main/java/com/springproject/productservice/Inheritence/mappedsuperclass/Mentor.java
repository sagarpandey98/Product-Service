package com.springproject.productservice.Inheritence.mappedsuperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "mpc_Mentor")
public class Mentor extends User{
    @Id
    private long Id;
    private int rating;

}
