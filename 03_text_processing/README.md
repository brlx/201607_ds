# Solution for the text processing challenge

This project contains the code of the solution for the text processing challenge.

## Usage

### From the command line

In the project root, issue the command
```
mvn clean install
```
Then you can run the applications with
```
java -cp <your maven repo path>/hu/brlx/201607_ds/03_text_processing/1.0f/03_text_processing-1.0f.jar hu.brlx.ds201607.textprocessing03.TextProcessor <input files' folder> <config file> <output folder>
```

### Parameters

The first parameter is the absolute path of the folder which contains the input files.
The second parameter is the absolute path of the config file with the patters.
The third parameter is the absolute path of the output folder, where the assorted files will be stored.

### Using the included test

Using the test `FileParserIT#testWithData` the project, you can test the application without bringing your own data.

Please note that the config file must not be in the same directory as the input files or else it will also be parsed as in input file.

## How it works

When the application is started, the controlling component (`Controller`) opens a thread (`FileParser`) for each input file and each thread reads their own file line by line. This way, reading the files is parallelized.
The controller also parses the config file for the patterns. Then creates a threadsafe queue for each pattern, and also a thread (called `FileOutputter`; since `FileWriter` was already taken by java and is used in this project too) for each pattern to write the lines into separate files by pattern.
The parser threads sort the lines by pattern and put them into the queue assigned to that pattern, and the outputter threads take the lines out from the queue which they are assigned to and write them to their file.

When the queues are full, the parser threads are blocked at the point where they put the new item into the queue and have to wait until its outputter takes at least one line out of that queue.
On the other hand, if the outputters are faster and the queue is empty, the outputters are blocked at the point where they take out the lines from the queue, until a new line is put into that queue.

When the input files are finished, the parser assigned to them are terminated. The controller waits (`Thread.join()`) for all of the parsers to finish, which means all of the files are processed. Next the controller signals the outputters (`Thread.interrupt()`), on which they terminate, then the controller  waits for them too and then exits.

## Possible future improvements

* The queue depth could be fine tuned based on the following circumstances
    * the ratio of the input files' number and the patterns' number
    * whether the input and output files accessed from a disk with comparable speed or not
    * available memory
* Collect lines which are not matched with any of the patters in an additional file

