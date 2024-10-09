package com.fudianbank.realtimedatagenerator.entity;

import com.github.javafaker.Faker;
import lombok.Data;

import java.util.Locale;

/**
 * @author Charles
 * @Date 2024-09-30 14:31
 * Belongs to Fudian Bank
 */
@Data
public class StudentDO {
    int sid;
    String sname;
    int sage;
    String ssex;
    int randomCode;
    Faker faker = new Faker(Locale.CHINA);

    public StudentDO(int sid) {
        this.sid = sid;
        sname = faker.name().fullName();
        sage = faker.number().numberBetween(18, 60);
        ssex = faker.options().option("Male", "Female");
        randomCode = faker.number().numberBetween(10000, 99999);
    }
}