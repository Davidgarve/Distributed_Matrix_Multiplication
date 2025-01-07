import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.concurrent.Callable;

public class MatrixMultiplicationTask implements Callable<double[]> {
    private final int row;
    private final int colsB;

    public MatrixMultiplicationTask(int row, int colsB) {
        this.row = row;
        this.colsB = colsB;
    }

    @Override
    public double[] call() {
        HazelcastInstance localInstance = Hazelcast.getAllHazelcastInstances().iterator().next();
        IMap<String, double[][]> sharedData = localInstance.getMap("matrices");

        double[][] matrixA = sharedData.get("matrixA");
        double[][] matrixB = sharedData.get("matrixB");

        double[] resultRow = new double[colsB];

        for (int col = 0; col < colsB; col++) {
            for (int k = 0; k < matrixA[0].length; k++) {
                resultRow[col] += matrixA[row][k] * matrixB[k][col];
            }
        }

        return resultRow;
    }
}