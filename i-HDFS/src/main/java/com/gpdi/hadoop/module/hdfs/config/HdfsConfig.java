package com.gpdi.hadoop.module.hdfs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 类或方法的功能描述 :TODO
 *
 * @author: logan.zou
 * @date: 2018-11-28 13:50
 */
@Configuration
public class HdfsConfig {
    //@Value("${hdfs.path}")
    private String path="hdfs://172.16.104.203:9000";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

