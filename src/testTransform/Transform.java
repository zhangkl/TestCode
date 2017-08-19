package testTransform;

public class Transform {
    public int[][] transformImage(int[][] mat, int n) {
        // write code| here test
        printArray(mat);    //打印数组
        int[][] result = copeArray(mat);   //拷贝数组
        for(int x = 0;x<n;x++){
            for(int y = 0;y<n;y++){
                result[x][y]=mat[n-1-y][x];  //行列互换
            }
        }
        printArray(result);
        return result;
    }

    public static void main(String[] args) {
        int[][] mat= {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
        new Transform().transformImage(mat,4);
    }

    public void printArray(int[][] mat){
        for(int i = 0;i<mat.length;i++){
            for(int y = 0;y<mat.length;y++){
                System.out.print(mat[i][y] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int[][] copeArray(int[][] mat){
        int[][] copeMat = new int[mat.length][mat.length];
        for(int i = 0;i<mat.length;i++){
            for(int y = 0;y<mat.length;y++){
                copeMat[i][y] = mat[i][y];
            }
        }
        return copeMat;
    }
}