package org.qlrm.executor;

public class PageRequest {

    private final Integer firstResult;
    private final Integer maxResult;

    public PageRequest(Integer firstResult, Integer maxResult) {
        this.firstResult = firstResult;
        this.maxResult = maxResult;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public Integer getMaxResult() {
        return maxResult;
    }
}
