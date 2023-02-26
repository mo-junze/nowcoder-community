package com.nowcoder.community.entity;

/**
 * @author mjz
 * @create 2023-02-23-17:56
 * @description 用于封装分页相关的信息和功能
 */
public class Page {
    /**
     * 页面提供的数据
     */
    // 当前页码
    private int current = 1;
    // 显示上限(每页)
    private int limit = 10;

    /**
     * 需要返回给页面的数据
     */
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径（用于复用分页链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        // current(当前页的起始数据) * limit(当前页的最后一行) - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        }
        else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页码
     * @return
     */
    public int getTo() {
        int to = current * 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
