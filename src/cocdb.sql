-- ----------------------------------------------------------
-- 创建名为 circle_of_campus 的数据库
-- 设置数据库编为utf8
-- 设置数据库字符集排序规则为utf8默认排序规则utf8_general_ci
-- ----------------------------------------------------------
  create database circle_of_campus
  character set utf8
  collate utf8_general_ci;

-- ----------------------
-- 使用数据库 circle_of_campus
-- ----------------------
  use circle_of_campus;

-- ------------------------------------------创建表-------------------------------------------------------
-- ----------------------------------------------
-- 创建学校表(t_campus)
-- ----------------------------------------------
  create table t_campus (

    c_id int primary key auto_increment,       -- 学校ID，主键，自动增长
    campus_account varchar(10) unique not null,-- 学校账号, 唯一
    campus_name varchar(50) unique not null,   -- 学校名称, 唯一
    introduction varchar(50) null,              -- 学校简介
	  pwd varchar(255) not null                  -- 学校管理员密码

  );

-- ----------------------------------------------
-- 创建院系表(t_faculty)
-- ----------------------------------------------
  create table t_faculty(

    f_id int primary key auto_increment,  -- 院系ID，主键，自动增长
    faculty_name varchar(50) not null,    -- 院系名
    c_id int not null,                    -- 学校ID, 外键

    foreign key(c_id) references t_campus(c_id)
  );

-- ----------------------------------------------
-- 社团表(t_society)
-- ----------------------------------------------
  create table t_society(

    s_id int primary key auto_increment,-- 社团ID，主键，自动增长
    society_name varchar(20) not null,	-- 社团名称
    c_id int not null,                  -- 学校ID, 外键

    foreign key(c_id) references t_campus(c_id)
  );

-- ----------------------------------------------
-- 创建用户表(t_user)
-- ----------------------------------------------
  create table t_user(

    u_id int primary key auto_increment,  -- 用户ID，主键，自动增长
    user_name varchar(10) unique not null,-- 用户名, 唯一
    email varchar(50) unique not null,    -- 电子邮箱, 唯一
    pwd varchar(255) not null,            -- 用户密码
    gender enum('male','female') null,    -- 性别(male or female or null)
    f_id int not null,                    -- 院系ID, 外键

    foreign key(f_id) references t_faculty(f_id)
  );

-- ----------------------------------------------
-- 社团权限申请表(t_society_request)
-- ----------------------------------------------
  create table t_society_request(

		id int primary key auto_increment,    -- 社团权限申请ID，主键，自动增长
    s_id int not null,                    -- 社团ID，外键
    u_id int not null,                    -- 用户ID, 外键
    request_reason varchar(100) not null, -- 申请理由
	  request_time datetime not null,		    -- 申请时间
	  result int not null,                  -- 处理结果 0未处理 1 申请成功 2 申请失败
    deal_reason varchar(50) null,         -- 处理原因
	  deal_time datetime null,              -- 处理时间

    foreign key(u_id) references t_user(u_id),
    foreign key(s_id) references t_society(s_id)
  );

 -- ----------------------------------------------
-- 创建好友申请表(t_good_friend_request)
-- ----------------------------------------------
  create table t_good_friend_request(

    id int primary key auto_increment,
	  u1_id int not null,                   -- 好友1ID，外键
	  u2_id int not null,                   -- 好友2ID，外键
	  request_reason varchar(100) not null, -- 申请理由
	  request_time datetime not null,		        -- 申请时间
	  result int not null,			            -- 处理结果 0未处理 1 申请成功 2 申请失败

    foreign key(u1_id) references t_user(u_id),
    foreign key(u2_id) references t_user(u_id)

  );

  -- ----------------------------------------------
-- 创建好友表(t_good_friend)
-- ----------------------------------------------
  create table t_good_friend(

	  u1_id int not null,                    -- 好友1ID，联合主键，外键
    u2_id int not null,                    -- 好友2ID，联合主键, 外键
    u1_notice varchar(20) null,            -- 好友1备注
    u2_notice varchar(20) null,            -- 好友2备注

    primary key(u1_id, u2_id),
    foreign key(u1_id) references t_user(u_id),
    foreign key(u2_id) references t_user(u_id)
  );

-- ----------------------------------------------
-- 创建群组表(t_group)
-- ----------------------------------------------
  create table t_group(

		group_id int primary key auto_increment,  -- 群组ID，主键，自动增长
    group_account varchar(10) unique not null,-- 群组账号
    group_name varchar(20) not null           -- 群组名称

  );

-- ----------------------------------------------
-- 创建群组成员表(t_group_member)
-- ----------------------------------------------
  create table t_group_member(

		id int primary key auto_increment, -- 群组成员表ID，主键，自动增长
		group_id int not null,             -- 群组ID，外键
    u_id int not null,                 -- 用户ID，外键

    foreign key(u_id) references t_user(u_id),
    foreign key(group_id) references t_group(group_id)

  );

-- ----------------------------------------------
-- 创建校园圈表(t_campus_circle)
-- ----------------------------------------------
  create table t_campus_circle(

    id int primary key auto_increment,-- 校园圈ID，主键，自动增长
    c_id int not null,                -- 学校ID，发布者，外键
    title varchar(100) not null,       -- 标题
	  content varchar(500) null,        -- 文本内容
    images_url varchar(1000) null,    -- 图片地址，多个图片使用json数组表示
	  publish_time datetime not null,	  -- 发布时间
    venue varchar(100) null,           -- 活动地点
    activity_time varchar(50) null,   -- 活动时间

    foreign key(c_id) references t_campus(c_id)
  );

-- ----------------------------------------------
-- 创建社团圈表(t_society_circle)
-- ----------------------------------------------
  create table t_society_circle(

    id int primary key auto_increment,-- 社团圈ID，主键，自动增长
    u_id int not null,                -- 发布者，用户ID，外键
    title varchar(100) not null,       -- 标题
	  content varchar(500) null,        -- 文本内容
    images_url varchar(1000) null,    -- 图片地址，多个图片使用json数组表示
	  publish_time datetime not null,		-- 发布时间
    venue varchar(100) null,           -- 活动地点
    activity_time varchar(50) null,   -- 活动时间
    auditing int not null,            -- 审核状态，1 - 已审核，0 - 未审核

    foreign key(u_id) references t_user(u_id)
  );

-- ----------------------------
-- 向校园表插入示例数据
-- ----------------------------
insert into `t_campus` values (1, 'cdp', '成都职业技术学院', '简介', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd');

-- ----------------------------
-- 向院系表插入示例数据
-- ----------------------------
insert into `t_faculty` values (1, '软件分院', '1');
insert into `t_faculty` values (2, '财经分院', '1');
insert into `t_faculty` values (3, '旅游分院', '1');
insert into `t_faculty` values (4, '工商管理与房地产分院', '1');
insert into `t_faculty` values (5, '医护分院', '1');

-- ----------------------------
-- 向社团表插入示例数据
-- ----------------------------
insert into `t_society` values (1, '棋艺社', '1');
insert into `t_society` values (2, '羽毛球社', '1');

-- ----------------------------
-- 向用户表插入示例数据
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'jaye', 'jayevip@163.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', 'male', '1');
INSERT INTO `t_user` VALUES ('2', 'test1', 'test1@coc.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', 'male', '2');
INSERT INTO `t_user` VALUES ('3', 'test2', 'test2@coc.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', 'female', '3');
INSERT INTO `t_user` VALUES ('4', 'test3', 'test3@coc.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', 'female', '4');

-- ----------------------------
-- Records of t_society_request
-- ----------------------------
INSERT INTO `t_society_request` VALUES ('1', '1', '1', '用于发布社团公告', '2018-06-26 23:00:41', '1', '通过', '2018-06-26 23:00:41');

-- ----------------------------
-- 向校园圈表插入示例数据
-- ----------------------------
INSERT INTO `t_campus_circle` VALUES ('1', '1', '\"书香成职\"系列活动成果展', '    为了弘扬和传承中华优秀传统文化，培育和践行社会主义核心价值观，宣传贯彻党的十九大精神，营造浓浓的书香文化氛围， 4月23日下午，由学院图书馆主办的学院首届经典诵读大赛活动在高新校区财经分院学术报告厅举行。学院党委书记周鑑、党委副书记、院长王涛、党委副书记、纪委书记邹克俭、以及各分院部（处室）负责人、部分教师代表出席本次活动。\n	\n    参加此次比赛的七支参赛队伍，全部是各分院初赛的胜出者。参赛节目有《大漠敦煌》、《满江红》、《沁园春•长沙》、《沁园春•雪》、《百年复兴 经典伴随》、《诵•离•合》、《星汉灿烂 若出其里》、《棋哥随手吟》。\n	\n    经过激烈地竞争，最终财经分院、工房分院、旅游分院分别荣获荣获一、二、三等奖。其他分院获得优胜奖。至此，学院首届经典诵读大赛活动落下了帷幕。', '[{\"url\":\"coc/campus_circle/f9581cd3c621420eb764897a3fe315c9\"}]', '2018-04-22 18:10:32', '图书馆', '2018年4月26日至10日');
INSERT INTO `t_campus_circle` VALUES ('2', '1', '\"五四青年节\"文艺汇演', '一、汇演主题:拥抱青春 祝福明天\n\n二、汇演组织:\n    1.主办：成都职业技术学院政教处\n    2.承办：成都职业技术学院团委\n    3.协办：成都职业技术学院各协会、社团\n	\n三、汇演日程：\n    1.演出时间：2018年5月4号 下午6:00\n	\n四、汇演地点：学术交流中心', '[{\"url\":\"coc/campus_circle/76cbf6a423c74619b230b46b2f03ca6e\"}]', '2018-04-29 18:10:32', '学术交流中心', '2018年5月4日 下午6:00');
INSERT INTO `t_campus_circle` VALUES ('3', '1', '校园KOL 颜值担当 非你莫属', '招聘岗位：校园KOL \n工作职责： \n1、负责在本校中宣传国外留学相关咨询活动 \n2、负责本校与本校团委学生会等学校组织建立合作关系 \n\n我们需要你有什么？ \n1、超强的执行力和团队合作精神 \n2、具有良好的人际沟通能力，善于与人沟通、打交道，勇于突破自己 \n\n你会收获什么？ \n1、开具实习证明，表现优秀者可转正 \n2、固定的岗位薪酬及提成 \n3、定期与各大院校优秀KOL交流机会 \n\n时间：2018年5月2日下午2点\n地点：学术交流中心', '[{\"url\":\"coc/campus_circle/22d3b508012d49e1900353563c3afe7a\"}]', '2018-06-27 18:10:32', '学术交流中心', '2018年6月30日 下午2:00');

-- ----------------------------
-- 向社团圈表插入示例数据
-- ----------------------------
INSERT INTO `t_society_circle` VALUES ('1', '1', '羽毛球社团招新', '    学弟学妹们大家好！这里是羽毛球社啦，首先说一句经典：羽毛球社欢迎你！(✿✪‿✪｡)ﾉ\n    \n	简单介绍一下哈，羽毛球社呢，就是一个关于打羽毛球，了解羽毛球，喜欢羽毛球的一群人组成的一个社团。当然，如果你不了解它，也从未打过它（感觉我们在虐待羽毛球这种生物\"▔□▔），但是你想要去学习这个新技能那么这个社团的大门是为你敞开哒~\\(≧▽≦)/~！\n	\n	时间：2018年5月1日 下午5:00\n    地点：高新篮球场', '[{\"url\":\"coc/society_circle/28cc5b4b6ebf42108533a0531a94fc44\"}]', '2018-05-04 12:32:58', '篮球场旁', '2018年05月06日', '1');
INSERT INTO `t_society_circle` VALUES ('2', '1', '羽毛球社团周年庆', '活动主题：“喜迎佳日,共渡羽毛球社周年庆”\n\n活动开展形式: 唱歌、跳舞、小品、相声、走秀等。\n\n活动时间：2018年06月03日 18:30 - 21:00\n\n活动地点：学术交流中心\n\n主办单位:成都职业技术学院学生羽毛球社', '[{\"url\":\"coc/society_circle/97e4bb315c334396bf07c3738e76ef52\"}]', '2018-06-01 18:32:58', '学术交流中心', '2018年06月03日 18:30', '1');
INSERT INTO `t_society_circle` VALUES ('3', '1', '激情绽放 \"羽\"你共享 第九届校园羽毛球比赛', '一、竞赛时间：2018年06月20日\n\n二、竞赛地点：高新体育馆\n\n三、比赛项目：男子双打、女子双打、混合双打\n\n四、 报名办法\n      1、男子双打、女子双打、混合双打各项目均可组队报名，不限报名队数，参赛选手不可兼项。\n      2、报名时间与地点：\n         各参赛队员于2018年06月12日12：00之前将报名名单交羽毛球社\n      3、温馨提示：每个参赛队一定在赛前做适量的准备活动，避免运动受伤。', '[{\"url\":\"coc/society_circle/ae1ec5ad70414e1d9a61746e748d7449\"}]', '2018-06-13 15:45:58', '高新校区体育馆', '2018年06月20日', '1');
