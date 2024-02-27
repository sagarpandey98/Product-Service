package com.springproject.productservice.Inheritence.jointable;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "jt_instructor")
@PrimaryKeyJoinColumn(name = "User_id")
public class Instructor extends User {
    private String favStudent;
}
