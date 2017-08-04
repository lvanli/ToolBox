# ToolBox  
## launcher  
使用 ViewPager加自定义指示器  
使用 XML 存储相关数据
自定义图标可显示设置的图标或者名称的第一个字
使用 LeakCanary 监控内存泄露
使用腾讯 bugly 监控crash
## 工具1:新闻导航  
使用 mvp 架构设计进行解耦  
使用 TabHost + Fragment 分页多个新闻来源
使用 fastJson 处理从网站 API 拿到的消息  
使用第三方框架 AutoLoadRecyclerView 实现在 recyclerView 列表布局基础上的上滑加载和下拉刷新。  
使用 Glide 进行图片加载和缓存  
使用 viewpager 进行图片的循环轮播和优化  
封装 Volley Request 获取网络消息直接返回可用bean
仿知乎首页轮播照片加文字处理效果，使文字能够清晰显示
对获取的网页进行初步设置使大部分网页可以符合屏幕大小
## 工具2:五子棋  
使用自定义 View 实现可配置的棋盘大小  
自己实现电脑 AI  
## 工具3:倒计时  
使用自定义 view 实现滚动选择时间  
## 工具4:简易音乐客户端
使用 TabLayout + viewpager 做主页面
使用 service 建立新播放进程
使用 aidl 连接播放进程与主进程
使用代理模式对播放进程的接口进行封装
实现了单曲循环、循环播放、随机播放模式
实现了 Notification 中对音乐的上一首、下一首、暂停、播放、退出操作
使用 dialog 选择 sd 卡目录
音乐数据不同系统数据库获取，而是扫描 SD 卡或选择 SD 卡中某目录。
## util 工具包
自定义 LogUtil 可以使 android studio 的 logcat 中点击跳转到打印代码处。
