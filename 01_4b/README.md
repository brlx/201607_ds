# Solution for the 4 billion challenge

The code snipptes below are pseudocode with java syntax highlight, ie. I tried to write valid java code but did not validate it any way.

In the challange, it was not specified whether the algorithm can save a state between runs on the same dataset, or whether the request itself should modify the dataset, so I tried to evaluate the algorithms for these possibilities.

## 1 The obvious but really slow solution

In this solution, you simply iterate over all of the possible integer values, and read through the whole file line by line to check whether the current integer is in the file. We return with the first number which is not found in the file. This does require only a little memory and CPU but creates a lot of disk operations `(O(n^2)` where n is the number of integers present in the file), since the file is not parsed into any kind of data structure, it is read through multiple times instead.

```java
	for (int i = 0; i < Integer.MAX_SIZE; i++) {
		boolean found = false;
		for (String line : file) {
			if (parseLine(line) == i) {
				found = true;
				break;
			}
		}
		if (!found) {
			System.out.println("The smallest number not present in the data is " + i);
			exit 0;
		} else {
			System.out.println("Every integer is represented in the data.");
			exit 1;
		}
	}
```

## 2 Solutions optimized for low memory usage

Really low memory usage is possible if we don't read the file into the memory at all.

If we don't have any kind of persistent state between runs, then the solution above is the only possible one.

If we can rewrite the file, we could do it in a way which speeds up the search, and/or makes it possible to persist the numbers which we returned since the first search.

### 2.1 Rewriting the file with the numbers ordered

If we rewrite the file with the numbers ordered, we can search by comparing each number with the next so we only have to walk through the file for each searched number. This way the individual searches' disk IO decreases to `O(n)`.

The first drawback of this method is that we either sort in memory which will require `O(n)` memory during the sorting and then `n` writes to the disk, or we do the sorting on the disk, which will require a lot of disk operations (depends on the sorting algorithm).

The second drawback is that if we want mark the searched number as used by writing it back to the file, it requires rewriting at least a portion of the file each time (`O(n) disk writes`).

### 2.2 Mapping the whole domain of numbers to bits

Since we know that the domain of the numbers is every 32 bit number, we can have a placeholder for every possible number in the file in increasing order, and can signal in the placeholder whether that number is present or not. The simplest placeholder is a bit, so the presence of each number would represented as a 0 or 1.

This way, we only have to sequentially scan the file once for every request. This also makes it easy to toggle a number's bit when that number gets used, since only one bit has to be rewritten. This method requires `O(n)` disk writes.

## 3 solutions optimized for low disk usage

If we don't want to write to the disk, either for speed or for capacity, we can read the numbers into memory and do the search(es) there.

### 3.1 Simply reading the numbers into memory

If we simply read the numbers into memory in the order they are written, than we have to seach the same way as in `1` but we gain the speed of memory vs. the disk.

### 3.2 Sorting the numbers in memory

If we also sort the numbers, than the search speeds up as it did in `2.1`

### 3.3 Mapping the whole domain of numbers to an array of booleans

We can map the numbers into a boolean array in the memory the same way as we mapped it into a file in `2.2`. This way they will use as little memory as possible. This array can be searched using `O(n)` operations.

