import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MatrixMultiplicationService {

    public static double[][] distributedMultiply(HazelcastInstance hazelcastInstance, double[][] matrixA, double[][] matrixB)
            throws InterruptedException, ExecutionException {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;

        if (colsA != matrixB.length) {
            throw new IllegalArgumentException("Number of columns in A must match number of rows in B.");
        }

        double[][] result = new double[rowsA][colsB];

        IMap<String, double[][]> sharedData = hazelcastInstance.getMap("matrices");
        sharedData.put("matrixA", matrixA);
        sharedData.put("matrixB", matrixB);

        IExecutorService executorService = hazelcastInstance.getExecutorService("executor");

        List<Future<double[]>> futures = new ArrayList<>();

        for (int row = 0; row < rowsA; row++) {
            int finalRow = row;
            futures.add(executorService.submit(new MatrixMultiplicationTask(finalRow, colsB)));
        }

        for (int row = 0; row < rowsA; row++) {
            result[row] = futures.get(row).get();
        }

        return result;
    }
}
