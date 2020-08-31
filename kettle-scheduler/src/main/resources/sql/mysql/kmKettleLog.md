countByDateAndCategoryId
===
    *总数量
    SELECT IFNULL(SUM(count),0) FROM 
    km_kettle_log
    WHERE category_id = #categoryId#
    @if(!isEmpty(startDate)){
        and date >= #startDate#
    @}
    @if(!isEmpty(endDate)){
        and date <= #endDate#
    @}
    
countByDateAndCategoryIdGroupByDate
===
    *总数量    
    SELECT
    	date, SUM(count) sumCount
    FROM km_kettle_log
    WHERE category_id = #categoryId#
    @if(!isEmpty(startDate)){
        and date >= #startDate#
    @}
    @if(!isEmpty(endDate)){
        and date <= #endDate#
    @}
    GROUP BY date;
