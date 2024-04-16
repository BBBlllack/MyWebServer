package com.shj.domain.ent;

import java.time.LocalDateTime;

public class Order {
    private Integer oid;
    private LocalDateTime dateTime;
    private User user;
    // order 嵌套 user
    /**
     如果直接查询字段他会返回
     oid, datetime, uid, username, password
     他不会自动将uid username password 封装user中
     */
    /**
     order: oid datetime uid
     user uid username password
     */
}
