package com.kinvn.miniblog.utils;

import com.kinvn.miniblog.model.StatusesResult;
import com.kinvn.miniblog.model.WbStatus;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by Kinvn on 2017/9/7.
 */

public class StatusesResultToStatus implements Func1<StatusesResult, List<WbStatus>> {
    @Override
    public List<WbStatus> call(StatusesResult statusesResult) {
        return null;
    }
}
