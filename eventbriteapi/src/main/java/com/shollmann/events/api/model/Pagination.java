package com.shollmann.events.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("object_count")
    @Expose
    private int objectCount;
    @SerializedName("page_number")
    @Expose
    private int pageNumber;
    @SerializedName("page_size")
    @Expose
    private int pageSize;
    @SerializedName("page_count")
    @Expose
    private int pageCount;

    @SerializedName("_embedded")
    @Expose
    private String _embedded;

    private String continuation;

    public String getembedded() {
        return _embedded;
    }

    public void set_embedded(String _embedded) {
        this._embedded = _embedded;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getContinuation() {
        return continuation;
    }

    public void setContinuation(String continuation) {
        this.continuation = continuation;
    }
}
