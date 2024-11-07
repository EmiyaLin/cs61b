# Gitlet Design Document

**Name**: Xinran Zhao

## Classes and Data Structures

### Class Commit

#### Fields

1. String message: log message
2. Data timestamp: timestamp
3. Map<String, Blob(String)> mapping: a mapping of file names (sha1) to blob reference
4. String uid: the sha1 value serves as a reference to the object 
5. String parent: a parent reference
6. String secondParent: a second parent reference
7. String Blob: a reference to the blob
8. 实际上Blob指的是序列化之后的文件，就是我工作区里需要跟踪的文件序列化之后的二进制码，存在这个文件里，文件名就是哈希值

### Class 2

#### Fields

1. Field 1
2. Field 2


## Algorithms

## Persistence

