package com.nangseakheng.common.dto;

public class MetaData {
    private boolean hashNext;
    private long totalUsers;
    private boolean hasPrevious;
    private int currentPage;
    private int pageSize;

    public MetaData(boolean hashNext, long totalUsers, boolean hasPrevious, int currentPage, int pageSize) {
        this.hashNext = hashNext;
        this.totalUsers = totalUsers;
        this.hasPrevious = hasPrevious;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public boolean isHashNext() {
        return hashNext;
    }

    public void setHashNext(boolean hashNext) {
        this.hashNext = hashNext;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}
