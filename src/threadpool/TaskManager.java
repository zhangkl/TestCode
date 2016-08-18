/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package threadpool;


import java.util.LinkedList;

/**
 * Created by zxl on 2016/7/5.
 */
public class TaskManager {

    public static LinkedList<WorkTask> workqueue = new LinkedList<WorkTask>();// 缓冲队列

    /**
     * 向工作队列中加入一个任务，由工作线程去执行该任务
     *
     * @param
     */
    public synchronized static void addTask(WorkTask worktask) {
        if (worktask != null && workqueue.size() < 5000) {
            workqueue.add(worktask);
        }
    }

    /*[com.yulin.threadpool.WorkTaskImp@44f4ac30,
     com.yulin.threadpool.WorkTaskImp@44f4ad60,
     com.yulin.threadpool.WorkTaskImp@44f4ae00,
     com.yulin.threadpool.WorkTaskImp@44f4aea0,
     com.yulin.threadpool.WorkTaskImp@44f4af40]*/

    /**
     * 从工作队列中取出一个任务
     *
     * @return
     * @throws InterruptedException
     */
    public synchronized static WorkTask getTask() throws InterruptedException {
        while (workqueue.size() > 0) {
            return workqueue.removeFirst();
        }
        return null;
    }

}
