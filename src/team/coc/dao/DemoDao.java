package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import team.coc.pojo.Demo;
import team.coc.util.HibernateUtils;

import java.util.List;

public class DemoDao {

    /**
     * 保存Demo表数据
     * @param demo
     */
    public void save(Demo demo) {
        Session session = HibernateUtils.openSession();
        try {
            Transaction tx = session.beginTransaction(); // 开启事务
            session.save(demo); // 保存数据
            tx.commit(); // 提交事务
        } catch (RuntimeException e) {
            session.getTransaction().rollback(); // 回滚事务
            throw e;
        } finally {
            session.close(); // 关闭session
        }
    }

    /*
     * 更新
     */
    public void update(Demo demo) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction(); // 开启事务
            session.update(demo);// 更新表数据
            tx.commit(); // 提交事务
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /*
     * 删除
     */
    public void delete(int id) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Demo demo = session.get(Demo.class, id); // 要先获取到这个对象
            session.delete(demo); // 删除的是实体对象
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /*
     * 根据id查询一个User数据
     */
    public Demo getById(int id) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Demo demo = (Demo) session.get(Demo.class, id);// 操作
            tx.commit();
            return demo;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /*
     * 查询所有
     */
    public List<Demo> findAll() {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Demo> list = session.createQuery("FROM Demo").list(); // 使用HQL查询
            tx.commit();
            return list;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * 分页的查询数据列表
     * @param firstResult 从结果列表中的哪个索引开始取数据
     * @param maxResults 最多取多少条数据
     * @return 一页的数据列表
     */
    @SuppressWarnings("unchecked")
    public List<Demo> findAll(int firstResult, int maxResults) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // 查询一页的数据列表
            // 方式一：使用HQL查询
            // Query query = session.createQuery("FROM User");
            // query.setFirstResult(firstResult);
            // query.setMaxResults(maxResults);
            // List<User> list = query.list();

            // 方式二：方法链
            List<Demo> list = session.createQuery( //
                    "FROM Demo") //
                    .setFirstResult(firstResult) //
                    .setMaxResults(maxResults) //
                    .list();

            tx.commit();

            // 返回结果
            return list;
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
