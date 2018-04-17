package com.test.dao;

import com.test.model.TestTable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTableRepository extends JpaRepository<TestTable, Integer>
{
    @Cacheable(value = "testTable:findOne", key = "#p0")
    TestTable findOne(Integer id);

    @CachePut(value = "testTable:findOne", key = "#p0.id")
    @Caching(evict = {@CacheEvict(value = "testTable:findAll", allEntries=true), @CacheEvict(value = "testTable:findAll2", key = "''")})
    TestTable save(TestTable testTable);

    @Caching(evict = {@CacheEvict(value = "testTable:findOne", key = "#p0"), @CacheEvict(value = "testTable:findAll", allEntries=true), @CacheEvict(value = "testTable:findAll2", key = "''")})
    void delete(Integer id);

    @Cacheable(value = "testTable:findAll2", key = "''")
    List<TestTable> findAll();

    @Cacheable(value = "testTable:findAll", key = "#p0.pageSize", condition="#p0.pageNumber==0")
    Page<TestTable> findAll(Pageable pageable);
}