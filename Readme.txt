Team Members :
Zankruti Desai(zndesai)
Rushi Bhatt(rbhatt)
Khelan Patel(kjpatel4)
Navjot Singh(nsingh9)

************************************************************************************

The files that were modified are,
Task-1
FileMgr.java
RemoteConnectionImpl.java

Task-2
Buffer.java
BufferMgr.java
BasicBufferMgr.java
Test.java

Buffer.java.
Addition of buffer index and variables to count read and write

BasicBufferMgr.java
We have create a HashMap fro bufferPoolMap.
Modified chooseUnpinnedBuffer function with the replacement policy with Most Recently Modified (MRM)
created a function getBufferStatistics() to return useful statistics information like number of times buffers are read and written

Test.java 
We have implemented testing as per the cases mentioned in the doc file provided

FileMgr.java
Implemented getFileStatistics() method. Created two static variables and incremented those in synchronized() read/write.

RemoteConnectionImpl.java
executed when SQL query is executed every-time. Created the object of SimpleDB.fileMgr().getFileStatistics() and called.


************************************************************************************

How to execute files for tasks.

Task-1:
1) Start the simpleDB server.
2) execute the Test.java file (which contains the test cases to check) as per,
java -cp <Path to sampled folder>/SimpleDB simpledb.server.Test simpleDB;
Note : this file can be modified to add the test cases that you wish to execute)

Task-2:
1) Start the simpleDB server.
2) execute any of the simpleDBClient files (we executed from Eclipse).
3) check output on the terminal screen.

************************************************************************************

Output:

Task-1 (as shown on terminal while executing the SQL query) :
transaction 10 committed
new transaction: 11
Total number of read blocks: 2162
Total number of written blocks: 57
--------1
transaction 11 rolled back
new transaction: 12
Total number of read blocks: 2163
Total number of written blocks: 59
transaction 12 committed

Task-2 (as shown on terminal while executing the Test.java file as per programming instructions):
Buffer which is choosen for replacement ------- 1
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 0
Read Count -----------------------466
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 2
Read Count -----------------------616
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
Buffer which is choosen for replacement ------- 0
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 2
Read Count -----------------------616
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
Buffer which is choosen for replacement ------- 2
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
--------0
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------1
Log Sequence Number --------------12

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------0
Log Sequence Number ---------------1

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------1
Log Sequence Number --------------12

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------1
Log Sequence Number --------------15

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------0
Log Sequence Number ---------------1

-----------------------------------------------------------------
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------1
Log Sequence Number --------------12

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------1
Log Sequence Number --------------15

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------1
Log Sequence Number --------------30

-----------------------------------------------------------------
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------1
Log Sequence Number --------------12

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------1
Log Sequence Number --------------15

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------1
Log Sequence Number --------------30

-----------------------------------------------------------------
Statistics for the Buffer ------- 1
Read Count -----------------------762
Write Count ----------------------1
Log Sequence Number --------------12

Statistics for the Buffer ------- 0
Read Count -----------------------467
Write Count ----------------------1
Log Sequence Number --------------15

Statistics for the Buffer ------- 2
Read Count -----------------------617
Write Count ----------------------1
Log Sequence Number --------------30


************************************************************************************

