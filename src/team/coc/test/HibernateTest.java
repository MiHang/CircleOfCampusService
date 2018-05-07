package team.coc.test;

import team.coc.dao.CommonDao;
import team.coc.pojo.*;

import java.util.Date;
import java.util.List;

public class HibernateTest {

    public static void main(String[]args) {

        //校园表示例数据
        CommonDao<Campus> campusCommonDao = new CommonDao<Campus>();
        Campus campus = new Campus();
        campus.setCampusAccount("cdvtc");
        campus.setCampusName("成都职业技术学院");
        campus.setIntroduction("简介");
        campus.setPassword("123456");
        campusCommonDao.save(campus);

        // 查询list
        List<Campus> campusList = campusCommonDao.findAll(Campus.class);

        //院系表示例数据
        CommonDao<Faculty> facultyCommonDao = new CommonDao<Faculty>();
        Faculty faculty = new Faculty("软件分院", campusList.get(0));
        facultyCommonDao.save(faculty);

        // 查询list
        List<Faculty> facultyList = facultyCommonDao.findAll(Faculty.class);

        // 用户表示例数据
        CommonDao<User> userCommonDao = new CommonDao<User>();
        User user1 = new User();
        user1.setUserName("李华");
        user1.setEmail("12345678@qq.com");
        user1.setPwd("123456");
        user1.setGender(User.MALE);
        user1.setBirthday("1997-12-12");
        user1.setNativePlace("四川成都");
        user1.setFaculty(facultyList.get(0));

        User user2 = new User();
        user2.setUserName("韩梅梅");
        user2.setEmail("87654321@qq.com");
        user2.setPwd("123456");
        user2.setGender(User.FEMALE);
        user2.setBirthday("1998-09-09");
        user2.setNativePlace("四川成都");
        user2.setFaculty(facultyList.get(0));

        userCommonDao.save(user1);
        userCommonDao.save(user2);

        // 添加社团示例数据
        CommonDao<Society> societyCommonDao = new CommonDao<Society>();
        Society society = new Society("羽毛球社", campusList.get(0));
        societyCommonDao.save(society);

        // 查询list
        List<Society> societyList = societyCommonDao.findAll(Society.class);

        // 校园圈示例数据
        CommonDao<CampusCircle> campusCircleCommonDao = new CommonDao<CampusCircle>();
        CampusCircle campusCircle = new CampusCircle("这是一条校园圈消息",
                "", "", new Date(), campusList.get(0));
        campusCircleCommonDao.save(campusCircle);

        // 社团圈示例数据
        CommonDao<SocietyCircle> societyCircleCommonDao = new CommonDao<SocietyCircle>();
        SocietyCircle societyCircle = new SocietyCircle("这是一条社团圈消息",
                "", "", new Date(), societyList.get(0));
        societyCircleCommonDao.save(societyCircle);

    }

}
