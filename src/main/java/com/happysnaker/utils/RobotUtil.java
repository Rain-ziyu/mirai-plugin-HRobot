package com.happysnaker.utils;

import com.happysnaker.api.PixivApi;
import com.happysnaker.config.RobotConfig;
import com.happysnaker.cron.RobotCronTask;
import com.happysnaker.exception.CanNotSendMessageException;
import com.happysnaker.exception.FileUploadException;
import com.happysnaker.proxy.TaskProxy;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2022/6/30
 * @email happysnaker@foxmail.com
 */
public class RobotUtil {
    public final String at = "[mirai:at:qq]";


    /**
     * 用于消息处理失败时记录日志
     * 修改使用scheduledExecutorService多线程来进行定时任务的执行
     *
     * @param event
     * @param errorMsg
     */
    public static void recordFailLog(MessageEvent event, String errorMsg) {
        TaskProxy taskProxy = new TaskProxy() {
            @Override
            public void run() {
                String filePath = ConfigUtil.getDataFilePath("error.log");
                try {
                    IOUtil.writeToFile(new File(filePath), getLog(event) + "\n错误日志：：" + errorMsg + "\n");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
//        注册并返回执行器
        ScheduledFuture<?> schedule = RobotCronTask.scheduledExecutorService.schedule(taskProxy, 0, TimeUnit.SECONDS);
        RobotCronTask.futureMap.put(taskProxy.getTaskId(), schedule);
    }

    public static String getLog(MessageEvent event) {
        if (event == null) return "";
        String content = getContent(event);
        String sender = getSenderId(event);
        if (!(event instanceof GroupMessageEvent)) {
            return "[sender:" + sender + "-" + formatTime() + "] -> " + content;
        }
        long groupId = ((GroupMessageEvent) event).getGroup().getId();
        return "[sender:" + sender + " - group:" + groupId + " - " + formatTime() + "] -> " + content;
    }


    /**
     * 读取机器人所有的群
     *
     * @return
     */
    public static Set<String> getBotsAllGroupId() {
        List<Bot> bots = Bot.getInstances();
        Set<String> ans = new HashSet<>();
        for (Bot bot : bots) {
            for (Group group : bot.getGroups()) {
                ans.add(String.valueOf(group.getId()));
            }
        }
        return ans;
    }


    /**
     * 对消息中的 {face:num} 表情进行解析，并将表情用实际的 Face 类替代，封装进 MessageChain 中，MessageChain 中仍然保持原消息中表情和其他消息的相对位置
     *
     * @param text 要解析的文本
     * @return MessageChain
     */
    public static MessageChain replaceFaceFromContent(String text) {
        String regex = "\\{face:\\d+\\}";
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        boolean findFace = false;
        int start = 0, end = 0;
        while (matcher.find()) {
            findFace = true;
            end = matcher.start();
            if (start != end) {
                messageChainBuilder.append(new PlainText(text.substring(start, end)));
            }
            String[] split = matcher.group().split("\\D+");
            for (String s1 : split) {
                if (!s1.isEmpty()) {
                    try {
                        int face = Integer.parseInt(s1);
                        messageChainBuilder.append(new Face(face));
                    } catch (Exception e) {
                        // continue
                    }
                }
            }
            start = matcher.end();
        }
        return findFace ? messageChainBuilder.build() : messageChainBuilder.append(text).build();
    }


    /**
     * 从事件中提取消息，并将该消息转换为 mirai 码
     *
     * @param event
     * @return mirai 编码消息
     */
    public static String getContent(MessageEvent event) {
        if (event == null) {
            return null;
        }
        return getContent(event.getMessage());
    }


    /**
     * 该消息转换为 mirai 码
     *
     * @param chain
     * @return mirai 编码消息
     */
    public static String getContent(MessageChain chain) {
        if (chain == null) {
            return null;
        }
        return chain.serializeToMiraiCode();
    }

    /**
     * 从 mirai 编码转换为 MessageChain
     *
     * @param content
     * @return
     */
    public static MessageChain parseMiraiCode(String content) {
        return MiraiCode.deserializeMiraiCode(content);
    }


    /**
     * 从事件中提取消息，该消息仅包含纯文本内容
     *
     * @param event
     * @return 纯文本
     * @see #getContent
     */
    public static String getOnlyPlainContent(MessageEvent event) {
        if (event == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (SingleMessage singleMessage : event.getMessage()) {
            if (singleMessage instanceof PlainText) {
                sb.append(singleMessage);
            }
        }
        return sb.toString().trim();
    }


    /**
     * 读取文件（图片）并上传至腾讯服务器
     *
     * @param event
     * @param filename 图片文件路径名
     * @return net.mamoe.mirai.message.data.Image
     */
    public static net.mamoe.mirai.message.data.Image uploadImage(MessageEvent event, String filename) throws FileUploadException {
        try {
            return ExternalResource.uploadAsImage(new File(filename), event.getSubject());
        } catch (Exception e) {
            throw new FileUploadException("Can not upload the image from the file: " + filename + "\nCause by " + e.getCause().toString());
        }
    }


    /**
     * 如果匹配则删除，注意该函数会返回 null
     *
     * @param content 源消息(MIRAI编码)
     * @param regex   at 匹配的消息
     * @return 如果被 at 将返回去除 atMessage(所有的) 信息后的消息，否则返回 null
     */
    public static String handlerContentIfMatches(String content, String regex) {
        // 如果 split = 1，说明没有分割，即不包含该 regex
        if (content != null && content.split(regex).length != 1) {
            return content.replaceAll(regex, "").trim();
        }
        return null;
    }


    /**
     * 建造 MessageChain
     *
     * @param m 多个文本消息
     * @return 将多个文本消息结合成 MessageChain
     */
    public static MessageChain buildMessageChain(String... m) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (String s : m) {
            builder.append(s);
        }
        return builder.build();
    }

    /**
     * 建造 MessageChain，参数是多个 SingleMessage
     *
     * @param m 多个 SingleMessage
     * @return 将多个 SingleMessage 组合成 MessageChain
     */
    public static MessageChain buildMessageChain(Object... m) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Object s : m) {
            if (s instanceof String) {
                s = new PlainText((CharSequence) s);
            }
            builder.append((SingleMessage) s);
        }
        return builder.build();
    }


    /**
     * 建造 MessageChain，参数是多个 SingleMessage
     *
     * @param m 多个 SingleMessage
     * @return 将多个 SingleMessage 组合成 MessageChain List，List 的大小只为 1
     */
    public static List<MessageChain> buildMessageChainAsList(Object... m) {
        return OfUtil.ofList(buildMessageChain(m));
    }


    /**
     * 建造 MessageChainList，参数是多个 MessageChain
     *
     * @param m 多个 MessageChain
     * @return 将多个 MessageChain 组合成多条消息 MessageChain
     */
    public static List<MessageChain> buildMessageChainAsList(MessageChain... m) {
        return OfUtil.ofList(m);
    }

    /**
     * 获取发送者的 QQ
     *
     * @param event
     * @return
     */
    public static String getSenderId(MessageEvent event) {
        return String.valueOf(event.getSender().getId());
    }

    public static long getSenderId2(MessageEvent event) {
        return event.getSender().getId();
    }

    /**
     * 网络图片并上传至腾讯服务器
     *
     * @param event
     * @param url   网络图片 URL
     * @return net.mamoe.mirai.message.data.Image
     */
    public static net.mamoe.mirai.message.data.Image uploadImage(MessageEvent event, URL url) throws FileUploadException {
        return uploadImage(event.getSubject(), url);
    }

    /**
     * 设置引用回复，如果失败，则返回 null<br/>
     * 如果想回复某消息，你可以这样做：chainBuilder.append(getQuoteReply(e))<br/>或者调用父类方法：buildMessageChain(getQuoteReply(e), msg) 以构造一条消息链<br/>或者使用 getQuoteReply 方法回复一条简单文本信息
     *
     * @param event
     * @return MessageSource
     * @see #buildMessageChain(Object...)
     * @see #quoteReply(MessageEvent, String)
     */
    public static QuoteReply getQuoteReply(MessageEvent event) {
        return new QuoteReply(event.getMessage());
    }

    /**
     * 获取引用消息事件的源，如果不存在，则返回 null
     *
     * @param event
     * @return MessageSource
     */
    public static MessageSource getQuoteSource(MessageEvent event) {
        return event.getMessage().get(QuoteReply.Key).getSource();
    }

    /**
     * 提交一条每天定时发送的消息
     *
     * @param hour      0-23
     * @param minute    0-60
     * @param count     需要执行几次，大于等于 1
     * @param plusImage
     * @param message   消息
     * @throws CanNotSendMessageException
     */
    public static void submitSendMsgTask(int hour, int minute, int count, boolean plusImage, MessageChain message, Contact contact) throws CanNotSendMessageException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        if (time.before(new Date(System.currentTimeMillis()))) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            time = calendar.getTime();
        }
        long nextTimeInMillis = calendar.getTimeInMillis();
        //        定时任务初始化时间
        long initDelay = nextTimeInMillis - System.currentTimeMillis();
        // 保存 count 的容器
        final PairUtil<Integer, Object> pair = PairUtil.of(count, null);
        RobotConfig.logger.info("下一次任务执行时间 = " + time);
        // 为之注册定时任务
        TaskProxy taskProxy = new TaskProxy() {
            @Override
            public void run() {
                MessageChain msg = message;
                if (plusImage) {
                    try {
                        msg = message.plus(RobotUtil.uploadImage(contact, new URL(PixivApi.beautifulImageUrl)));
                    } catch (FileUploadException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                contact.sendMessage(msg);
                pair.setKey(pair.getKey() - 1);
                if (pair.getKey() <= 0) {
//                    获取并移除
                    Future future = RobotCronTask.futureMap.remove(getTaskId());
                    future.cancel(true);
                    RobotConfig.logger.info("定时任务执行次数达到阈值，已取消该任务");
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = RobotCronTask.scheduledExecutorService.scheduleAtFixedRate(taskProxy, initDelay, 1000 * 60 * 60 * 24, TimeUnit.MILLISECONDS);
        RobotCronTask.futureMap.put(taskProxy.getTaskId(), scheduledFuture);
    }

    public static MessageChain quoteReply(MessageEvent event, String msg) {
        return buildMessageChain(getQuoteReply(event), msg);
    }


    /**
     * 获取一个消息链中的 Images
     *
     * @param chain
     * @return List&lt;SingleMessage&gt;，可以将 SingleMessage 强转为 Image 类
     */
    public static List<SingleMessage> getImagesFromMessage(MessageChain chain) {
        return chain.stream().filter(Image.class::isInstance).collect(Collectors.toList());
    }


    /**
     * 发送消息，子类可以提前发送消息，而不必等到由 getReplyMessage 方法被调用，请注意，即使子类提前发送消息，getReplyMessage 仍然会被调用，不过子类可以在 getReplyMessage 方法内返回 null 值以表示不发送消息
     *
     * @param msg   消息链
     * @param event 消息事件
     * @return
     */
    public static void sendMsg(List<MessageChain> msg, MessageEvent event) throws CanNotSendMessageException {
        sendMsg(msg, event.getSubject());
    }


    /**
     * 发送消息，子类可以提前发送消息，而不必等到由 getReplyMessage 方法被调用，请注意，即使子类提前发送消息，getReplyMessage 仍然会被调用，不过子类可以在 getReplyMessage 方法内返回 null 值以表示不发送消息
     *
     * @param msg   消息链
     * @param event 消息事件
     * @return
     */
    public static void sendMsg(MessageChain msg, MessageEvent event) throws CanNotSendMessageException {
        sendMsg(OfUtil.ofList(msg), event.getSubject());
    }


    /**
     * 发送多条消息，<strong>注意此方法并不能保证发送消息的顺序<strong/>
     *
     * @param msg     消息链
     * @param contact 发送对象
     * @return
     */
    public static void sendMsg(List<MessageChain> msg, Contact contact) throws CanNotSendMessageException {
        try {
            for (MessageChain chain : msg) {
                contact.sendMessage(chain);
            }
        } catch (Exception e) {
            throw new CanNotSendMessageException(e.getMessage());
        }
    }


    /**
     * 发送一条将自动撤回的消息，子类可以提前发送消息，而不必等到由 getReplyMessage 方法被调用，请注意，即使子类提前发送消息，getReplyMessage 仍然会被调用，不过子类可以在 getReplyMessage 方法内返回 null 值以表示不发送消息
     *
     * @param msg        消息
     * @param contact    发送对象
     * @param autoRecall 自动撤回等待时间(毫秒)
     * @return
     */
    public static void sendMsg(MessageChain msg, Contact contact, long autoRecall) throws CanNotSendMessageException {
        try {
            contact.sendMessage(msg).recallIn(autoRecall);
        } catch (Exception e) {
            throw new CanNotSendMessageException(e.getMessage());
        }
    }

    /**
     * 网络图片并上传至腾讯服务器
     *
     * @param contact 要发送的对象
     * @param url     网络图片 URL
     * @return net.mamoe.mirai.message.data.Image
     */
    public static net.mamoe.mirai.message.data.Image uploadImage(Contact contact, URL url) throws FileUploadException {
        try (InputStream stream = IOUtil.sendAndGetResponseStream(url, "GET", null, null)) {
            return Contact.uploadImage(contact, stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException("can not upload the image from the url: " + url + ", cause by " + e.getCause().toString());
        }
    }

    /**
     * 提交一条将要发送的消息，此消息将在 waitTime 毫秒后自动发送
     * @param msg      消息
     * @param contact  发送对象
     * @param waitTime 将要等待的事件
     * @return 返回 future，可以调用 future.cancel 以取消事件
     */
    public static void submitSendMsgTask(MessageChain msg, Contact contact, long waitTime) throws CanNotSendMessageException {
        TaskProxy taskProxy = new TaskProxy() {
            @Override
            public void run() {
                try {
                    sendMsg(Collections.singletonList(msg), contact);
                } catch (CanNotSendMessageException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledFuture<?> schedule = RobotCronTask.scheduledExecutorService.schedule(taskProxy, waitTime, TimeUnit.MILLISECONDS);
        RobotCronTask.futureMap.put(taskProxy.getTaskId(), schedule);
    }


    /**
     * 提交一条周期性发送的消息，初始等待时间为 initTime 毫秒，周期为 waitTime 毫秒
     *
     * @param msg       消息
     * @param contact   发送对象
     * @param initTTime 初始等待时间
     * @param waitTime  周期时间
     * @return 返回 future，可以调用 future.cancel 以取消事件
     */
    public static void submitSendMsgTaskAtFixRate(MessageChain msg, Contact contact, long initTTime, long waitTime) throws CanNotSendMessageException {
        TaskProxy taskProxy = new TaskProxy() {
            @Override
            public void run() {
                try {
                    sendMsg(Collections.singletonList(msg), contact);
                } catch (CanNotSendMessageException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = RobotCronTask.scheduledExecutorService.scheduleAtFixedRate(taskProxy, initTTime, waitTime, TimeUnit.MILLISECONDS);
        RobotCronTask.futureMap.put(taskProxy.getTaskId(), scheduledFuture);
    }


    public static String formatTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }


    /**
     * 判断给定消息事件是否引用了一条消息
     *
     * @param event
     * @return
     */
    public static boolean hasQuote(MessageEvent event) {
        return event.getMessage().get(QuoteReply.Key) != null;
    }

    /**
     * 从消息事件中取出事件所引用的消息
     *
     * @param event
     * @return
     * @throws NullPointerException 如果消息事件不包含引用
     * @see #hasQuote(MessageEvent)
     */
    public static MessageChain getQuoteMessageChain(MessageEvent event) {
        return getQuoteSource(event).getOriginalMessage();
    }


    public static boolean equals(MessageSource source1, MessageSource source2) {
        if (source1 == null && source2 == null) return true;
        if (source1 == null || source2 == null) return false;
        return source1.getFromId() == source2.getFromId() && source1.getTargetId() == source2.getTargetId() && source1.getTime() == source2.getTime();
    }

    public static List<MessageChain> doHelp(MessageEvent event) throws MalformedURLException, FileUploadException {
        event.getSubject().sendMessage("正在上传帮助图片，此操作可能较慢，请稍等。如此操作出错，请前往 https://github.com/happysnaker/mirai-plugin-HRobot 查阅相关信息");
        // 版本变更
        Image image0 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228125440601.png"));

        List<MessageChain> ans = new ArrayList<>();
        ans.add(buildMessageChain("—— HRobot v2.0-beta ——\n", image0));

        String desc1 = "以下为表格关键字及示例，部分关键字需要 @ 机器人才有效。\n" + "主页地址：https://github.com/happysnaker/mirai-plugin-HRobot\n";
        Image image1 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228130359523.png"));
        Image image2 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228131341431.png"));
        Image image3 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228131533107.png"));
        ans.add(buildMessageChain(desc1, image1, image2, image3));

        String desc2 = "以下为表格命令及示例，发送对应命令到群内即可识别，命令必须要以特殊前缀 # 开头。\n";
        Image image4 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228131050819.png"));
        Image image5 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228131128660.png"));
        Image image6 = uploadImage(event, new URL("https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220228131229491.png"));
        ans.add(buildMessageChain(desc2, image4, image5, image6));

        String desc3 = "如果相关 BUG 或有更好的创意，请于 https://github.com/happysnaker/mirai-plugin-HRobot/issues 提出相关 ISSUE\n";
        ans.add(buildMessageChain((Object) desc3));
        return ans;
    }
}
