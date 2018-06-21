package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.CampusCircle;
import team.coc.util.HibernateUtils;

import java.util.List;

public class CampusCircleDao extends CommonDao<CampusCircle> {

    /**
     * 通过学校ID进行分页查询该学校的校园圈消息
     * @param campusId - 学校ID
     * @param start - 查询开始的位置
     * @param end - 查询结束的位置
     * @return
     */
    public List<CampusCircle> getCampusCircles(int campusId, int start, int end) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<CampusCircle> campusCircles;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from CampusCircle where campus.campusId = ? order by convert(publishTime, datetime) desc";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            query.setFirstResult(start);
            query.setMaxResults(end);
            campusCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (campusCircles != null && campusCircles.size() > 0) {
            return campusCircles;
        } else {
            return null;
        }
    }

    /**
     * 查询学校校园圈信息的大小(数量)
     * @param campusId - 校园ID
     * @return
     */
    public int getCampusCircleSize(int campusId) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> campusCircles;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select id from CampusCircle where campus.campusId = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            campusCircles = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (campusCircles != null && campusCircles.size() > 0) {
            return campusCircles.size();
        } else {
            return 0;
        }
    }

}
