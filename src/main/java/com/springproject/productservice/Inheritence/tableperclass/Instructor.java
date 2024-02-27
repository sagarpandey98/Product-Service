package com.springproject.productservice.Inheritence.tableperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tbc_Instructor")
public class Instructor extends User {
}
