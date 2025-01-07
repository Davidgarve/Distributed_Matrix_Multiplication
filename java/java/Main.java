import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        try {
            double[][] matrixA = readMatrixFromFile("input.txt", 'A', 100, 100);
            double[][] matrixB = readMatrixFromFile("input.txt", 'B', 100, 100);

            double[][] result = MatrixMultiplicationService.distributedMultiply(hazelcastInstance, matrixA, matrixB);

            for (double[] row : result) {
                System.out.println(Arrays.toString(row));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hazelcastInstance.shutdown();
        }
    }

    public static double[][] readMatrixFromFile(String fileName, char matrixType, int rows, int cols) throws IOException {
        double[][] matrix = new double[rows][cols];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                char type = parts[0].charAt(0);
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                double value = Double.parseDouble(parts[3]);

                if (type == matrixType) {
                    matrix[row][col] = value;
                }
            }
        }
        return matrix;
    }
}
