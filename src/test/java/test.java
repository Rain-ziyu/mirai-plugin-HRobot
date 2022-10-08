import com.alibaba.fastjson.JSON;
import com.happysnaker.api.JobTaskTriggerApi;
import com.happysnaker.command.impl.ScheduledTaskCommandMessageEventHandler;
import com.happysnaker.entity.ContentMessage;
import com.happysnaker.entity.JobTriggerResultDTO;
import com.happysnaker.utils.IOUtil;
import net.mamoe.mirai.message.data.MessageChain;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {
        IOUtil.sendAndGetResponseMap(new URL("http://api.lolicon.app/setu/v2?r18=1"), "GET", null, null).get("data");
    }

    @Test
    public void testGetToken() throws IOException {
//        String remoteToken = JobTaskTriggerApi.getRemoteToken();
        ContentMessage contentMessage = new ContentMessage("1874300301", "CRON 0 0 2 * * ? *|MSG 你好|SENDTO 1874300301");

        JobTriggerResultDTO jobTriggerResultDTO = JobTaskTriggerApi.sendScheduledTasks(contentMessage);
        System.out.println(JSON.toJSONString(jobTriggerResultDTO) + "哈哈哈你好调度");
    }

    @Test
    public void testGetList() throws IOException {
//        String remoteToken = JobTaskTriggerApi.getRemoteToken();
        ScheduledTaskCommandMessageEventHandler scheduledTaskCommandMessageEventHandler = new ScheduledTaskCommandMessageEventHandler();
        List<MessageChain> messageChains = scheduledTaskCommandMessageEventHandler.taskList();
    }
}
