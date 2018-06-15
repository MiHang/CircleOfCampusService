package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.SocietyCircle;
import team.coc.pojo.SocietyRequest;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 社团权限申请表数据库操作类
 */
public class SocietyRequestDao extends CommonDao<SocietyRequest> {

    /**
     * 查询此用户是否拥有发布社团圈的权限
     * @param uId - 用户
     * @return
     */
    public SocietyRequest getSocietyRequestByUid(int uId) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<SocietyRequest> societyRequests;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from SocietyRequest where user.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, uId);
            societyRequests = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (societyRequests != null && societyRequests.size() > 0) {
            return societyRequests.get(0);
        } else {
            return null;
        }
    }

}
