package com.raiden.homework;

import com.raiden.homework.framework.MyConfiguration;
import com.raiden.homework.framework.MyExcutor;
import com.raiden.homework.framework.MySqlSession;
import com.raiden.homework.mapper.PersonMapper;
import com.raiden.homework.model.Person;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/5/12
 */
public class test {
    public static void main(String[] args) {
        MySqlSession sqlSession = new MySqlSession(new MyConfiguration(), new MyExcutor());
        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
        Person person = mapper.getPersonById(1l);
        System.out.println(person.toString());
    }
}
