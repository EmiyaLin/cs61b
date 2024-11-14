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



init: 创建.gitlet文件夹和其他指令需要用到的文件夹
add: 添加一个文件进入暂存区，其中有多种情况<br>
1. 该文件没有被当前的commit跟踪，并且暂存区中没有，那么添加这个文件进入暂存区
2. 该文件没有被当前的commit跟踪，暂存中存在，那么修改暂存区中的这个文件
3. 该文件被当前的commit跟踪，首先比较当前文件内容和commit跟踪的blob内容是否相同，如果相同，并且该文件在暂存区中，则删除暂存区的该文件

commit: 提交一次commit
rm: 首先判断该文件有没有被当前的commit跟踪，如果没有的话，那么判断暂存区里有没有这个文件，如果有的话就把它从暂存区里面删除，如果没有就进入failure case 中， 
如果当前commit正在跟踪这个文件，把它添加进入待删除区(待删除区里的文件如果被commit的话，那么这次commit就没有对应的blob)