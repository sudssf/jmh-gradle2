package test;

import java.util.Arrays;

public class Sorter {

    /**
     * Bucket sort.
     *
     * @param data
     *            Data array.
     * @return Sorted array.
     */
    public static double[] bucketSort(double[] data) {
        int numberOfBuckets = data.length;
        double[][] buckets = new double[numberOfBuckets][];
        int[] bucketListIndices = new int[numberOfBuckets];
        for (int i = 0; i < data.length; i++) {
            int bucketIndex = (int) ((double) numberOfBuckets * data[i]);
            if (buckets[bucketIndex] == null) {
                buckets[bucketIndex] = new double[1];
            } else {
                // increase bucket size by 1 (maybe better redo with linked list)
                double[] bucket = buckets[bucketIndex];
                buckets[bucketIndex] = Arrays.copyOf(bucket, bucket.length + 1);
            }
            buckets[bucketIndex][bucketListIndices[i]++] = data[i];
        }

        double[] sortedData = new double[data.length];
        int nextIndex = 0;
        // sort all buckets with insertion sort and merge them into result.
        for (int bucketIndex = 0; bucketIndex < numberOfBuckets; bucketIndex++) {
            if (buckets[bucketIndex] != null) {
                int bucketLength = buckets[bucketIndex].length;
                System.arraycopy(sortedData, nextIndex, Sorter.insertionSort(buckets[bucketIndex]), 0, bucketLength);
                nextIndex = nextIndex + bucketLength - 1;
            }
        }

        return sortedData;
    }

    /**
     * Radix sort.
     *
     * @param data
     *            Data array.
     * @param numberOfDigits
     *            Number of digits.
     * @return Sorted array.
     */
    public static int[] radixSort(int[] data, int numberOfDigits) {
        int[] sortedData = data;
        for (int i = 0; i < numberOfDigits; i++) {
            sortedData = Sorter.countingSortByDigit(sortedData, i, 9);
        }
        return sortedData;
    }

    private static int[] countingSortByDigit(int[] data, int digitIndex, int upperBound) {
        int dataLenght = data.length;
        int tempArrayLength = upperBound + 1;
        int[] tempArr = new int[tempArrayLength];
        // count how much of each element (index - element, value - count)
        for (int i = 0; i < dataLenght; i++) {
            int digitFromNumber = MathUtils.getDigitFromNumber(data[i], digitIndex);
            tempArr[digitFromNumber]++;
        }
        // count how much of less or equal elements exists
        for (int i = 1; i < tempArrayLength; i++) {
            tempArr[i] += tempArr[i - 1];
        }

        int[] sortedData = new int[dataLenght];
        // put each element to its place in sorted array
        for (int i = dataLenght - 1; i >= 0; i--) {
            int digitFromNumber = MathUtils.getDigitFromNumber(data[i], digitIndex);
            int place = tempArr[digitFromNumber] - 1; // element place in result
            // array
            sortedData[place] = digitFromNumber;
            tempArr[digitFromNumber]--; // decrease count less or equal elements
            // count
        }

        return sortedData;
    }

    /**
     * Counting sort.
     *
     * @param data
     *            Data array.
     * @param upperBound
     *            Upper bound of possible values.
     * @return Sorted array.
     */
    public static int[] countingSort(int[] data, int upperBound) {
        int dataLenght = data.length;
        int tempArrayLength = upperBound + 1;
        int[] tempArr = new int[tempArrayLength];
        // count how much of each element (index - element, value - count)
        for (int i = 0; i < dataLenght; i++) {
            tempArr[data[i]]++;
        }
        // count how much of less or equal elements exists
        for (int i = 1; i < tempArrayLength; i++) {
            tempArr[i] += tempArr[i - 1];
        }

        int[] sortedData = new int[dataLenght];
        // put each element to its place in sorted array
        for (int i = dataLenght - 1; i >= 0; i--) {
            int place = tempArr[data[i]] - 1; // element place in result array
            sortedData[place] = data[i];
            tempArr[data[i]]--; // decrease count less or equal elements count
        }

        return sortedData;
    }

    /**
     * Quicksort method for full array.
     *
     * @param data
     *            Data array.
     * @return Sorted array.
     */
    public static int[] quickSort(int[] data) {
        Sorter.quickSort(data, 0, data.length - 1);
        return data;
    }

    /**
     * Quicksort recursion.
     *
     * @param data
     *            Data array.
     * @param sublistFirstIndex
     *            Index of first element of sublist.
     * @param sublistLastIndex
     *            Index of first element of sublist.
     */
    private static void quickSort(int[] data, int sublistFirstIndex, int sublistLastIndex) {
        if (sublistFirstIndex < sublistLastIndex) {
            // move smaller elements before pivot and larger after
            int pivotIndex = partition(data, sublistFirstIndex, sublistLastIndex);
            // apply recursively to sub lists
            Sorter.quickSort(data, sublistFirstIndex, pivotIndex - 1);
            Sorter.quickSort(data, pivotIndex + 1, sublistLastIndex);
        }
    }

    /**
     * Pick pivot element and partition list into two sub lists - lower than
     * pivot before and higher after the pivot.
     *
     * @param data
     *            Data array.
     * @param sublistFirstIndex
     *            Index of first element of sublist.
     * @param sublistLastIndex
     *            Index of first element of sublist.
     * @return Index of pivot element.
     */
    private static int partition(int[] data, int sublistFirstIndex, int sublistLastIndex) {
        int pivotElement = data[sublistLastIndex];
        int pivotIndex = sublistFirstIndex - 1;
        for (int i = sublistFirstIndex; i < sublistLastIndex; i++) {
            if (data[i] <= pivotElement) {
                pivotIndex++;
                ArrayUtils.swap(data, pivotIndex, i);
            }
        }
        ArrayUtils.swap(data, pivotIndex + 1, sublistLastIndex);
        return pivotIndex + 1; // return index of pivot element
    }

    /**
     * Heap sort.
     *
     * @param data
     *            Full data array.
     * @return Sorted array.
     */
    public static int[] heapSort(int[] data) {
        // build heap
        HeapUtils.buildMaxHeap(data);
        int heapSize = data.length;
        for (int i = data.length - 1; i > 0; i--) {
            // move largest element to the end
            ArrayUtils.swap(data, 0, i);
            // rebuild heap with smaller size by 1 (last element already sorted)
            HeapUtils.maxHeapify(data, 0, --heapSize);
        }
        return data;
    }

    /**
     * Insertion sort.
     *
     * @param data
     *            Full data array.
     * @return Sorted array.
     */
    public static int[] insertionSort(int[] data) {
        // run from second element to last
        for (int i = 1; i < data.length; i++) {
            int key = data[i];
            int j = i - 1;
            // take element and run back until smaller is found
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j]; // move elements to make place for insert
                j--;
            }
            // insert element in position after a smaller element
            data[j + 1] = key;
        }
        return data;
    }

    /**
     * Insertion sort for double primitives.
     *
     * @param data
     *            Data array.
     * @return Sorted array.
     */
    public static double[] insertionSort(double[] data) {
        // run from second element to last
        for (int i = 1; i < data.length; i++) {
            double key = data[i];
            int j = i - 1;
            // take element and run back until smaller is found
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }
            // insert element in position after a smaller element
            data[j + 1] = key;
        }
        return data;
    }

    /**
     * Merge sort.
     *
     * @param data
     *            Full data array.
     * @return Sorted array.
     */
    public static int[] mergeSort(int[] data) {
        mergeSort(data, 0, data.length - 1);
        return data;
    }

    /**
     * Merge sort recursion.
     *
     * @param data
     *            Full data array.
     * @param firstSublistStartIndex
     *            Index of first sorted sub array first element.
     * @param secondSublistEndIndex
     *            Index of second sorted sub array last element.
     */
    private static void mergeSort(int[] data, int firstSublistStartIndex, int secondSublistEndIndex) {
        if (firstSublistStartIndex < secondSublistEndIndex) {
            int firstSublistLastElementIndex = (firstSublistStartIndex + secondSublistEndIndex) / 2;
            // recursively reach sub lists that has only one element
            Sorter.mergeSort(data, firstSublistStartIndex, firstSublistLastElementIndex);
            Sorter.mergeSort(data, firstSublistLastElementIndex + 1, secondSublistEndIndex);
            // then start merging them
            Sorter.merge(data, firstSublistStartIndex, firstSublistLastElementIndex, secondSublistEndIndex);
        }
    }

    /**
     * Merge two sorted arrays into one sorted array. Method assumes that p <= q
     * < r and data[p..q] and data[q+1..r] is sorted. After method is completed
     * data[p..q] forms single sorted sub array.
     *
     * @param data
     *            Full data array.
     * @param firstSublistStartIndex
     *            Index of first sorted sub array first element.
     * @param firstSublistLastElementIndex
     *            Index of first sorted sub array last element. q+1 will be
     *            index of second sorted sub array first element.
     * @param secondSublistEndIndex
     *            Index of second sorted sub array last element.
     */
    private static void merge(int[] data, int firstSublistStartIndex, int firstSublistLastElementIndex,
                              int secondSublistEndIndex) {
        int firstArrayLenght = firstSublistLastElementIndex - firstSublistStartIndex + 1;
        int secondArrayLenght = secondSublistEndIndex - firstSublistLastElementIndex;
        int[] firstArray = new int[firstArrayLenght + 1];
        int[] secondArray = new int[secondArrayLenght + 1];
        // copy first sub array to separate array
        for (int i = 0; i < firstArrayLenght; i++) {
            firstArray[i] = data[firstSublistStartIndex + i];
        }
        // copy second sub array to separate array
        for (int i = 0; i < secondArrayLenght; i++) {
            secondArray[i] = data[firstSublistLastElementIndex + 1 + i];
        }
        // optimization. because last element is maximum, we don't need to check
        // if current index is not out of bounds
        firstArray[firstArrayLenght] = Integer.MAX_VALUE;
        secondArray[secondArrayLenght] = Integer.MAX_VALUE;

        int firstCurrentIndex = 0;
        int secondCurrentIndex = 0;

        // merge firstArray and second array to data[p..r]
        for (int i = firstSublistStartIndex; i <= secondSublistEndIndex; i++) {
            // compare current values from sub arrays (both values are smallest
            // in each array) and move smaller one to data[p..r]
            if (firstArray[firstCurrentIndex] <= secondArray[secondCurrentIndex]) {
                data[i] = firstArray[firstCurrentIndex];
                firstCurrentIndex++;
            } else {
                data[i] = secondArray[secondCurrentIndex];
                secondCurrentIndex++;
            }
        }
    }

}


class MathUtils {

    /**
     * Extracts digit from integer number.
     *
     * @param number
     *            Number from which digit is extracted.
     * @param digitIndex
     *            index of digit to get. 0 - last one.
     *
     * @return Required digit.
     */
    public static int getDigitFromNumber(int number, int digitIndex) {
        // TODO error check and efficiency (maybe put powers of 10 to array)
        return number / (int) Math.pow(10, digitIndex) % 10;
    }

    /**
     * Returns bigger integer.
     *
     * @param first
     *            First number.
     * @param second
     *            Second number.
     * @return Bigger number.
     */
    public static int getMax(int first, int second) {
        return first > second ? first : second;
    }

    /**
     * Calculates logarithm.
     */
    public static double logarithm(double base, double a) {
        return Math.log(a) / Math.log(base);
    }

}


/**
 * Utils for working with heap data structure. Heap is a tree like data
 * structure, which has root element always bigger than its children.
 * Example of a heap:
 *              100
 *               /\
 *             90  23
 *             /\  /
 *           45  1 15
 * Heap is used in heap sort and priority queues implementations.
 */
class HeapUtils {

    /**
     * Build heap data structure in place of array.
     *
     * @param data
     *            Data array.
     */
    public static void buildMaxHeap(int[] data) {
        for (int i = data.length / 2; i >= 0; i--) {
            maxHeapify(data, i);
        }
    }

    /**
     * This method takes array and index of root element (of heap subtree). It
     * assumes that this element might not
     *
     * @param data
     *            Data array.
     * @param index
     *            Index of root element.
     */
    public static void maxHeapify(int[] data, int index) {
        maxHeapify(data, index, data.length);
    }

    /**
     * Make a heap out of sublist of array.
     *
     * @param data
     *            Data array.
     * @param index
     *            Index of root element.
     * @param heapSize
     *            Heap size.
     */
    public static void maxHeapify(int[] data, int index, int heapSize) {
        int leftLeaf = getLeftLeaf(index);
        int rightLeaf = getRightLeaf(index);
        int largest = index;
        if (leftLeaf < heapSize && data[leftLeaf] > data[largest]) {
            largest = leftLeaf;
        }
        if (rightLeaf < heapSize && data[rightLeaf] > data[largest]) {
            largest = rightLeaf;
        }
        if (largest != index) {
            ArrayUtils.swap(data, index, largest);
            HeapUtils.maxHeapify(data, largest, heapSize);
        }
    }

    /**
     * Get parent of an element.
     *
     * @param leafIndex
     *            Element index.
     * @return Elements parent index if array is heapified.
     */
    public static int getParent(int leafIndex) {
        return leafIndex / 2;
    }

    /**
     * Calculates index of left child node.
     *
     * @param parentIndex
     *            Array index that represents parent node.
     * @return Left child node index in array.
     */
    private static int getLeftLeaf(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    /**
     * Calculates index of right child node.
     *
     * @param parentIndex
     *            Array index that represents parent node.
     * @return Right child node index in array.
     */
    private static int getRightLeaf(int parentIndex) {
        return 2 * parentIndex + 2;
    }
}

class ArrayUtils {

    /**
     * Swaps elements in array.
     *
     * @param data
     *            Array of elements.
     * @param elementIndex1
     *            Index of element which value will be moved to elementIndex2.
     * @param elementIndex2
     *            Index of element which value will be moved to elementIndex1.
     */
    public static void swap(int[] data, int elementIndex1, int elementIndex2) {
        int tmp = data[elementIndex1];
        data[elementIndex1] = data[elementIndex2];
        data[elementIndex2] = tmp;
    }

    /**
     * Select nth smallest element in array. It could be easily solved by
     * sorting array and select nth element, but this algorithm is more
     * efficient. Expected running time is O(n). However worst case scenario
     * could be O(n^2).
     *
     * @param data
     *            Data array.
     * @param n
     *            N parameter.
     * @return Value of nth smallest element.
     */
    public static int selectNthSmallestElement(int[] data, int n) {
        return selectNthSmallestElement(data, 0, data.length - 1, n);
    }

    /**
     * Select nth smallest element in array from sublist.
     *
     * @param @param data Data array.
     * @param sublistStartIndex
     *            Sublist start index.
     * @param sublistEndIndex
     *            Sublist end index.
     * @param n
     *            N parameter.
     * @return Value of nth smallest element in sublist.
     */
    public static int selectNthSmallestElement(int[] data, int sublistStartIndex, int sublistEndIndex, int n) {
        if (sublistStartIndex == sublistEndIndex) {
            return data[sublistStartIndex];
        }
        int pivotIndex = partition(data, sublistStartIndex, sublistEndIndex);
        int k = sublistStartIndex - pivotIndex + 1;
        if (n == k) {
            return data[pivotIndex];
        } else if (n < k) {
            return selectNthSmallestElement(data, sublistStartIndex, pivotIndex - 1, n);
        } else {
            return selectNthSmallestElement(data, pivotIndex, sublistEndIndex, n - k);
        }

    }

    /**
     * Partition algorithm.
     *
     * @param @param data Data array.
     * @param sublistStartIndex
     *            Sublist start index.
     * @param sublistEndIndex
     *            Sublist end index.
     * @return Pivot index.
     */
    private static int partition(int[] data, int sublistStartIndex, int sublistEndIndex) {
        int pivotElement = data[sublistEndIndex];
        int pivotIndex = sublistStartIndex - 1;
        for (int i = sublistStartIndex; i < sublistEndIndex; i++) {
            if (data[i] <= pivotElement) {
                pivotIndex++;
                ArrayUtils.swap(data, pivotIndex, i);
            }
        }
        ArrayUtils.swap(data, pivotIndex + 1, sublistEndIndex);
        return pivotIndex + 1;
    }

}
