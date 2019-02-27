package com.example.jpa.util;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * jpa生成数据库表字符集设置
 */
public class MySQL5DialectUTF8 extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
