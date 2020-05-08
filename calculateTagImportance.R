library(randomForest)
d=read.csv('5B.csv')
m=randomForest(B~., data=d, importance=TRUE)
i=importance(m, type=1)
rownames(i)[which(i>0)]
