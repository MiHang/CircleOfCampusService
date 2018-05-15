package team.coc.test;

import rasencrypt.encrypt.RSAEncrypt;
import team.coc.dao.CommonDao;
import team.coc.pojo.*;

import java.util.List;

public class HibernateTest {

    public static void main(String[]args) {

        init();
    }

    private void scu() {
        //校园表示例数据
        CommonDao<Campus> campusCommonDao = new CommonDao<Campus>();
        Campus campus = new Campus();
        campus.setCampusAccount("scu");
        campus.setCampusName("四川大学");
        campus.setIntroduction("简介");
        campus.setPassword("123456");
        campusCommonDao.save(campus);

        // 查询list
        List<Campus> campusList = campusCommonDao.findAll(Campus.class);

        //院系表示例数据
        CommonDao<Faculty> facultyCommonDao = new CommonDao<Faculty>();
        Faculty faculty1 = new Faculty("材料科学与工程学院", campusList.get(1));
        facultyCommonDao.save(faculty1);
        Faculty faculty2 = new Faculty("电气信息学院", campusList.get(1));
        facultyCommonDao.save(faculty2);
        Faculty faculty3 = new Faculty("电子信息学院", campusList.get(1));
        facultyCommonDao.save(faculty3);
        Faculty faculty4 = new Faculty("法学院", campusList.get(1));
        facultyCommonDao.save(faculty4);
        Faculty faculty5 = new Faculty("高分子科学与工程学院", campusList.get(1));
        facultyCommonDao.save(faculty5);
        Faculty faculty6 = new Faculty("公共管理学院", campusList.get(1));
        facultyCommonDao.save(faculty6);
        Faculty faculty7 = new Faculty("化学工程学院", campusList.get(1));
        facultyCommonDao.save(faculty7);
        Faculty faculty8 = new Faculty("计算机学院", campusList.get(1));
        facultyCommonDao.save(faculty8);
        Faculty faculty9 = new Faculty("艺术学院", campusList.get(1));
        facultyCommonDao.save(faculty9);
    }

    private static void init() {

        // RSA加密对象
        RSAEncrypt rsaEncrypt = new RSAEncrypt();

        //校园表示例数据
        CommonDao<Campus> campusCommonDao = new CommonDao<Campus>();
        Campus campus = new Campus();
        campus.setCampusAccount("cdp");
        campus.setCampusName("成都职业技术学院");
        campus.setIntroduction("简介");
        campus.setPassword(rsaEncrypt.encrypt("123456"));
        campusCommonDao.save(campus);

        // 查询list
        List<Campus> campusList = campusCommonDao.findAll(Campus.class);

        //院系表示例数据
        CommonDao<Faculty> facultyCommonDao = new CommonDao<Faculty>();
        Faculty faculty1 = new Faculty("软件分院", campusList.get(0));
        facultyCommonDao.save(faculty1);
        Faculty faculty2 = new Faculty("财经分院", campusList.get(0));
        facultyCommonDao.save(faculty2);
        Faculty faculty3 = new Faculty("旅游分院", campusList.get(0));
        facultyCommonDao.save(faculty3);
        Faculty faculty4 = new Faculty("工商管理与房地产分院", campusList.get(0));
        facultyCommonDao.save(faculty4);
        Faculty faculty5 = new Faculty("医护分院", campusList.get(0));
        facultyCommonDao.save(faculty5);

        // 查询list
        List<Faculty> facultyList = facultyCommonDao.findAll(Faculty.class);

        // 用户表示例数据
        CommonDao<User> userCommonDao = new CommonDao<User>();
        User user1 = new User();
        user1.setUserName("jaye");
        user1.setEmail("jayevip@163.com");
        user1.setPwd(rsaEncrypt.encrypt("123456"));
        user1.setGender(User.MALE);
        user1.setBirthday("1997-11-09");
        user1.setFaculty(facultyList.get(0));
        userCommonDao.save(user1);

        // 查询list
        List<User> userList = userCommonDao.findAll(User.class);

        // 添加社团示例数据
        CommonDao<Society> societyCommonDao = new CommonDao<Society>();
        Society society1 = new Society("棋艺社", campusList.get(0));
        societyCommonDao.save(society1);
        Society society2 = new Society("羽毛球社", campusList.get(0));
        societyCommonDao.save(society2);

        // 校园圈示例数据
//        CommonDao<CampusCircle> campusCircleCommonDao = new CommonDao<CampusCircle>();
//        CampusCircle campusCircle = new CampusCircle("这是一条校园圈消息",
//                "", "", new Date(), campusList.get(0));
//        campusCircleCommonDao.save(campusCircle);
//
//        // 社团圈示例数据
//        CommonDao<SocietyCircle> societyCircleCommonDao = new CommonDao<SocietyCircle>();
//        SocietyCircle societyCircle = new SocietyCircle("这是一条社团圈消息",
//                "", "", new Date(), 0, userList.get(0));
//        societyCircleCommonDao.save(societyCircle);

    }

}
