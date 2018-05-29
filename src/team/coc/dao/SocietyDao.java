package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.Society;
import team.coc.util.HibernateUtils;

import java.util.List;

public class SocietyDao extends CommonDao<Society> {

    /**
     * 通过学校ID获取该学校的所有社团信息
     * @param campusId - 学校ID
     * @return List<Society>
     */
    public List<Society> getAllSocietyByCampusId(int campusId) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Society> societies = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from Society where campus.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            societies = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        return societies;
    }

}
