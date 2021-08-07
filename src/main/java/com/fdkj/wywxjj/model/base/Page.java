package com.fdkj.wywxjj.model.base;

import java.io.Serializable;
import java.util.List;

/**
 * 分页处理类
 *
 * @author wyt
 */
public class Page<T> implements Serializable {

    /**
     * 默认的条数10
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    private List<T> dataList;
    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示的记录数
     */
    private int pageSize;
    /**
     * 总的记录数
     */
    //
    private int totalRecord;
    /**
     * 总的页数
     */
    private int totalPage;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        int totalPages = totalRecord % pageSize == 0 ? totalRecord / pageSize
                : (totalRecord / pageSize + 1);
        setTotalPage(totalPages);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Page() {
        this.pageNo = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
        this.pageSize = pageSize <= 0 ? 1 : pageSize;
    }
}
