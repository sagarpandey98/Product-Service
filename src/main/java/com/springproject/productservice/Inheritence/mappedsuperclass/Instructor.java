package com.springproject.productservice.Inheritence.mappedsuperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "mpc_Instructor")
public class Instructor extends User{
    @Id
    private long Id;
    private String favStudent;
}
