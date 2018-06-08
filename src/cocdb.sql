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
insert into `t_user` values (1, 'jaye', 'jaye@163.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', 'male', '1');
insert into `t_user` values (2, 'demo', 'demo@163.com', '62da1e5d9d212e0ebd318c6182af760ef1c5de6b5c9ae4130f4f402f5bf8ac9df87e2654023c1ca36ae8f4ed89139a5db1baa72781190d22d2192bcd798de4bd', null, '1');

-- ----------------------------
-- 向校园圈表插入示例数据
-- ----------------------------
insert into `t_campus_circle` values ('1', '1', '校园KOL 颜值担当 非你莫属', '校园KOL 颜值担当 非你莫属', '[{\"url\":\"coc/campus_circle/ed9179813c6a45518afb159cd92c6d49\"}]', '2018-06-03 16:53:50', '学术交流中心', '2018年5月2日 下午2:00');
insert into `t_campus_circle` values ('2', '1', '\"书香成职\"系列活动成果展', '\"书香成职\"系列活动成果展', '[{\"url\":\"coc/campus_circle/97a829f99b6f467eb8b2d81ea3be0024\"}]', '2018-06-03 16:53:50', '图书馆', '2018年5月3日至10日');
insert into `t_campus_circle` values ('3', '1', '\"五四青年节\"文艺汇演', '\"五四青年节\"文艺汇演', '[{\"url\":\"coc/campus_circle/9f7f256480524545b294fb1414335ca2\"}]', '2018-06-03 16:53:50', '学术交流中心', '2018年5月4日 下午6:00');

-- ----------------------------
-- 向社团圈表插入示例数据
-- ----------------------------
insert into `t_society_circle` values ('1', '1', '羽毛球社团招新', '羽毛球社团招新', '[{\"url\":\"coc/society_circle/4e3a9d02baf84e379a27896eb91936f4\"}]', '2018-06-03 17:13:20', '篮球场旁', '2018年5月1日', '1');
insert into `t_society_circle` values ('2', '1', '激情绽放 \"羽\"你共享 第九届校园羽毛球比赛', '激情绽放 \"羽\"你共享 第九届校园羽毛球比赛', '[{\"url\":\"coc/society_circle/cdc93338559e40a3a228e830718a4ab2\"}]', '2018-06-03 17:13:20', '高新校区体育馆', '2018年5月26日', '1');

