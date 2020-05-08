setwd('/home/lendle/dev/NetBeansProjects/1082/TeachingStyleAnalysis')
tags=read.csv('二B.txt')[1]
tags=t(tags)
d=read.csv('2B.csv')
tags=as.vector(tags)
d=d[,c('B',tags)]
result=aggregate(.~B, data=d, mean)
write.csv(result, file='二Btag分佈.csv', row.names=F)
