package com.zqh.player.tools.common.page;


import com.baomidou.mybatisplus.plugins.Page;
import com.zqh.player.tools.common.xss.SQLFilter;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query<T> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 当前页码
     */
    private int pageNo = 1;
    /**
     * 每页条数
     */
    private int pageSize = 10;

    public Query(Map<String, Object> params){
        this.putAll(params);

        //分页参数
        if(params.get("pageNo") != null){
            pageNo = Integer.valueOf(params.get("pageNo").toString());
        }
        if(params.get("pageSize") != null){
            pageSize = Integer.valueOf(params.get("pageSize").toString());
        }

        this.put("offset", (pageNo - 1) * pageSize);
        this.put("pageNo", pageNo);
        this.put("pageSize", pageSize);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String)params.get("sortField"));
        String order = SQLFilter.sqlInject((String)params.get("sortOrder"));
        this.put("sortField", sidx);
        this.put("sortOrder", order);

        //mybatis-plus分页
        this.page = new Page<>(pageNo, pageSize);

        //排序
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)){
            this.page.setOrderByField(sidx);
            this.page.setAsc("ASC".equalsIgnoreCase(order));
        }

    }

    public Query(BasePageQueryDto dto){

        //分页参数
        if(dto.getPageNo() != null){
            pageNo = dto.getPageNo();
        }
        if(dto.getPageSize() != null){
            pageSize = dto.getPageSize();
        }

        this.put("offset", (pageNo - 1) * pageSize);
        this.put("pageNo", pageNo);
        this.put("pageSize", pageSize);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject(dto.getSortField());
        String order = SQLFilter.sqlInject(dto.getSortOrder());
        this.put("sortField", sidx);
        this.put("sortOrder", order);

        //mybatis-plus分页
        this.page = new Page<>(pageNo, pageSize);

        //排序
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)){
            this.page.setOrderByField(sidx);
            this.page.setAsc("ASC".equalsIgnoreCase(order));
        }

    }

    public Page<T> getPage() {
        return page;
    }

    public int getCurrPage() {
        return pageNo;
    }

    public int getLimit() {
        return pageSize;
    }
}
