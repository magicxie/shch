package net.sc.pd;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;

public final class AverageCollector implements
		RealMatrixPreservingVisitor {
	
	double[] averageArray;
	double weight = 0.5;
	double prevValue = 0;
	
	public AverageCollector() {
	}
	
	public AverageCollector(double weight) {
		this.weight = weight;
	}
	
	public void start(int rows, int columns, int startRow, int endRow,
			int startColumn, int endColumn) {
		averageArray = new double[columns - 1];
		
	}

	public void visit(int row, int column, double value) {
		
		if(column > 0){
			averageArray[column - 1] = (value + prevValue) * weight;
		}
		
		prevValue = value;
		
	}

	public double end() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double[] getAccumulatedArray(){
		return averageArray;
	}
	
	public RealMatrix getMatrix(){
		return new Array2DRowRealMatrix(new double[][]{averageArray});
	}
}