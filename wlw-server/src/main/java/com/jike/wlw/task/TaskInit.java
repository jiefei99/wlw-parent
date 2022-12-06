package com.jike.wlw.task;

import com.jike.wlw.core.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TaskInit extends BaseService {

    @Autowired
    private Environment env;

//    @Scheduled(cron = "*/30 * * * * ?")
//    public void workInitTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            workInitTaskHandle.doTask(Constants.Redis.ROOT_PATH + WorkInitTaskHandle.class.getSimpleName());
//        }else{
//            workInitTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + WorkInitTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "*/5 * * * * ?")
//    public void WorkPlanTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            workPlanTaskHandle.doTask(Constants.Redis.ROOT_PATH + WorkPlanTaskHandle.class.getSimpleName());
//        }else{
//            workPlanTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + WorkPlanTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "*/3 * * * * ?")//每5秒执行 生成打卡记录
//    public void signCountInitTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            signCountInitTaskHandle.doTask(Constants.Redis.ROOT_PATH + SignCountInitTaskHandle.class.getSimpleName());
//        }else{
//            signCountInitTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + SignCountInitTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?") //每天0点执行 生成打卡记录计划
//    public void signCountInitMissionTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            initMissionTaskHandle.doTask(Constants.Redis.ROOT_PATH + SignCountInitMissionTaskHandle.class.getSimpleName());
//        }else{
//            initMissionTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + SignCountInitMissionTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "0 */10 * * * ?") //每天1点执行将任务发布
//    public void workPlanPublishTaskHandle() {
//
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            workPlanPublishTaskHandle.doTask(Constants.Redis.ROOT_PATH + WorkPlanPublishTaskHandle.class.getSimpleName());
//        }else{
//            workPlanPublishTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + WorkPlanPublishTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?") //每天0点把过期的任务置为过期
////    @Scheduled(cron = "*/10 * * * * ?") //每天0点把过期的任务置为过期
//    public void workInitMissionCompleteTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            workInitMissionCompleteTaskHandle.doTask(Constants.Redis.ROOT_PATH + WorkInitMissionCompleteTaskHandle.class.getSimpleName());
//        }else{
//            workInitMissionCompleteTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + WorkInitMissionCompleteTaskHandle.class.getSimpleName());
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 1 * ?") //每个月1号把上月的稽查员统计一下
//    public void InspectorCountRecordTaskHandle() {
//        if ("prd".equals(env.getProperty("spring.profiles.active"))) {
//            inspectorCountRecordTaskHandle.doTask(Constants.Redis.ROOT_PATH + InspectorCountRecordTaskHandle.class.getSimpleName());
//        }else{
//            inspectorCountRecordTaskHandle.doTask(Constants.Redis.ROOT_TEST_PATH + InspectorCountRecordTaskHandle.class.getSimpleName());
//        }
//    }
}
