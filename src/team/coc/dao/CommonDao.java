package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import team.coc.util.HibernateUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CommonDao<T> {

    /**
     * 获取泛型T的 class
     * @return
     */
    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    /**
     * 保存table表数据
     * @param table
     * @return 保存成功返回true，保存失败返回false
     */
    public boolean save(T table) {
        boolean isSuccess = false;
        Session session = HibernateUtils.openSession();
        try {
            Transaction tx = session.beginTransaction(); // 开启事务
            session.save(table); // 保存数据
            tx.commit(); // 提交事务
            isSuccess = true;
        } catch (RuntimeException e) {
            isSuccess = false;
            session.getTransaction().rollback(); // 回滚事务
            throw e;
        } finally {
            session.close(); // 关闭session
        }
        return isSuccess;
    }

    /*
     * 更新table
     */
    public boolean update(T table) {
        boolean isSuccess = false;
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction(); // 开启事务
            session.update(table);// 更新表数据
            tx.commit(); // 提交事务
            isSuccess = true;
        } catch (RuntimeException e) {
            isSuccess = false;
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return isSuccess;
    }

    /*
     * 删除
     */
    public void delete(int id) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T table = session.get(getTClass(), id); // 要先获取到这个对象
            session.delete(table); // 删除的是实体对象
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /*
     * 根据id查询一个Table数据
     */
    public T getById(int id) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T table = (T) session.get(getTClass(), id);// 操作
            tx.commit();
            return table;
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
    public List<T> findAll(Class<T> table) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<T> list = session.createQuery(("FROM " + table.getName())).list(); // 使用HQL查询
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
    public List<T> findAll(Class<T> table, int firstResult, int maxResults) {
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
            List<T> list = session.createQuery( //
                    ("FROM " + table.getName())) //
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
