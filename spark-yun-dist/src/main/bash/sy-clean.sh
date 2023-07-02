#!/bin/bash

######################
# 清理日志脚本
######################

# hdfs
rm -rf /tmp/hadoop-zhiqingyun/nm-local-dir/usercache/zhiqingyun/filecache

# spark
rm -rf /data/spark/spark-3.4.0-bin-hadoop3/work

# k8s
kubectl delete --all pods --namespace=spark-yun

# docker
docker ps -a | grep 'k8s_POD_zhiqingyun-job-*' | awk '{print $1}' | xargs docker rm