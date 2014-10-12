package net.sc.pd;

import java.util.Scanner;

public class GrayModelPredictor {

	private double[] model;
	
	public GrayModelPredictor(double[] model) {
		this.model = model;
	}
	
	public double[][] predict() {
		
		double[] arr = this.model;
		
		double[] accumulateArray = accumulateEachElementOf(arr);
		
		double[] averageArray = toAverageArray(accumulateArray);
		/*
		 * 
		 * 下面建立 向量B和YN求解待估参数向量， 即求参数a,b
		 */
		
		
		
		/*
		 * 下面建立向量B, B是5行2列的矩阵， 相当于一个二维数组。
		 */
		int arrayLength = arr.length - 1;
		
		double[][] B = new double[arrayLength][2];
		for (int i = 0; i < arrayLength; i++) {
			for (int j = 0; j < 2; j++) {
				if (j == 1)
					B[i][j] = 1;
				else
					B[i][j] = -averageArray[i];
				// System.out.println("B["+i+"]["+j+"]="+B[i][j]+";");
			}

		}
		/*
		 * 下面建立向量YN
		 */
		double[][] YN = new double[arrayLength][1];
		for (int i = 0; i < arrayLength; i++) {
			for (int j = 0; j < 1; j++) {
				YN[i][j] = arr[i + 1];
				// System.out.println("YN["+i+"]["+j+"]="+YN[i][j]+";");
			}
		}

		/*
		 * B的转置矩阵BT,2行5列的矩阵
		 */
		double[][] BT = new double[2][arrayLength];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < arrayLength; j++) {
				BT[i][j] = B[j][i];
				// System.out.println("BT["+i+"]["+j+"]="+BT[i][j]+";");
			}
		}
		/*
		 * 将BT和B的乘积所得到的结果记为数组B2T,则B2T是一个2*2的矩阵
		 */
		double[][] B2T = new double[2][2];
		for (int i = 0; i < 2; i++) {// rows of BT

			{
				for (int j = 0; j < 2; j++)// cloums of B
				{
					for (int k = 0; k < arrayLength; k++)// cloums of BT=rows of B
					{
						B2T[i][j] = B2T[i][j] + BT[i][k] * B[k][j];
					}
					// System.out.println("B2T["+i+"]["+j+"]="+B2T[i][j]+";");
				}

			}
		}
		/* 下面求B2T的逆矩阵，设为B_2T，怎么适用于一般的矩阵？ */
		double[][] B_2T = new double[2][2];
		B_2T[0][0] = (1 / (B2T[0][0] * B2T[1][1] - B2T[0][1] * B2T[1][0]))
				* B2T[1][1];
		B_2T[0][1] = (1 / (B2T[0][0] * B2T[1][1] - B2T[0][1] * B2T[1][0]))
				* (-B2T[0][1]);
		B_2T[1][0] = (1 / (B2T[0][0] * B2T[1][1] - B2T[0][1] * B2T[1][0]))
				* (-B2T[1][0]);
		B_2T[1][1] = (1 / (B2T[0][0] * B2T[1][1] - B2T[0][1] * B2T[1][0]))
				* B2T[0][0];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				// System.out.println("B_2T["+i+"]["+j+"]="+B_2T[i][j]+";");
			}
		}
		/*
		 * 根据以上所求的各已知量下面求待估参数的未知量a和b，待估向量矩阵等于B_2T*BT*YN
		 * 下面我们分别求这些矩阵的乘积，首先求B_2T*BT，令B_2T*BT的乘积为A矩阵，则A就是一个2*5的矩阵
		 */
		/*
		 * 
		 * 
		 * 
		 * 下面先求A矩阵
		 */
		double[][] A = new double[2][arrayLength];
		for (int i = 0; i < 2; i++) {// rows of B_2T
			{
				for (int j = 0; j < arrayLength; j++)// cloums of BT
				{
					for (int k = 0; k < 2; k++)// cloums of B_2T=rows of BT
					{
						A[i][j] = A[i][j] + B_2T[i][k] * BT[k][j];
					}
					// System.out.println("A["+i+"]["+j+"]="+A[i][j]+";");
				}

			}
		}
		/*
		 * 
		 * 
		 * 下面求A和YN矩阵的乘积，令乘积为C矩阵，则C就是一个2*1的矩阵
		 */
		double[][] C = new double[2][1];
		for (int i = 0; i < 2; i++) {// rows of A

			{
				for (int j = 0; j < 1; j++)// cloums of YN
				{
					for (int k = 0; k < arrayLength; k++)// cloums of A=rows of YN
					{
						C[i][j] = C[i][j] + A[i][k] * YN[k][j];
					}
					// System.out.println("C["+i+"]["+j+"]="+C[i][j]+";");
				}

			}
		}
		return C;
	}

	/**
	 * 紧邻均值数组
	 * @param array
	 * @return
	 */
	public double[] toAverageArray(double[] array) {
		
		int lengthOfAvgArray = array.length - 1;
		
		double[] avgArray = new double[lengthOfAvgArray];// arr1的紧邻均值数组
		for (int i = 0; i < lengthOfAvgArray; i++) {
			avgArray[i] = (double) (array[i] + array[i + 1]) / 2;
		}
		return avgArray;
	}

	/**
	 * 经过一次累加数组
	 * @param array
	 * @return
	 */
	public double[] accumulateEachElementOf(double[] array) {
		int lengthOfArray = array.length;
		
		double[] accumulatedArray = new double[lengthOfArray];// 经过一次累加数组
		double sum = 0;
		for (int i = 0; i < lengthOfArray; i++) {
			sum += array[i];
			accumulatedArray[i] = sum;
		}
		
		return accumulatedArray;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 预测模型函数
		double[] model = { 89332,96800,107453 };// 原始数组
		
		GrayModelPredictor corePrediction = new GrayModelPredictor(model);
		double[][] C = corePrediction.predict();
		/* 根据以上所得则a=C[0][0],b=C[1][0]; */
		double a = C[0][0], b = C[1][0];
		// System.out.println("a="+a+";"+"b="+b+";");
		// System.out.println("b/a="+b/a);
		/*
	      * 
	      * 
	      *
	      */

		Scanner x = new Scanner(System.in);// 构造一个Scanner对象，其传入参数为System.in
		System.out.print("请输入一个整数");
		
			int i = x.nextInt();// 读取一个数值
			
			double Y = (model[0] - b / a) * Math.exp(-a * (i + 1)) - (model[0] - b / a)
					* Math.exp(-a * i);
			System.out.println("预测结果为：Y=" + Y);
		
		x.close();

	}
}
