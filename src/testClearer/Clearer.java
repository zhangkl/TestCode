package testClearer;

public class Clearer {
    public int[][] clearZero(int[][] mat, int n) {
        // write code here
        int[][] result = copeArray(mat);
        for(int i=0;i<mat.length;i++){
            for(int j=0;j<mat[i].length;j++){
                if (mat[i][j]==0){
                   for(int x=0;x<mat.length;x++){
                       result[x][j]=0;
                   }
                    for(int y=0;y<mat[i].length;y++){
                        result[i][y]=0;
                    }
                }
            }
        }
        printArray(mat);
        printArray(result);
        return result;
    }

    public static void main(String[] args) {
        int [][] temp = {{1,2,3},{0,1,2},{0,0,1}};
         new Clearer().clearZero(temp,3);
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