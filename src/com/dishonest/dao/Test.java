/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package com.dishonest.dao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 七月,2016
 */
public class Test implements Runnable {

    TestConn testConn = TestConn.getInstance();

    public static void main(String[] args) {
        /*ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 200; i++) {
            Test test = new Test();
            executorService.execute(test);
        }*/
        Test test = new Test();
        test.testRec();
    }

    public void testRec() {
        System.out.println(111);
        while (true) {
            testRec();
            System.out.println(222);
        }
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 1; i++) {
                List idrs = testConn.executeQueryForList("select * from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                System.out.print(idrs.size() + ":");
                if (idrs != null && idrs.size() > 0) {
                    testConn.executeSave("delete from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                }
                List rs = testConn.executeQueryForList("select count(*) num from v$process");
                Iterator it = rs.iterator();
                while (it.hasNext()) {
                    Map map = (Map) it.next();
                    System.out.println(Thread.currentThread().getName() + ":" + map.get("NUM"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*for (int i = 150; i < 1520; i++) {
            try {
                List idrs = testConn.executeQueryForList("select * from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                if (idrs != null && idrs.size() > 0) {
                    testConn.executeSave("delete from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                }

                List  list = new ArrayList();
                list.add(i);
                list.add(i);
                list.add(i);
                testConn.executeSave("update cred_dishonesty_log set dcurrentdate = sysdate where cardnum = 0101");
                testConn.psAdd("insert into CRED_DISHONESTY_PERSON (IID, SINAME, SCARDNUM) values (?, ?, ?)",list);

                idrs = testConn.executeQueryForList("select * from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                if (idrs != null && idrs.size() > 0) {
                    testConn.executeSave("delete from CRED_DISHONESTY_PERSON where iid = '" + i + "'");
                }

                List rs = testConn.executeQueryForList("select count(*) num from v$process");
                Iterator it = rs.iterator();
                while (it.hasNext()){
                    Map map = (Map) it.next();
                    System.out.println(Thread.currentThread().getName()+":"+map.get("NUM"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
}
