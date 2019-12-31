package com.queryflow.page;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable {

    private List<T> records;// 当前页的记录
    private int total;// 总记录数
    private int page;// 当前页
    private int limit;// 每页记录数
    private int start;// 开始记录数
    private int pages;// 总页数

    public Pager(int total, int page, List<T> records) {
        this(total, page, 0, records);
    }

    public Pager(int total, int page, int limit, List<T> records) {
        this.records = records;
        init(total, page, limit);
    }

    private void init(int total, int page, int limit) {
        //设置基本参数
        this.total = total;
        this.limit = limit;
        this.pages = total % limit == 0 ? total / limit : total / limit + 1;
        if (page <= 0 || this.pages == 0) {
            page = 1;
        } else if (page > pages) {
            page = pages;
        }
        this.page = page;
        this.start = (page - 1) * limit;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Pager{" +
            "records=" + records +
            ", total=" + total +
            ", page=" + page +
            ", limit=" + limit +
            ", start=" + start +
            ", pages=" + pages +
            '}';
    }
}
