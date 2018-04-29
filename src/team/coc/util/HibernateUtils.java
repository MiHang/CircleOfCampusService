package team.coc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    // SessionFactory
    private static SessionFactory sessionFactory;

    static {
        /*
        Configuration cfg = new Configuration(); // 代表配置文件的一个对象
        cfg.configure();
        // cfg.configure("hibernate.cfg.xml");
        sessionFactory = cfg.buildSessionFactory();
        */
        sessionFactory = new Configuration() // 配置文件的一个对象
                .configure() // 读取默认的配置文件 /hibernate.cfg.xml
                //.configure("hibernate.cfg.xml") // 读取指定位置的配置文件
                .buildSessionFactory(); // 方法链
    }

    /**
     * 获取全局唯一的SessionFactory
     *
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 从全局唯一的SessionFactory中打开一个Session
     *
     * @return
     */
    public static Session openSession() {
        return sessionFactory.openSession();
    }
}
