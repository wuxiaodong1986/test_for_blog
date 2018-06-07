package com.test.dao.secondary;

import com.test.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 吴晓冬 on 2018/6/6.
 */
public interface TestTableSecondaryRepository extends JpaRepository<TestTable, Long>
{
}