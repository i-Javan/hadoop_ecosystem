package com.task;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;


@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DynamicScheduleTask implements SchedulingConfigurer {


    /**
     * 时间
     */
    @Getter
    @Setter
    private String cron = "0/10 * * * * ?";

    /**
     * 任务名称
     */
    @Getter
    @Setter
    private String name;


    public DynamicScheduleTask() {
    }

    public DynamicScheduleTask(String name, String cron) {
        this.cron = cron;
        this.name = name;
    }



    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> System.out.println("执行动态定时任务: " + LocalDateTime.now().toLocalTime()),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    System.err.println("任务名称："+name+"，任务时间："+cron);
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }
    /**
     * 业务触发器
     * @return
     */
    private Trigger getTrigger() {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                System.err.println("触发器："+cron);
                // 触发器
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }

}
