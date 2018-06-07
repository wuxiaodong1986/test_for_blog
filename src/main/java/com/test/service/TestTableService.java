package com.test.service;

import com.test.config.DBContextHolder;
import com.test.dao.TestTableRepository;
import com.test.model.TestTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吴晓冬 on 2018/6/7.
 */
@Service
public class TestTableService
{
    @Autowired
    private TestTableRepository testTableRepository;

    public List<TestTable> getTestTables1()
    {
        List<TestTable> testTables = testTableRepository.findAll();

        return testTables;
    }

    public List<TestTable> getTestTables2()
    {
        DBContextHolder.setDbType(DBContextHolder.DB_TYPE_SECONDARY);
        List<TestTable> testTables = testTableRepository.findAll();

        return testTables;
    }

    public List<TestTable> getTestTables3()
    {
        List<TestTable> testTables1 = testTableRepository.findAll();

        DBContextHolder.setDbType(DBContextHolder.DB_TYPE_SECONDARY);
        List<TestTable> testTables2 = testTableRepository.findAll();

        List<TestTable> testTables = new ArrayList<>();
        testTables.addAll(testTables1);
        testTables.addAll(testTables2);

        return testTables;
    }

    @Transactional
    public void updateTestTable1()
    {
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setStatus(1);
        testTableRepository.save(testTable);

        testTable = testTableRepository.findOne(1l);
        testTable.setName("1");
        testTableRepository.save(testTable);

        System.out.println(1);
    }

    @Transactional
    public void updateTestTable2()
    {
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setStatus(1);
        testTableRepository.save(testTable);

        testTable = testTableRepository.findOne(1l);
        testTable.setName("1");
        testTableRepository.save(testTable);

        System.out.println(1);
    }

    @Transactional
    public void updateTestTable3()
    {
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setStatus(1);

        testTableRepository.save(testTable);

        DBContextHolder.setDbType(DBContextHolder.DB_TYPE_SECONDARY);

        testTableRepository.save(testTable);

        System.out.println(1);
    }
}