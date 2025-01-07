from mrjob.job import MRJob
from itertools import combinations

class MRFrequentItemsets(MRJob):

    def configure_args(self):
        super(MRFrequentItemsets, self).configure_args()
        self.add_passthru_arg('--min-support', type=int, default=2, help="Minimum support count")

    def mapper(self, _, line):
        transaction = line.strip().split(',')
        items = sorted(transaction)
        min_support = self.options.min_support
        
        for itemset_size in range(1, len(items) + 1):
            for itemset in combinations(items, itemset_size):
                yield (itemset, 1)
    
    def reducer(self, key, values):
        total_count = sum(values)
        min_support = self.options.min_support
        
        if total_count >= min_support:
            yield (key, total_count)

if __name__ == '__main__':
    MRFrequentItemsets.run()
    #Para ejecutar el programa -> python frequentItemSet.py transactions.txt --min-support 3
