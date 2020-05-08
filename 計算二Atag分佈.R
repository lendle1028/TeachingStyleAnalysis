setwd('/home/lendle/dev/NetBeansProjects/1082/TeachingStyleAnalysis')
tags=read.csv('二A.txt')[1]
tags=t(tags)
d=read.csv('2A.csv')
tags=as.vector(tags)
d=d[,c('B',tags)]
result=aggregate(.~B, data=d, mean)
write.csv(result, file='二Atag分佈.csv', row.names=F)
