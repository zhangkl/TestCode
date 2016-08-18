/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package threadpool;

/**
 * Created by zxl on 2016/7/5.
 */
public final class TaskMonitorThread extends Thread {
    //private PoolManage threadPool;
    private int GetWorkTaskPollTime = 10;// 监测任务轮询时间，可配置

    /*public TaskMonitorThread(PoolManage pool) {
        System.out.println("正在创建任务监测线程...");
        this.threadPool = pool;
    }*/
    public TaskMonitorThread() {
        System.out.println("正在创建任务监测线程...");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {

            try {
                WorkTask task = TaskManager.getTask();
                if (task == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    Worker t = PoolManager.getInstance().getIdleThread();// 获取空闲线程
                    System.out.println("Worker.toString()=============================>?" + t.toString());
                    if (t == null)
                        break;
                    t.setWorkTask(task);// 设置线程任务
                    System.out.println("task.toString()=============================>?" + task.toString());
                    t.setIsRunning(true);//激活空闲线程
                    System.out.println("Worker.toString()=============================>?" + t.getIsrunning());
                    // try {
                    //Thread.sleep(GetWorkTaskPollTime);
                    //
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
