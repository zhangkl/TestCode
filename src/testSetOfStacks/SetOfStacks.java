package testSetOfStacks;


import java.util.ArrayList;

public class SetOfStacks {
    public ArrayList<ArrayList<Integer>> setOfStacks(int[][] ope, int size) {
        // write code here
        ArrayList<ArrayList<Integer>> list = new ArrayList();
        ArrayList array1 = new ArrayList();
        for (int j = 0; j < ope.length; j++) {
            if (ope[j][0] == 1) {
                array1.add(ope[j][1]);
            }
            else if(ope[j][0] == 2){
                if(array1.size()==0){
                    array1 = (ArrayList) list.get(list.size()-1).clone();
                    list.remove(list.size()-1);
                }
                array1.remove(array1.size()-1);
                if(list.size()>0 && list.get(list.size()-1).size()==0){
                    list.remove(list.size()-1);
                }
            }
            if (array1.size()==size||j==ope.length-1) {
                list.add((ArrayList) array1.clone());
                array1.clear();
            }
        }
        return list;
    }

    public ArrayList<ArrayList<Integer>> setOfStacks2(int[][] ope, int size){
        ArrayList<ArrayList<Integer>> stackList = new ArrayList<ArrayList<Integer>>();
        if(ope==null||ope.length<1)
            return stackList;
        //定义操作的次数
        int opeCount = ope.length;
        //定义第一个栈
        ArrayList<Integer> curStack = new ArrayList<Integer>();
        //因为操作数大于1，所以先把第一个栈加入集合中
        stackList.add(curStack);
        for(int i=0;i<opeCount;i++)
        {
            if(ope[i][0]==1)          //push操作
            {
                if(curStack.size()==size)     //如果当前栈的size已满
                {
                    //新建一个栈，curStack指向新的栈
                    curStack = new ArrayList<Integer>();
                    //将该栈加入集合栈
                    stackList.add(curStack);
                }
                curStack.add(ope[i][1]);               //数据入当前栈

            }
            if(ope[i][0]==2)            //pop操作
            {
                //如果当前栈的大小大于0
                if(curStack.size()>0)
                    //则直接删除，当前栈的最后一个元素
                    curStack.remove(curStack.size()-1);
                else
                //否则当前栈的size等于0说明栈集合中最后一个栈被pop空了
                {
                    //把集合中最后一个空栈先删除掉
                    stackList.remove(stackList.size()-1);
                    //curStack指向集合中最后一个栈作为当前栈
                    curStack = stackList.get(stackList.size()-1);
                    //当前栈执行pop操作，弹出最后一个元素
                    curStack.remove(curStack.size()-1);
                }
            }
        }

        return stackList;
    }

    public static void main(String[] args) {
        int[][] ope = {{1,39662},{1,43501},{1,38324},{1,27399},{1,13114},{2,61367},{1,16064},{1,79832},{1,52594},{1,62229},{1,32555},{1,45533},{1,94718},{1,58728},{1,22047},{1,2383},{1,56550},{1,31998},{1,64619},{1,68826},{1,90240},{1,59426},{1,90580},{1,64032},{1,36832},{2,47517},{1,82699},{1,81321},{1,6070},{1,64566}};
        System.out.println(new SetOfStacks().setOfStacks2(ope, 7));
    }
}