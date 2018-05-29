package com.witosmartyn.app.repositories;

import com.witosmartyn.app.entities.Category;
import com.witosmartyn.app.entities.Item;
import com.witosmartyn.app.entities.User;
import com.witosmartyn.app.entities.model.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * vitali
 */
@Repository
public interface SearchRepository {
    @Transactional(readOnly = true)
    Page<Item> searchByRequest(SearchRequest searchRequest,Pageable pageable);

}
