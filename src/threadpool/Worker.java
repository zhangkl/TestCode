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
public class Worker extends Thread {
    public boolean isrunning = false;
    private WorkTask nowTask; // 当前任务
    private Object threadTag;// 线程标识

    public Worker(Object key) {
        System.out.println("正在创建工作线程...线程编号" + key.toString());
        this.threadTag = key;
        // this.state=CREATESTATE;
    }

    //获取线程标识key
    public Object getThreadTag() {
        return threadTag;
    }

    public synchronized void setWorkTask(WorkTask task) {
        this.nowTask = task;
    }

    public synchronized void setIsRunning(boolean flag) {
        this.isrunning = flag;
        if (flag) {
            this.notify();
        }
    }

    public synchronized void setIsWorkTaskRunning(WorkTask task, boolean flag) {
        this.nowTask = task;
        this.isrunning = flag;
        if (flag) {
            this.notify();
        }
    }

    public boolean getIsrunning() {
        return isrunning;
    }

    public synchronized void run() {
        System.out.println("工作线程" + this.getThreadTag() + "初始化成功");
        while (true) {
            if (!isrunning) {
                try {
                    System.out.println("工人" + this.getThreadTag() + "任务完成回归线程池");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println("线程被阻挡");
                    e.printStackTrace();
                }
            } else {
                //try {
                nowTask.runTask();
                setIsRunning(false);
                System.out.println("工人" + this.getThreadTag() + "开始工作");
                //this.sleep(3000);
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}

                //this.notify();
                //break;
            }
        }
    }
}
