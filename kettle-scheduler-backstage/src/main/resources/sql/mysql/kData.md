pageQuery
===
    *数据库表分页查询
    SELECT #use("cols")# FROM 
    k_data
    #use("condition")#
    #use("sort")#
    #use("limit")#

cols
===
    *列名
    data_id,
    data_name,
    data_big_class,
    data_small_class,
    data_service_object,
    data_size,
    add_user,
    del_flag,
    edit_user,
    add_time,
    edit_time
    
condition
===
    where 1=1
     @if(!isEmpty(kData.dataName)){
            and data_name like #'%'+kData.dataName+'%'#
     @}
     @if(!isEmpty(kData.dataServiceObject)){
            and data_service_object like #'%'+kData.dataServiceObject+'%'#
     @}
     @if(!isEmpty(kData.dataBigClass)){
            and data_big_class =#kData.dataBigClass#
     @}
     @if(!isEmpty(kData.dataSmallClass)){
            and data_small_class =#kData.dataSmallClass#
     @}
     @if(!isEmpty(kData.addUser)){
            and add_user =#kData.addUser#
     @}
     @if(!isEmpty(kData.delFlag)){
            and del_flag =#kData.delFlag#
     @}
sort
===
        order by add_time desc

limit
===
    *分页
    @if(!isEmpty(start)){
        limit #start#   
    @}
    @if(!isEmpty(size)){
        ,#size#
    @}

allCount
===
    *总数量
    SELECT COUNT(1) FROM 
    k_data
    #use("condition")#
