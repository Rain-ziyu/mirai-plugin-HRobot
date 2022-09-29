package com.happysnaker.cron;

import com.happysnaker.config.RobotConfig;
import com.happysnaker.factory.CustomThreadFactory;
import com.happysnaker.proxy.TaskProxy;
import com.happysnaker.utils.RobotUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 机器人后台线程，每 10 分钟执行一次，用户可向此类提交后台任务
 * <p><strong>机器人应该只有一个 {@link ScheduledExecutorService}，若想要调用定时任务，需调用此类全局的 service 进行服务</strong></p>
 *
 * @author Happysnaker
 * @description
 * @date 2022/7/2
 * @email happysnaker@foxmail.com
 */
public class RobotCronTask {
    public volatile static ScheduledExecutorService scheduledExecutorService;

    /**
     * 用于存放创建对应定时任务之后的Future对象，用于执行指定次数之后停止
     */
    public static ConcurrentHashMap<String, Future> futureMap = new ConcurrentHashMap<>();

    /**
     * 方法cronInit
     * 作用为：初始化定时任务线程池
     * 虽然在使用上保证了改scheduledExecutorService的单例，但是为了防止多次调用出现问题，还是使用单例模式封装
     *
     * @param
     * @return void
     * @throws
     * @author User
     */
    public static void cronInit() throws Exception {
//        双端检锁+volatile
        if (scheduledExecutorService == null) {
            synchronized (RobotCronTask.class) {
                if (scheduledExecutorService == null) {
                    // 运行后台线程池，执行定时任务
                    int corePoolSize = Runtime.getRuntime().availableProcessors();
                    CustomThreadFactory customThreadFactory = new CustomThreadFactory();
                    scheduledExecutorService = new ScheduledThreadPoolExecutor(corePoolSize, customThreadFactory);
                }
            }
        }
    }


    /**
     * 执行定期任务，此方法必须得等到机器人初始化完成后调用
     */
    public static void cronPeriodTask() throws Exception {
        // 执行定时任务
        Date now = new Date(System.currentTimeMillis());
        int year = now.getYear(), month = now.getMonth();
        for (Map<String, Object> map : RobotConfig.periodicTask) {
            int hour = (int) map.get("hour"), minute = (int) map.get("minute");
            long gid = Long.parseLong((String) map.get("groupId"));
            String content = (String) map.get("content");
            boolean image = (boolean) map.getOrDefault("image", false);
            int count = (int) map.getOrDefault("count", Integer.MAX_VALUE);
            if (count <= 0) {
                count = Integer.MAX_VALUE;
            }
            MessageChain message = RobotUtil.parseMiraiCode(content);
            List<Bot> instances = Bot.getInstances();
            for (Bot instance : instances) {
                if (instance.getGroups().contains(gid)) {
                    Contact contact = instance.getGroups().getOrFail(gid);
//                  使用工具类注册定时发送消息任务
                    RobotUtil.submitSendMsgTask(hour, minute, count, image, message, contact);
                }
            }
        }
    }

    //    增加系统定时清理任务，并注册执行
    public static void addCronTaskAndRegister(TaskProxy task,
                                              long initialDelay,
                                              long period,
                                              TimeUnit unit) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
        futureMap.put(task.getTaskId(), scheduledFuture);
    }
}
