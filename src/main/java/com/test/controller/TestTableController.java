package com.test.controller;

import com.test.config.DBContextHolder;
import com.test.model.TestTable;
import com.test.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 吴晓冬 on 2018/6/7.
 */
@RestController
public class TestTableController
{
    @Autowired
    private TestTableService testTableService;

    @GetMapping("/getTestTables1")
    public List<TestTable> getTestTables1()
    {
        List<TestTable> testTables = testTableService.getTestTables1();

        return testTables;
    }

    @GetMapping("/getTestTables2")
    public List<TestTable> getTestTables2()
    {
        List<TestTable> testTables = testTableService.getTestTables2();

        return testTables;
    }

    @GetMapping("/getTestTables3")
    public List<TestTable> getTestTables3()
    {
        List<TestTable> testTables = testTableService.getTestTables3();

        return testTables;
    }

    @GetMapping("/updateTestTable1")
    public String updateTestTable1()
    {
        testTableService.updateTestTable1();

        return "sucess";
    }

    @GetMapping("/updateTestTable2")
    public String updateTestTable2()
    {
        DBContextHolder.setDbType(DBContextHolder.DB_TYPE_SECONDARY);
        testTableService.updateTestTable2();

        return "sucess";
    }

    @GetMapping("/updateTestTable3")
    public String updateTestTable3()
    {
        testTableService.updateTestTable3();

        return "sucess";
    }
}
