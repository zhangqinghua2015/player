package com.zqh.player.tools.common.page;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.io.Serializable;

import lombok.Data;

/**
 * @discription:
 * @date: 2018/08/24 下午2:51
 */
@Data
public class BasePageQueryDto<T> implements Serializable {

    private static final long serialVersionUID = 8666510608184982004L;

    protected Integer pageNo = 1;

    protected Integer pageSize = 50;

    protected Integer offSet = 0;

    protected String sortField;

    protected String sortOrder;

    protected Integer founderId;

    protected Integer tbId;

    public Query<T> createQuery() {
        return new Query(this);
    }

    public EntityWrapper<T> toEntityWrapper(EntityWrapper<T> wrapper) {
        return null;
    }

    public BasePageQueryDto<T> setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        if (this.pageNo != null && this.pageSize != null){
            this.offSet = (this.pageNo -1) * this.pageSize;
        }
        return this;
    }

    public BasePageQueryDto<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if (this.pageNo != null && this.pageSize != null){
            this.offSet = (this.pageNo -1) * this.pageSize;
        }
        return this;
    }
}
