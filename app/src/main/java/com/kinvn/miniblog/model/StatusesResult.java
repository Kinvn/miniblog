package com.kinvn.miniblog.model;

import java.util.List;

/**
 * Created by Kinvn on 2017/9/7.
 */

public class StatusesResult {
    public List<WbStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<WbStatus> statuses) {
        this.statuses = statuses;
    }

    private List<WbStatus> statuses;
}
