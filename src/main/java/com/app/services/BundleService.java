package com.app.services;

import com.app.domain.Bundle;

/**
 * Created by tom on 6/11/2016.
 */
public interface BundleService extends CRUDservice<Bundle> {
    public long count();
}
