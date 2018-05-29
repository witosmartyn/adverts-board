package com.witosmartyn.app.repositories;

import com.witosmartyn.app.config.enums.ItemField;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.model.SearchRequest;
import com.witosmartyn.app.repositories.SearchRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * vitali
 */
@Repository("hiber")
public class SearchRepositoryImplHibernate implements SearchRepository {
    private SessionFactory sessionFactory;
    private EntityManager entityManager;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }//setters

    public List findAll() {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Item.class);
        System.out.println(criteria.list());
        return criteria.list();
    }

    @Override
    public Page<Item> searchByRequest(SearchRequest sReq, Pageable pageable) {
        Session session = this.sessionFactory.openSession();
        final Criteria cr = session.createCriteria(Item.class, "item");

        if (sReq.getQuery() != null && !sReq.getQuery().equals("")) {
            resolveQueryAndDescription(sReq, cr);
        }
        if (sReq.getCity() != null) {
            cr.add(Restrictions.eq(ItemField.CITY.val(), sReq.getCity()));
        }
        if (sReq.getCategory() != null) {
            cr.add(Restrictions.eq(ItemField.CATEGORY.val(), sReq.getCategory()));
        }
        cr.add(Restrictions.between(ItemField.PRICE.val(), sReq.getFromPrice(), sReq.getToPrice()));

        long totalInDb = cr.list().size();

        cr.setMaxResults(sReq.getCountOnPage());
        cr.setFirstResult((sReq.getPage()) * sReq.getCountOnPage());
        if (sReq.getOrderBy().equals(Sort.Direction.ASC)) {
            cr.addOrder(Order.asc(sReq.getSortBy().val()));
        }else {
            cr.addOrder(Order.desc(sReq.getSortBy().val()));


        }
        final List listItems = cr.list();
        session.clear();
        session.close();

        final PageImpl<Item> items = new PageImpl<Item>(listItems, sReq.getPageRequest(), totalInDb);
        return items;
    }

    private void resolveQueryAndDescription(SearchRequest sR, Criteria cr) {
        final ArrayList<Criterion> criterions = new ArrayList<>();
        Criterion searchInTitle;
        if (sR.isIgnoreCase()){
            searchInTitle = Restrictions.ilike(ItemField.NAME.val(), sR.getQuery(), MatchMode.ANYWHERE);

        }else {
            searchInTitle = Restrictions.like(ItemField.NAME.val(), sR.getQuery(), MatchMode.ANYWHERE);
        }

        criterions.add(searchInTitle);

        if (sR.isSearchInDescription()){
            Criterion searchIninDescription;
            if (sR.isIgnoreCase()){
                searchIninDescription = Restrictions.ilike("description", sR.getQuery(), MatchMode.ANYWHERE);
            }else {
                searchIninDescription = Restrictions.like("description", sR.getQuery(), MatchMode.ANYWHERE);
            }
            criterions.add(searchIninDescription);
        }

        final Criterion[] criterionsArray = criterions.toArray(new Criterion[criterions.size()]);
        cr.add(Restrictions.or(criterionsArray));
    }

}
