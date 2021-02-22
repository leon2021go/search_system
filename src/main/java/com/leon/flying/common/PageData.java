package com.leon.flying.common;

import java.util.HashMap;

public class PageData extends HashMap<String, Object> {
    private static final long serialVersionUID = -440772689786277256L;
    public static final String PAGESIZE = "pageSize";
    public static final String TOTALPAGE = "totalPage";
    public static final String TOTALCOUNT = "totalCount";
    public static final String CURRENTPAGE = "currentPage";
    public static final String ITEMS = "items";

    public PageData(int initialCapacity) {
        super(initialCapacity);
    }

    public void setPageData(Integer currentPage, Integer pageSize, Integer totalPage, Long totalCount, Object items) {
        this.put("currentPage", currentPage);
        this.put("pageSize", pageSize);
        this.put("totalPage", totalPage);
        this.put("totalCount", totalCount);
        this.put("items", items);
    }
}
