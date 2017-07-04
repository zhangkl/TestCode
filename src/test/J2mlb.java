package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.mathworks.toolbox.javabuilder.*; 
import J2mlb.*; 

public class J2mlb {

	public static void main(String[] args) throws IOException, MWException {


		System.out.println(System.getProperty("java.library.path"));
		Class1 ipf = new Class1() ;
		double nstocks = 20;
		/*基于遗传基因算法的股票投资组合
		 * name:股票名称
		 * currprice：现价
		 * toSpend：投资总金额
		 * minSingle单只股票投资下限金额（手）
		 * maxSingle单只股票投资上限金额（手）
		 * callbacknum：返回参数数量，默认为1
		 * 返回二维数组每行第一列为委托手数
		 * **/
		String[] names = {"600060.SH","600011.SH","600012.SH","600247.SH","600063.SH","600015.SH","600016.SH","600017.SH","600018.SH","600019.SH","600021.SH","600022.SH","600023.SH","601155.SH","601198.SH","600340.SH","600027.SH","600028.SH","600029.SH","600030.SH"};
		double[][] currprice={{15.0300,},{7.7500,},{13.2700,},{10.0600,},{4.0900,},{11.0200,},{8.1100,},{4.3400,},{6.2900,},{6.4600,},{12.3300,},{2.0700,},{5.4800,},{17.8600,},{17.1800,},{34.0500,},{4.8700,},{6.0100,},{8.8600,},{16.9400}};
		double toSpend = 9000;
		double minSingle = 0;
		double maxSingle = 0.2;
		int callbacknum = 1;
		
		Object[] result = new Object[1];
		result = ipf.intportfolio(callbacknum,nstocks,names, currprice,toSpend,minSingle,maxSingle);	
		System.out.println(result[0]);

	}

}
