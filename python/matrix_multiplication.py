from mrjob.job import MRJob
from collections import defaultdict

from mrjob.step import MRStep


class MatrixMultiplication(MRJob):

    def configure_args(self):
        super(MatrixMultiplication, self).configure_args()
        self.add_passthru_arg('--num_rows_a', type=int, help="Número de filas en la matriz A")
        self.add_passthru_arg('--num_cols_b', type=int, help="Número de columnas en la matriz B")

    def mapper(self, _, line):
        matrix, i, j, value = line.split()
        i, j, value = int(i), int(j), int(value)

        if matrix == "A":
            for k in range(self.options.num_cols_b): 
                yield (i, k), ("A", j, value)
        elif matrix == "B":
            for k in range(self.options.num_rows_a): 
                yield (k, j), ("B", i, value)

    def reducer(self, key, values):
        a_values = defaultdict(int)
        b_values = defaultdict(int)

        for matrix, index, value in values:
            if matrix == "A":
                a_values[index] = value
            elif matrix == "B":
                b_values[index] = value

        result = sum(a_values[k] * b_values[k] for k in a_values if k in b_values)
        yield key, result

    def steps(self):
        return [
            MRStep(mapper=self.mapper, reducer=self.reducer)
        ]


if __name__ == '__main__':
    with open("input.txt", "w") as f:
        for i in range(100):
            for j in range(100):
                value = i + j + 1
                f.write(f"A {i} {j} {value}\n")

        for i in range(100):
            for j in range(100):
                value = i - j + 1
                f.write(f"B {i} {j} {value}\n")

    MatrixMultiplication.run()
    # Para ejecutar el programa -> python matrix_multiplication.py input.txt --num_rows_a 100 --num_cols_b 100
