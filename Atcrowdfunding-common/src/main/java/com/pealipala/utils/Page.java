package com.pealipala.utils;

import java.util.List;

public class Page<T> {

    private Integer pageno;
    private Integer pagesize;
    private List<T> data;
    private Integer totalsize;
    private Integer totalno;

    public Page(Integer pageno, Integer pagesize) {
        if (pageno <= 0) {
            this.pageno = 1;
        } else {
            this.pageno = pageno;
        }
        if (pagesize <= 0) {
            this.pagesize = 10;
        } else {
            this.pagesize = pagesize;
        }
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(Integer totalsize) {
        this.totalsize = totalsize;
        this.totalno = (totalsize % pagesize) == 0 ? (totalsize / pagesize) : (totalsize / pagesize + 1);
    }

    public Integer getTotalno() {
        return totalno;
    }

    private void setTotalno(Integer totalno) {
        this.totalno = totalno;
    }

    public Integer getStartIndex() {
        return (this.pageno - 1) * pagesize;
    }

}
