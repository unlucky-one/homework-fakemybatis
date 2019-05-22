package com.raiden.homework.framework;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2019/5/12
 */
public class MySqlSession {
    private MyConfiguration configuration;
    private MyExcutor excutor;

    public MySqlSession(MyConfiguration configuration, MyExcutor excutor) {
        this.configuration = configuration;
        this.excutor = excutor;
    }


    public <T> T selectOne(String statementId, Object parameter) {
        String sql = configuration.sqlMappings.getString(statementId);
        return excutor.query(sql, new Object[]{parameter});
    }

    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }
}
