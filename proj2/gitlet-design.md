# Gitlet Design Document
### 2024.11.1 - 2024.12.1
**Name**: Xinran Zhao

本项目是来自于CS61b的proj2，其内容是用java实现对版本管理Git的模仿，最后的成品是一个简化版的git，同时还支持远程功能，前前后后弄了一个月时间，

## Classes and Data Structures

### Commit类

#### Fields

1. String message: log message
2. Data timestamp: timestamp
3. Map<String, String> trackingFile: 记录该commit跟踪的文件，key是文件名称，value是该文件对应的blob哈希值
4. String UID: the sha1 value serves as a reference to the object 
5. String parent: a parent reference
6. String secondParent: a second parent reference
7. String branch：该commit所在的branch

#### 构造器
    public Commit()

    public Commit(String message, Date timestamp, String parent, Map<String, String> trackingFile,List<String> stagingFilesList, List<String> removalFilesList, String branch)

    public Commit(String message, Date timestamp, String parent, Map<String, String> trackingFile,List<String> stagingFilesList, List<String> removalFilesList, String branch,String secondParentUid)


### Repository类   
该类是对gitlet所有指令的方法实现


## Algorithms
以下是对gitlet所有指令的理解
- init: `java gitlet.Main init`创建.gitlet文件夹和其他指令需要用到的文件夹
  + 思路就是创建.gitlet文件夹和子文件夹,.gitlet文件夹的构成在下面有介绍
- add: `java gitlet.Main add [file name]`添加一个文件进入暂存区，如果该文件未被跟踪在commit之后就会被跟踪，其中有多种情况
  1. 该文件没有被当前的commit跟踪，并且暂存区中没有，那么添加这个文件进入暂存区
  2. 该文件没有被当前的commit跟踪，暂存中存在，那么修改暂存区中的这个文件
  3. 该文件被当前的commit跟踪，首先比较当前文件内容和commit跟踪的blob内容是否相同，如果相同，并且该文件在暂存区中，则删除暂存区的该文件。
  + 思路就是根据情况判断修改暂存区和删除区
- commit: `java gitlet.Main commit [message]`提交一次commit，根据暂存区和删除区的内容修改跟踪文件
  + 思路就是根据暂存区和删除区的内容new一个commit并且序列化到commit文件夹中

- rm: `java gitlet.Main rm [file name]`删除一个文件并将该文件放入删除区
  + 思路就是根据文件是否跟踪，如果没有跟踪，移出暂存区，被跟踪，放入删除区，同时CWD中也要删除这个文件
- log: `java gitlet.Main log`输出当前分支的日志
  + 思路就是根据commit的parent一边遍历一边输出
- global-log: `java gitlet.Main global-log`根据字典序输出所有分支的日志
  + 思路就是直接输出commit文件夹，记得按照字典序排序，可以用List的sort方法
- find: `java gitlet.Main find [commit message]`根据message输出有相同message的commit的uid
  + 思路就是在commit文件夹中筛选符合条件的commit
- status: `java gitlet.Main status`展示分支、暂存区、删除区、工作区中未被暂存的修改、工作区中未被跟踪的文件
  + 思路就是直接展示
- checkout:
  1. java gitlet.Main checkout -- [file name]
    将某文件恢复至当前头指针跟踪的内容
  2. java gitlet.Main checkout [commit id] -- [file name]
    将某文件恢复至commit id指向的跟踪内容
  3. java gitlet.Main checkout [branch name]
    将当前CWD、.gitlet恢复至某个分支状态， HEAD指向该分支
  + 思路就是根据获取文件，可能会覆盖、删除、添加文件
- branch: 创建一个新分支
  + 思路就是获取当前commit的内容，并且在branch里新建一个文件，文件名是分支名，内容是当前commit的序列化内容
- rm-branch: 删除某个分支，只需要移除branch指针即可
  + 只需要移除该branch指针即可
- reset: `java gitlet.Main reset [commit id]` 和checkout到某个branch类似，这个相当于是checkout到某个指定的commit
- merge: `java gitlet.Main merge [branch name]` 将branch name指向的分支合并到HEAD指向的分支
  + 这个比较复杂，首先需要获取splitPoint，就是两个分支的最近公共祖先，由于可能出现有的分支是有两个parent的，又要是最近的公共祖先，那就用bfs
    遍历就好了。获取了splitPoint之后要和比较branchCommit、currentCommit，根据不同有七种情况，这里看文档说明吧。处理完成文件之后，自动提交一次commit
    ，parent是currentCommit，secondParent是branchCommit。

## Persistence
我的.gitlet文件夹的结构是这样设计的
```plaintext
.
├── HEAD
├── blob
│ ├── 0a9ccb886b2fb13ec36a1f52def86d3317b063e7
│ ├── 3f786850e387550fdab836ed7e6dc881de23001b
│ ├── 696028af003c021b7d0c9b19f14b22656eb79da2
│ ├── 8d2792dd9c16fcc6c3c62d40a0adccc14895bbcd
│ └── e4788b07c95f9283b03f4f40672fd7f39a0df6a6
├── branch
│├── master
│└── other
├── commit
│├── 12aba20798b7cb6c6293ac22b449135a4a7ba2c6
│├── 366e68866be525c36ee42745f96c469281925216
│├── 518831b7ea4611c2d3693dd587cb650f325f8f3d
│├── 8d9f41129857561a2dbb094c9514eba6fa6ff356
│├── ad540cdc30f96a6bbed5fbf87d7af47a54ac28da
│├── b5dafbd4c0d4c453545bd88aedca07832226c23d
│├── d2ee103fae34c3693e7c712f9063d46a6355bb42
│└── eb36cd323bdfb13f501cfce49231dea186e24711
├── current_branch
├── initialCommit
├── removalArea
│└── a.txt
└── stagingArea

```
blob文件夹用于保存跟踪文件，文件名是跟踪文件的uid，内容是跟踪文件序列化内容
branch文件夹用于保存分支，文件名是分支名，内容是分支对应的commit的uid
commit文件夹用于保存commit，文件名是commit的uid，内容是commit序列化内容
current_branch用于保存当前分支，内容是当前分支的名称
initialCommit用于保存初始提交，即init指令自动生成的提交，内容是初始提交的序列化内容
removalArea用于保存被移除跟踪的文件，里面存文件的copy
stagingArea用于保存被暂存的文件，里面存文件的copy