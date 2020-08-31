pageQuery
===
    *数据库表分页查询
    SELECT #use("cols")# FROM 
    k_trans_record
    #use("condition")#
    #use("sort")#
    #use("limit")#

cols
===
    *列名
    record_id,
    record_trans,
    start_time,
    stop_time,
    record_status,
    log_file_path,
    add_user
    
condition
===
    where 1=1
     @if(!isEmpty(kTransRecord.addUser)){
            and add_user =#kTransRecord.addUser#
     @}
     @if(!isEmpty(kTransRecord.recordTrans)){
            and record_trans =#kTransRecord.recordTrans#
     @}
     
sort
===
        order by record_id desc

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
    k_trans_record
    #use("condition")#
    
getAll
===
    *数据库查询
    select #use("cols")# from 
    k_trans_record
    #use("conditions")#
    
conditions
===
    where 1=1
    and stop_time BETWEEN #startTime# AND #endTime#
    and record_status=1
    and record_trans = #kTransId#
