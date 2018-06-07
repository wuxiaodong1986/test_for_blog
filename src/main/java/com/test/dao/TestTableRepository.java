package com.test.dao;

import com.test.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 吴晓冬 on 2018/6/7.
 */
public interface TestTableRepository extends JpaRepository<TestTable, Long>
{
}