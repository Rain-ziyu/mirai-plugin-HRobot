[toc]



# 历史版本关键字及命令

新版本的关键字及命令都是向后兼容的，

## v2.0

### 关键字

| 特殊关键字   | 示例                   | 功能                                                         |
| ------------ | :--------------------- | ------------------------------------------------------------ |
| 帮助         | @机器人 帮助           | 显示主菜单                                                   |
| help         | @机器人 help           | 显示主菜单                                                   |
| 天气         | @机器人 天气深圳       | 查看深圳的天气                                               |
| 翻译         | @机器人 翻译i love you | 中英翻译                                                     |
| 笑话         | @机器人 笑话           | 笑话                                                         |
| 歌词         | @机器人 歌词后来       | 查看歌词                                                     |
| 成语         | @机器人 成语暗度陈仓   | 查看成语的释义                                               |
| 前线报道     | 前线报道               | 查看工会战前线Boss信息                                       |
| 会战报表     | 会战报表               | 查看当日出刀情况                                             |
| 会战统计     | 会战统计               | 统计本次会战所有参与玩家数据，包括对任一 Boss 的数据统计     |
| 会战统计     | 会战统计好开心         | 统计玩家名为“好开心”的会战数据，此功能会包含出刀数和输出饼状图 |
| 查刀         | 查刀 成员1 成员2       | 查询 成员1、成员2 的出刀情况，机器人会发送柱状图以表示出刀情况(我正在考虑换成折线图或其他图标，如有更好想法，可以提出你的 issue) |
| 查刀折线图   | 用法同上               | “查刀” 使用柱状图生成，适合多成员比对；“查刀折线图” 使用折线图，适合查看单成员数据，因为多成员可能会出现线“重合问题” |
| 谁未出刀     | 谁未出刀               | 查看今日谁没出刀，可以通过配置或命令设置公会成员，若不设置，则默认群内所有成员都是公会成员，这可能造成错误@，此功能必须要求群成员昵称完全等于游戏名 |
| 谁没出刀     | 谁没出刀               | 同谁未出刀                                                   |
| 催刀         | 催刀                   | 机器人 @ 所有未出刀的群成员并催刀，可以通过配置或命令设置公会成员，若不设置，则默认群内所有成员都是公会成员，这可能造成错误@，此功能必须要求群成员昵称完全等于游戏名 |
| 一键催刀     | 一键催刀               | 机器人 @ 所有未出刀的群成员并催刀，相较于 催刀 而言，此功能还会 @ 出了刀但未出满的成员，可以通过配置或命令设置公会成员，若不设置，则默认群内所有成员都是公会成员，这可能造成错误@，此功能必须要求群成员昵称完全等于游戏名 |
| 鸡汤         | 鸡汤                   | 发送随机鸡汤                                                 |
| 美图         | 美图                   | 发送随机二刺螈美图                                           |
| 风景图       | 风景图                 | 发送随机风景图                                               |
| 神秘代码     | 神秘代码萝莉 白丝      | 发送涩图，可指定 tag，tag 之间存在空格                       |
| 涩图         | 涩图萝莉 白丝          | 发送涩图，可指定 tag，tag 之间存在空格，相较于神秘代码而言，此涩图放开 R18 限制，更加涩涩，默认情况下发送图片后 30s 后自动撤回，可通过配置修改撤回时间 |
| 音乐         | 音乐克罗地亚狂想曲     | 从酷我、QQ、网易云聚合搜索音乐，并发送卡片                   |
| 咪咕         | 咪咕七里香             | 从咪咕音乐搜索，咪咕音乐可以免费听周杰伦的所有歌曲           |
| 百度百科     | 百度百科 基金          | 查询 基金 的百度百科释义                                     |
| 自定义关键字 | ——                     | 可以通过配置或者命令设置自定义关键字及回复，关键字和回复可以是纯文本，也可以是表情、图片等，详情请前往配置手册或命令手册查看 |



### 命令

| 命令                | 示例                                      | 功能                                                         | 权限要求                                             |
| ------------------- | ----------------------------------------- | ------------------------------------------------------------ | ---------------------------------------------------- |
| #保存配置           | #保存配置                                 | 将由命令造成的配置更改写入配置文件以持久化                   | 至少是管理员权限                                     |
| #重载配置           | #重载配置                                 | 重新载入配置文件                                             | 至少是管理员权限                                     |
| #查看配置状态       | #查看配置状态                             | 查看是否有命令执行成功                                       | 至少是管理员权限                                     |
| #查看配置日志       | #查看配置日志                             | 若有命令执行成功，查看命令执行日志                           | 至少是管理员权限                                     |
| #授予管理员权限     | #授予管理员权限 123456                    | 权限命令，授予 qq 为 123456 用户管理员权限                   | 必须要求自身权限大于被操作对象的权限以及要授予的权限 |
| #授予坎公管理员权限 | #授予坎公管理员权限 123456                | 权限命令，授予 qq 为 123456 用户授予坎公管理员权限           | 必须要求自身权限大于被操作对象的权限以及要授予的权限 |
| #授予群管理员权限   | #授予群管理员权限 123456                  | 权限命令，授予 qq 为 123456 用户群管理员权限                 | 必须要求自身权限大于被操作对象的权限以及要授予的权限 |
| #移除管理员权限     | #移除管理员权限 123456                    | 权限命令，移除 qq 为 123456 用户管理员权限                   | 必须要求自身权限大于被操作对象的权限                 |
| #授予坎公管理员权限 | #授予坎公管理员权限 123456                | 权限命令，授予 qq 为 123456 用户授予坎公管理员权限           | 必须要求自身权限大于被操作对象的权限                 |
| #授予群管理员权限   | #授予群管理员权限 123456                  | 权限命令，授予 qq 为 123456 用户群管理员权限                 | 必须要求自身权限大于被操作对象的权限                 |
| #查看权限           | #查看权限                                 | 权限命令，查看自身的权限，也可以通过“#查看权限 qq” 来查看他人的权限 | 无要求                                               |
| #设置关键字回复     | #设置关键字回复 {自定义关键字} 自定义回复 | 设定**全局生效**的自定义关键字及回复，关键字及回复都可以是图片或表情，你必须使用 {} 将关键字包裹起来，除关键字和命令之外的其他任何消息都将会被视为回复 | 至少是管理员权限                                     |
| #设置关键字回复     | #设置关键字回复 {#regex#呜*} 你呜尼玛呢   | 默认情况下，机器人通过“编辑距离”算法检测关键词相似度，但当关键字以特殊前缀 “#regex#” 打头时，表示关键字是一个正则表达式，例如 “呜*” 将匹配任意个 “呜“ | 至少是管理员权限                                     |
| #设置关键字回复     | (引用消息)#设置关键字回复  关键字         | 你还可以引用一条消息来代表回复，通过此方式，关键字无须用 {} 包裹，请注意删除 @ 信息，否则关键字必须也要 @ 触发 | 至少是管理员权限                                     |
| #设置群内关键字回复 | 用法同上                                  | 群内关键字回复只针对发送者的群生效                           | 至少是群管理员权限                                   |
| #移除关键字         | #移除关键字  关键字                       | 移除一条全局的关键字                                         | 至少是管理员权限                                     |
| #移除群内关键字     | #移除群内关键字  关键字                   | 移除一条发送者群内的关键字                                   | 至少是群管理员权限                                   |
| #清空关键字         | #清空关键字                               | 清空全局的关键字                                             | 至少是管理员权限                                     |
| #清空群内关键字     | #清空群内关键字                           | 清空发送者群内的关键字                                       | 至少是群管理员权限                                   |
| #清空所有关键字     | #清空所有关键字                           | #清空所有（包括群内）的关键字                                | 至少是管理员权限                                     |
| #设置坎公公会成员   | #设置坎公公会成员 成员1 成员2 ......      | 将成员1、成员2、......、成员 N 设置为发送者群对应的坎公公会成员(**会覆盖原有的配置**)，**执行添加或移除成员前，必须先执行此命令或通过配置文件配置** | 至少是坎公管理员权限                                 |
| #添加坎公公会成员   | #添加坎公公会成员 成员1 成员2 ......      | 在原有配置的基础上，动态的增添成员                           | 至少是坎公管理员权限                                 |
| #移除坎公公会成员   | #移除坎公公会成员 成员1 成员2 ......      | 在原有配置的基础上，动态的移除成员                           | 至少是坎公管理员权限                                 |
| #清空坎公公会成员   | #清空坎公公会成员                         | 清空发送者群对应的坎公公会成员                               | 至少是坎公管理员权限                                 |
| #查看坎公公会成员   | #查看坎公公会成员                         | **查看坎公公会成员，在配置后可调用此命令查看配置结果**       | 至少是坎公管理员权限                                 |
| #设置坎公cookie     | #设置坎公cookie 123456 ikhqwe             | 将群 123456 的 cookie 设置为 ikhqwe                          | 至少是管理员权限                                     |

## v1.2

### 关键字

| 特殊关键字 | 示例                   | 功能                                                         |
| ---------- | :--------------------- | ------------------------------------------------------------ |
| 帮助       | @机器人 帮助           | 显示主菜单                                                   |
| help       | @机器人 help           | 显示主菜单                                                   |
| 天气       | @机器人 天气深圳       | 查看深圳的天气                                               |
| 翻译       | @机器人 翻译i love you | 中英翻译                                                     |
| 笑话       | @机器人 笑话           | 笑话                                                         |
| 歌词       | @机器人 歌词后来       | 查看歌词                                                     |
| 成语       | @机器人 成语暗度陈仓   | 查看成语的释义                                               |
| 前线报道   | 前线报道               | 查看工会战前线Boss信息                                       |
| 会战报表   | 会战报表               | 查看当日出刀情况                                             |
| 会战统计   | 会战统计               | 统计本次会战所有参与玩家数据，包括对任一 Boss 的数据统计     |
| 鸡汤       | 鸡汤                   | 发送随机鸡汤                                                 |
| 美图       | 美图                   | 发送随机二刺螈美图                                           |
| 风景图     | 风景图                 | 发送随机风景图                                               |
| 神秘代码   | 神秘代码萝莉 白丝      | 发送涩图，可指定 tag，tag 之间存在空格                       |
| 涩图       | 涩图萝莉 白丝          | 发送涩图，可指定 tag，tag 之间存在空格，相较于神秘代码而言，此涩图放开 R18 限制，更加涩涩，默认情况下发送图片后 30s 后自动撤回，可通过配置修改撤回时间 |
| 音乐       | 音乐克罗地亚狂想曲     | 从酷我、QQ、网易云聚合搜索音乐，并发送卡片                   |

### 命令

无此功能



## v1.1

### 关键字

| 特殊关键字 | 示例                   | 功能                                                     |
| ---------- | :--------------------- | -------------------------------------------------------- |
| 帮助       | @机器人 帮助           | 显示主菜单                                               |
| help       | @机器人 help           | 显示主菜单                                               |
| 天气       | @机器人 天气深圳       | 查看深圳的天气                                           |
| 翻译       | @机器人 翻译i love you | 中英翻译                                                 |
| 笑话       | @机器人 笑话           | 笑话                                                     |
| 歌词       | @机器人 歌词后来       | 查看歌词                                                 |
| 成语       | @机器人 成语暗度陈仓   | 查看成语的释义                                           |
| 前线报道   | 前线报道               | 查看工会战前线Boss信息                                   |
| 会战报表   | 会战报表               | 查看当日出刀情况                                         |
| 会战统计   | 会战统计               | 统计本次会战所有参与玩家数据，包括对任一 Boss 的数据统计 |
| 鸡汤       | 鸡汤                   | 发送随机鸡汤                                             |
| 美图       | 美图                   | 发送随机二刺螈美图                                       |
| 风景图     | 风景图                 | 发送随机风景图                                           |
| 神秘代码   | 神秘代码萝莉 白丝      | 发送涩图，可指定 tag，tag 之间存在空格                   |
| 音乐       | 音乐克罗地亚狂想曲     | 从酷我、QQ、网易云聚合搜索音乐，并发送卡片               |

### 命令

无此功能



## v1.0

### 关键字

| 特殊关键字 | 示例                   | 功能                                                     |
| ---------- | :--------------------- | -------------------------------------------------------- |
| 帮助       | @机器人 帮助           | 显示主菜单                                               |
| help       | @机器人 help           | 显示主菜单                                               |
| 天气       | @机器人 天气深圳       | 查看深圳的天气                                           |
| 翻译       | @机器人 翻译i love you | 中英翻译                                                 |
| 笑话       | @机器人 笑话           | 笑话                                                     |
| 歌词       | @机器人 歌词后来       | 查看歌词                                                 |
| 成语       | @机器人 成语暗度陈仓   | 查看成语的释义                                           |
| 前线报道   | 前线报道               | 查看工会战前线Boss信息                                   |
| 会战报表   | 会战报表               | 查看当日出刀情况                                         |
| 会战统计   | 会战统计               | 统计本次会战所有参与玩家数据，包括对任一 Boss 的数据统计 |
| 鸡汤       | 鸡汤                   | 发送随机鸡汤                                             |
| 美图       | 美图                   | 发送随机二刺螈美图                                       |
| 风景图     | 风景图                 | 发送随机风景图                                           |
| 神秘代码   | 神秘代码萝莉 白丝      | 发送涩图，可指定 tag，tag 之间存在空格                   |
| 音乐       | 音乐克罗地亚狂想曲     | 从酷我、QQ、网易云聚合搜索音乐，并发送卡片|
### 命令

无此功能