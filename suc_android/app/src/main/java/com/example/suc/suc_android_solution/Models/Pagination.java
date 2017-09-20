package com.example.suc.suc_android_solution.Models;

import com.example.suc.suc_android_solution.Enumerations.UserRoles;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by efridman on 20/9/17.
 */

public class Pagination {
    private Integer page;
    private Integer size;
    private Integer numberOfElements;
    private Integer totalPages;
    private Integer totalElements;

    public Pagination(){}

    public Pagination.Builder asBuilder(){
        return new Pagination.Builder()
                .setPage(this.page)
                .setSize(this.size)
                .setNumberOfElements(this.numberOfElements)
                .setTotalPages(this.totalPages)
                .setTotalElements(this.totalElements);
    }

    @Override
    public String toString() {
        return String.format("page: %d - size: %d - numberOfElement: %d - totalPages: %d - totalElements: %d" , page,size,numberOfElements,totalPages,totalElements);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public static class Builder{
        private Pagination paginationToBuild;

        public Builder(){
            paginationToBuild = new Pagination();
        }

        public Pagination build(){
            Pagination builtPagination = paginationToBuild;
            paginationToBuild = new Pagination();
            return builtPagination;
        }

        public Pagination.Builder setPage(Integer page){
            paginationToBuild.page = page;
            return this;
        }

        public Pagination.Builder setSize(Integer size){
            paginationToBuild.size = size;
            return this;
        }

        public Pagination.Builder setNumberOfElements(Integer numberOfElements){
            paginationToBuild.numberOfElements = numberOfElements;
            return this;
        }

        public Pagination.Builder setTotalPages(Integer totalPages){
            paginationToBuild.totalPages = totalPages;
            return this;
        }

        public Pagination.Builder setTotalElements(Integer totalElements){
            paginationToBuild.totalElements = totalElements;
            return this;
        }
    }

}
