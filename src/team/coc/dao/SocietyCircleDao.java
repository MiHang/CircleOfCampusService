package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.CampusCircle;
import team.coc.pojo.SocietyCircle;
import team.coc.util.HibernateUtils;

import java.util.List;

public class SocietyCircleDao extends CommonDao<SocietyCircle> {

    /**
     * 通过用户ID进行分页查询该用户发布的社团圈消息
     * @param uId - 用户ID
     * @param start - 查询开始的位置
     * @param end - 查询结束的位置
     * @return
     */
    public List<SocietyCircle> getMyPublishSocietyCircle(int uId, int start, int end) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<SocietyCircle> societyCircles;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from SocietyCircle where user.id = ? order by convert(publishTime, datetime) desc";
            Query query = session.createQuery(hql);
            query.setParameter(0, uId);
            query.setFirstResult(start);
            query.setMaxResults(end);
            societyCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (societyCircles != null && societyCircles.size() > 0) {
            return societyCircles;
        } else {
            return null;
        }
    }

    /**
     * 通过学校ID进行分页查询该学校的社团圈消息
     * @param campusId - 学校ID
     * @param start - 查询开始的位置
     * @param end - 查询结束的位置
     * @return
     */
    public List<SocietyCircle> getSocietyCircle(int campusId, int start, int end) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<SocietyCircle> societyCircles;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from SocietyCircle where user.faculty.campus.id = ? order by convert(publishTime, datetime) desc";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            query.setFirstResult(start);
            query.setMaxResults(end);
            societyCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (societyCircles != null && societyCircles.size() > 0) {
            return societyCircles;
        } else {
            return null;
        }
    }

    /**
     * 查询某用户已发布的社团圈信息的大小(数量)
     * @param uId - 用户ID
     * @return
     */
    public int getMyPublishSocietyCircleSize(int uId) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> societyCircles = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select id from SocietyCircle where user.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, uId);
            societyCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (societyCircles != null && societyCircles.size() > 0) {
            return societyCircles.size();
        } else {
            return 0;
        }
    }

    /**
     * 查询某学校社团圈信息的大小(数量)
     * @param campusId - 校园ID
     * @return
     */
    public int getSocietyCircleSize(int campusId) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> societyCircles = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select id from SocietyCircle where user.faculty.campus.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            societyCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (societyCircles != null && societyCircles.size() > 0) {
            return societyCircles.size();
        } else {
            return 0;
        }
    }

}
