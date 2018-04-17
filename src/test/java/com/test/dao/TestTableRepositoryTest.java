package com.test.dao;

import com.test.model.TestTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTableRepositoryTest
{
    @Autowired
    private TestTableRepository testTableRepository;

    @Test
    public void save() throws Exception
    {
        TestTable testTable = new TestTable();
        testTable.setId(1);
        testTable.setName("test2");
        testTable.setStatus(2);

        testTableRepository.save(testTable);
    }

    @Test
    public void findOne() throws Exception
    {
        //返回null 不缓存
        TestTable testTable = testTableRepository.findOne(1);
        System.out.println(testTable);
    }

    @Test
    public void delete() throws Exception
    {
        testTableRepository.delete(1);
    }

    @Test
    public void findAll2() throws Exception
    {
        List<TestTable> testTables = testTableRepository.findAll();
        System.out.println(testTables);
    }

    @Test
    public void findAll() throws Exception
    {
        PageRequest pageRequest = new PageRequest(0, 2);

        Page<TestTable> page = testTableRepository.findAll(pageRequest);

        System.out.println(page.getContent());
    }
}