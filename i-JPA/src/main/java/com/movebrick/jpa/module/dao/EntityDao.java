package com.movebrick.jpa.module.dao;

import com.movebrick.jpa.module.entity.QueryResult;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EntityDao {
    @PersistenceContext
    protected EntityManager em;

    public int executeBatchNativeSql(List<String> sqls, List<Map<String, Object>> params) {
        int affectedLine = 0;
        Query query = null;
        for (int i = 0; i < sqls.size(); i++) {
            query = em.createNativeQuery(sqls.get(i));
            for (Map.Entry<String, Object> entry : params.get(i).entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            affectedLine = affectedLine + query.executeUpdate();
        }
        return affectedLine;
    }

    public int executeNativeUpdate(String nativeSql, Map<String, Object> params) {
        Query query = em.createNativeQuery(nativeSql);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.executeUpdate();
    }

    public int executeNativeUpdate(String nativeSql, Object[] params) {
        Query query = em.createNativeQuery(nativeSql);
        if (params != null && params.length > 0) {
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i - 1]);
            }
        }
        return query.executeUpdate();
    }

    public void save(Object entity) {
        em.persist(entity);
    }

    public void update(Object entity) {
        em.merge(entity);
    }

    public <T> void delete(Class<T> entityClass, Object entityid) {
        delete(entityClass, new Object[] { entityid });
    }

    public <T> void delete(Class<T> entityClass, Object[] entityids) {
        for (Object id : entityids) {

            em.remove(em.getReference(entityClass, id));

        }
    }

    public <T> T find(Class<T> entityClass, Object entityId) {

        return em.find(entityClass, entityId);
    }

    @SuppressWarnings("unchecked")
    public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstindex, int maxresult, String whereql, Object[] queryParams, LinkedHashMap<String, String> orderby) {
        QueryResult qr = new QueryResult<T>();
        String entityname = getEntityName(entityClass);
        Query query = em.createQuery("select o from " + entityname + " o " + (whereql == null ? "" : "where " + whereql) + buildOrderby(orderby));
        if (firstindex != -1 && maxresult != -1)
            query.setFirstResult(firstindex).setMaxResults(maxresult);

        setQueryParams(query, queryParams);
        qr.setResultList(query.getResultList());
        query = em.createQuery("select count(o) from " + entityname + " o " + (whereql == null ? "" : "where " + whereql));
        setQueryParams(query, queryParams);
        qr.setTotalrecord((Long) query.getSingleResult());

        return qr;
    }

    public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstindex, int maxresult, LinkedHashMap<String, String> orderby) {
        return getScrollData(entityClass, firstindex, maxresult, null, null, orderby);

    }

    public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstindex, int maxresult, String whereql, Object[] queryParams) {
        return getScrollData(entityClass, firstindex, maxresult, whereql, queryParams, null);

    }

    public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstindex, int maxresult) {
        return getScrollData(entityClass, firstindex, maxresult, null, null, null);

    }

    public <T> QueryResult<T> getScrollData(Class<T> entityClass) {
        return getScrollData(entityClass, -1, -1);

    }

    protected void setQueryParams(Query query, Object[] queryParams) {
        if (queryParams != null && queryParams.length > 0) {

            for (int i = 0; i < queryParams.length; i++) {
                query.setParameter(i + 1, queryParams[i]);

            }

        }

    }

    protected String buildOrderby(LinkedHashMap<String, String> orderby) {

        StringBuffer orderbyql = new StringBuffer();
        if (orderby != null && orderby.size() > 0) {
            orderbyql.append(" order by ");
            for (String key : orderby.keySet()) {
                orderbyql.append("o.").append(key).append(" ").append(orderby.get(key)).append(",");

            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);

        }
        return orderbyql.toString();
    }

    protected <T> String getEntityName(Class<T> entityClass) {
        String entityname = entityClass.getSimpleName();
        Entity entity = entityClass.getAnnotation(Entity.class);

        if (entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();

        }
        return entityname;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getScrollDataMap(String sql, Object[] queryParams,List<String> mapKeyList) {
        Query query = em.createNativeQuery(sql);
        if (queryParams != null && queryParams.length > 0) {
            for (int i = 1; i <= queryParams.length; i++) {
                query.setParameter(i, queryParams[i - 1]);
            }
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<Map<String, Object>>) query.getResultList();
    }

    /**
     * @Author Shin L
     * @Description  可用，查询的结果是map的集合
     * @Date 16:27 2019/5/14
     * @Param
     * @return
     **/
    public List<Map<String, Object>> getScrollDataMap(String sql, Object[] queryParams) {
        Query query = em.createNativeQuery(sql);
        if (queryParams != null && queryParams.length > 0) {
            for (int i = 1; i <= queryParams.length; i++) {
                query.setParameter(i, queryParams[i - 1]);
            }
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<Map<String, Object>>) query.getResultList();
    }

}
