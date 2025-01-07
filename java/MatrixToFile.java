import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixToFile {
    public static void main(String[] args) {
        String fileName = "input.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int value = i + j + 1;
                    writer.write("A " + i + " " + j + " " + value);
                    writer.newLine();
                }
            }

            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int value = i - j + 1;
                    writer.write("B " + i + " " + j + " " + value);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Matrices guardadas en " + fileName);
    }
}
