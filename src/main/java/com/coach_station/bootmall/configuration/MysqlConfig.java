package com.coach_station.bootmall.configuration;

/**
 * @Auther: yjw
 * @Date: 2020/03/15/12:01
 * @Description:
 */
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public class MysqlConfig extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}

