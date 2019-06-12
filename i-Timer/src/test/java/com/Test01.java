package com;

import com.task.DynamicScheduleTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test01 {

    @Autowired
    DynamicScheduleTask ds;

    @Test
    public void test01(){
        DynamicScheduleTask ds = new DynamicScheduleTask("任务一","0/6 * * * * ?");
    }
}
