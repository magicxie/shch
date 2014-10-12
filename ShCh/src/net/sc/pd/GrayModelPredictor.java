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
		 * ���潨�� ����B��YN���������������� �������a,b
		 */
		
		
		
		/*
		 * ���潨������B, B��5��2�еľ��� �൱��һ����ά���顣
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
		 * ���潨������YN
		 */
		double[][] YN = new double[arrayLength][1];
		for (int i = 0; i < arrayLength; i++) {
			for (int j = 0; j < 1; j++) {
				YN[i][j] = arr[i + 1];
				// System.out.println("YN["+i+"]["+j+"]="+YN[i][j]+";");
			}
		}

		/*
		 * B��ת�þ���BT,2��5�еľ���
		 */
		double[][] BT = new double[2][arrayLength];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < arrayLength; j++) {
				BT[i][j] = B[j][i];
				// System.out.println("BT["+i+"]["+j+"]="+BT[i][j]+";");
			}
		}
		/*
		 * ��BT��B�ĳ˻����õ��Ľ����Ϊ����B2T,��B2T��һ��2*2�ľ���
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
		/* ������B2T���������ΪB_2T����ô������һ��ľ��� */
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
		 * ������������ĸ���֪�����������������δ֪��a��b�����������������B_2T*BT*YN
		 * �������Ƿֱ�����Щ����ĳ˻���������B_2T*BT����B_2T*BT�ĳ˻�ΪA������A����һ��2*5�ľ���
		 */
		/*
		 * 
		 * 
		 * 
		 * ��������A����
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
		 * ������A��YN����ĳ˻�����˻�ΪC������C����һ��2*1�ľ���
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
	 * ���ھ�ֵ����
	 * @param array
	 * @return
	 */
	public double[] toAverageArray(double[] array) {
		
		int lengthOfAvgArray = array.length - 1;
		
		double[] avgArray = new double[lengthOfAvgArray];// arr1�Ľ��ھ�ֵ����
		for (int i = 0; i < lengthOfAvgArray; i++) {
			avgArray[i] = (double) (array[i] + array[i + 1]) / 2;
		}
		return avgArray;
	}

	/**
	 * ����һ���ۼ�����
	 * @param array
	 * @return
	 */
	public double[] accumulateEachElementOf(double[] array) {
		int lengthOfArray = array.length;
		
		double[] accumulatedArray = new double[lengthOfArray];// ����һ���ۼ�����
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

		// Ԥ��ģ�ͺ���
		double[] model = { 89332,96800,107453 };// ԭʼ����
		
		GrayModelPredictor corePrediction = new GrayModelPredictor(model);
		double[][] C = corePrediction.predict();
		/* ��������������a=C[0][0],b=C[1][0]; */
		double a = C[0][0], b = C[1][0];
		// System.out.println("a="+a+";"+"b="+b+";");
		// System.out.println("b/a="+b/a);
		/*
	      * 
	      * 
	      *
	      */

		Scanner x = new Scanner(System.in);// ����һ��Scanner�����䴫�����ΪSystem.in
		System.out.print("������һ������");
		
			int i = x.nextInt();// ��ȡһ����ֵ
			
			double Y = (model[0] - b / a) * Math.exp(-a * (i + 1)) - (model[0] - b / a)
					* Math.exp(-a * i);
			System.out.println("Ԥ����Ϊ��Y=" + Y);
		
		x.close();

	}
}
