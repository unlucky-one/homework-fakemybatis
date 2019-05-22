package com.raiden.homework.framework;

import java.lang.reflect.Proxy;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/5/12
 */
public class MyConfiguration {

    public final static ResourceBundle sqlMappings;

    static {
        sqlMappings = ResourceBundle.getBundle("application");
    }

    public <T> T getMapper(Class clazz, MySqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{clazz}, new MyMapperProxy(sqlSession));
    }
}
