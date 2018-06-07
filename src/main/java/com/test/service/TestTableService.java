package com.test.service;

import com.test.dao.primary.TestTablePrimaryRepository;
import com.test.dao.secondary.TestTableSecondaryRepository;
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
    private TestTablePrimaryRepository testTablePrimaryRepository;

    @Autowired
    private TestTableSecondaryRepository testTableSecondaryRepository;

    public List<TestTable> getTestTables1()
    {
        List<TestTable> testTables = testTablePrimaryRepository.findAll();

        return testTables;
    }

    public List<TestTable> getTestTables2()
    {
        List<TestTable> testTables = testTableSecondaryRepository.findAll();

        return testTables;
    }

    public List<TestTable> getTestTables3()
    {
        List<TestTable> testTables1 = testTablePrimaryRepository.findAll();

        List<TestTable> testTables2 = testTableSecondaryRepository.findAll();

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
        testTablePrimaryRepository.save(testTable);

        testTable = testTablePrimaryRepository.findOne(1l);
        testTable.setName("1");
        testTablePrimaryRepository.save(testTable);

        System.out.println(1);
    }

    @Transactional(transactionManager="transactionManagerSecondary")
    public void updateTestTable2()
    {
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setStatus(1);
        testTableSecondaryRepository.save(testTable);

        testTable = testTableSecondaryRepository.findOne(1l);
        testTable.setName("1");
        testTableSecondaryRepository.save(testTable);

        System.out.println(1);
    }

//    @Transactional
    @Transactional(transactionManager="transactionManagerSecondary")
    public void updateTestTable3()
    {
        TestTable testTable = new TestTable();
        testTable.setName("test");
        testTable.setStatus(1);

        testTablePrimaryRepository.save(testTable);

        testTableSecondaryRepository.save(testTable);

        System.out.println(1);
    }
}
