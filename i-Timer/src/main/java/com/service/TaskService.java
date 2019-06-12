package com.service;

import com.task.DynamicScheduleTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Mapper
    public interface CronMapper {
        @Select("select * from cron")
        List<Map<String,String>> getCron();
    }

    @Autowired      //注入mapper
    @SuppressWarnings("all")
    CronMapper cronMapper;

    public void startTask(){
        DynamicScheduleTask ds = new DynamicScheduleTask("任务一","0/6 * * * * ?");
    }
}
