package com.raiden.homework.mapper;

import com.raiden.homework.model.Person;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/5/20
 */
public interface PersonMapper {
    Person getPersonById(Long id);
}
