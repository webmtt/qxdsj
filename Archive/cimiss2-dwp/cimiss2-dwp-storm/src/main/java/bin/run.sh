#name 拓扑名,必须传值!!!!不同资料类型对应固定名字，名字不相符，拓扑无法启动
#      Z_SURF_BABJ  国家站小时,分钟数据
#      Z_SURF_REG   区域站小时,分钟数据
#args1 配置文件路径 /home/dpc/surfNationConfig.xml

#!/usr/bin/env bash
storm jar cimiss2-dwp-storm.jar org.cimiss2.dwp.main.StartTopology name args1