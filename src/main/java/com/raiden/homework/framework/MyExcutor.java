package com.raiden.homework.framework;

import com.mysql.cj.jdbc.CallableStatement;
import com.raiden.homework.framework.annotations.MyColumn;
import com.raiden.homework.model.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/5/12
 */
public class MyExcutor {
    Properties properties = null;

    public <T> T query(String sql, Object[] param) {
        try {
            properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/application.properties"));

            Class.forName(properties.getProperty("datasource.driver"));
            String username = properties.getProperty("datasource.username");
            String password = properties.getProperty("datasource.password");
            String url = properties.getProperty("datasource.url");
            Connection con = DriverManager.getConnection(url, username, password);

            PreparedStatement statement = con.prepareStatement(sql);
            for (int i = 0; i < param.length; i++) {
                statement.setObject(i+1, param[i]);
            }
            ResultSet result = statement.executeQuery();

            setMap(Person.class);
            while (result.next()) {
                Person p;
                p = convert(result, Person.class);
                if (p != null)
                    return (T) p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Map<String, String> nickNameMap = new HashMap<String, String>();

    void setMap(Class clazz) {
        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(MyColumn.class)) {
                MyColumn column = field.getAnnotation(MyColumn.class);
                nickNameMap.put(column.name(), field.getName());
            }
        }

    }

    <T> T convert(ResultSet result, Class<T> tClass) {
        T t = null;
        try {
            t = tClass.newInstance();
            int paramSize = result.getMetaData().getColumnCount();
            Constructor<?>[] constructors = tClass.getDeclaredConstructors();
            //构造方法赋值
            for (Constructor c : constructors) {
                if (c.getParameterTypes().length > 0 && c.getParameterTypes().length == paramSize) {
                    Object[] params = new Object[c.getParameterTypes().length];
                    for (int i = 0; i < paramSize; i++) {
                        params[i] = result.getObject(i + 1);
                    }
                    try {
                        return (T) c.newInstance(params);
                    } catch (Exception e) {
                        break;
                    }
                }
            }

            for (int i = 0; i < paramSize; i++) {
                Object value = result.getObject(i + 1);
                String columnName = result.getMetaData().getColumnName(i + 1);
                String name = columnName;
                if (nickNameMap.containsKey(name))
                    name = nickNameMap.get(name);
                Field field = tClass.getDeclaredField(name);
                field.setAccessible(true);
                if (value != null && value.getClass() == field.getType())
                    field.set(t, value);
                else if (field.getType() == java.util.Date.class) {
                    field.set(t, str2Date(value.toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    Date str2Date(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
