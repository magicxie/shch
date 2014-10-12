package net.sc.pd; 

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Component;


/**
 * GM(1��1)
 * @author 
 *
 */
@Component
public class GreyModel {

	public RealVector build(double[] sample) {
		
		RealMatrix originalMatrix = new Array2DRowRealMatrix(new double[][]{sample});
		
		LinearAccumulationCollector accColllector = new LinearAccumulationCollector();
		originalMatrix.walkInRowOrder(accColllector);
		
		//һ���ۼ�
		RealMatrix accumulateMatrix = accColllector.getMatrix();
		
		AverageCollector avgColllector = new AverageCollector();
		accumulateMatrix.walkInRowOrder(avgColllector);
		
		//�ھ�ֵ��Ȩ
		RealMatrix averageMatrix = avgColllector.getMatrix();
		
		//���潨�� ����B��YN���������������� �������a,b
		
		//��������B, B��n-1��2�еľ��� �൱��һ����ά���顣
		RealMatrix matrixB = new Array2DRowRealMatrix(toVecB(averageMatrix.getRow(0), sample));
		
		//��������YN
		RealVector vectorYN = originalMatrix.getSubMatrix(0, 0, 1, originalMatrix.getColumnDimension()-1).getRowVector(0);
		
		//B��ת�þ���BT,2��n-1�еľ���
		RealMatrix matrixBT = matrixB.transpose();
		
		/*
		 * ��BT��B�ĳ˻����õ��Ľ����Ϊ����B2T,��B2T��һ��2*2�ľ���
		 */
		RealMatrix matrixB2T = matrixBT.multiply(matrixB);
		
		/* ������B2T���������ΪB_2T*/
		
		RealMatrix matrixB_2T = new LUDecomposition(matrixB2T).getSolver().getInverse();
		/*
		 * ������������ĸ���֪�����������������δ֪��a��b�����������������B_2T*BT*YN
		 * �������Ƿֱ�����Щ����ĳ˻���������B_2T*BT����B_2T*BT�ĳ˻�ΪA������A����һ��2*5�ľ���
		 */
		
		//��������A����
		RealMatrix aMaxtrix = matrixB_2T.multiply(matrixBT);
		
		//������A��YN����ĳ˻�����˻�ΪC������C����һ��2*1�ľ���
		return aMaxtrix.operate(vectorYN);
		
		
	}

	private double[][] toVecB(double[] averageArray, double[] sample) {
		double[][] B = new double[sample.length - 1][2];
		for (int i = 0; i < sample.length - 1; i++) {
			for (int j = 0; j < 2; j++) {
				if (j == 1)
					B[i][j] = 1;
				else
					B[i][j] = -averageArray[i];
			}

		}
		return B;
	}

	public double resolve(double[] sample, int i){
		
		RealVector vectorC = build(sample);
		i = i + sample.length-1;
		double a = vectorC.getEntry(0), b =vectorC.getEntry(1);
			
		return (sample[0] - b / a) * Math.exp(-a * (i + 1)) - (sample[0] - b / a)
					* Math.exp(-a * i);
			
	}
	
}
