package team.coc.test;

import team.coc.dao.DemoDao;
import team.coc.pojo.Demo;

public class HibernateTest {

    public static void main(String[]args) {
        DemoDao demoDao = new DemoDao();
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("小明");
        demo.setPassword("123456");
        demoDao.save(demo);
    }

}
