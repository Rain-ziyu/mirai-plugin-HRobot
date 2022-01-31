# 配置文件

## 模板

HRobot 支持配置文件，配置文件在 mcl/config/com.happysnaker.HRobot/config.json 中，您可以手动创建配置文件，不过建议您运行一次 mcl.cmd，这会自动生产配置文件 config.json，如下是配置文件的模板：

```json
{
	"menu":"主菜单",
	"exclude":["群号1", "群号2"],
	"include":[],
	"gtConfig":[
		{
			"groupId":"群号1",
			"gtCookie":"cookie1"
		},
        {
			"groupId":"群号2",
			"gtCookie":"cookie2"
		},
        {
			"groupId":"",
			"gtCookie":"cookie3"
		}
	]
}
```

## 介绍

- menu：这是机器人的主菜单，当 @机器人 并发送 help 或者 帮助 时，我们会发送此菜单。

- exclude： exclude 和 include 之间需选填一项而置另外一项为空，机器人不会处理 exclude 中的群，当  exclude 为空时，机器人会处理所有的群。

- include： include 和 exclude 之间需选填一项而置另外一项为空，当  include 为空时，机器人会处理所有的群，当  include 不为空时，只有被 include 标识的群才会被处理。

  > 当你同时配置 exclude 和 include 所产生的语义是不确定的，你必须只配置一项。当你两项都不配置时，机器人会处理所有的群。

- gtConfig：砍公相关配置

  - groupId：对应的群，当不配置 groupId 或 groupId 为空时，说明匹配所有的群。
  - gtCookie：对应 bigfun 账号的 Cookie，该 cookie 对应 groupId。

  > 例如在上述模板配置中，如果群 1 发送关键字“前线报道”，那么将会用 cookie1 进行查询，同理群 2 会使用 cookie2 进行查询，而群 3、群4、群N都会使用 cookie3 进行查询，这是因为 cookie3 对应的 groupId 为空，因此会匹配所有群。
  >
  > 如果 groupId 为空，请务必将其配置成最后一项(并且只有一个)，因为程序是顺序匹配的。

## 获取砍公 cookie

1. 用电脑点击[坎公百宝袋-bigfun社区](https://www.bigfun.cn/tools/gt/)并使用自己账号登录。
2. 点击 f12 打开游览器开发工具，找到网络工具。
3. 保持 f12 打开，同时点击前线报道，找到名称为 feweb?target=kan-gong-guild-boss-info%2Fa 的项。

​	![image-20220131193950450](https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220131193950450.png)

4. 再标头中找到请求标头，复制 cookie 即可。

![image-20220131194105416](https://happysnaker-1306579962.cos.ap-nanjing.myqcloud.com/img/typora/image-20220131194105416.png)