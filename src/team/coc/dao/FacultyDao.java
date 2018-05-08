package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.Faculty;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 院系表操作类
 */
public class FacultyDao extends CommonDao<Faculty> {

    /**
     * 通过学校ID获取学校的所有院系
     * @param campusId - 学校ID
     * @return List<Object[]>
     *     Object[0] - 院系ID
     *     Object[1] - 院系名称
     */
    public List<Object[]> getAllFacultiesByCampusId(int campusId) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object[]> objects;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select facultyId, facultyName from Faculty where campus.campusId = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, campusId);
            objects = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (objects != null && objects.size() > 0) {
            return objects;
        } else {
            return null;
        }
    }

}
