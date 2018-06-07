package com.test.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * Created by 吴晓冬 on 2018/6/5.
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{
    @Override
    protected Object determineCurrentLookupKey()
    {
        String dbType = DBContextHolder.getDbType();
        return dbType;
    }
}
