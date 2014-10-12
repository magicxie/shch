package net.sc.pd;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;

public final class LinearAccumulationCollector implements
		RealMatrixPreservingVisitor {
	double[] accumulatedArray;// 经过一次累加数组
	double sum = 0;

	public void start(int rows, int columns, int startRow, int endRow,
			int startColumn, int endColumn) {
		accumulatedArray = new double[columns];
		
	}

	public void visit(int row, int column, double value) {
		sum += value;
		accumulatedArray[column] = sum;
		
	}

	public double end() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double[] getAccumulatedArray(){
		return accumulatedArray;
	}
	
	public RealMatrix getMatrix(){
		return new Array2DRowRealMatrix(new double[][]{accumulatedArray});
	}
}