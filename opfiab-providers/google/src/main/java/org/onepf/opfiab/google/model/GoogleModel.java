/*
 * Copyright 2012-2015 One Platform Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onepf.opfiab.google.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parent of a few Google billing model classes.
 */
public class GoogleModel {

    private static final String NAME_PRODUCT_ID = "productId";


    @NonNull
    private final String originalJson;
    @NonNull
    private final String productId;


    protected GoogleModel(@NonNull final String originalJson, @NonNull final JSONObject jsonObject)
            throws JSONException {
        super();
        this.originalJson = originalJson;
        this.productId = jsonObject.getString(NAME_PRODUCT_ID);
    }

    /**
     * Gets JSON data associated with this billing model.
     *
     * @return JSON string.
     */
    @NonNull
    public String getOriginalJson() {
        return originalJson;
    }

    /**
     * Gets item's product identifier. Every item has a product ID which you must specify in the
     * application's product list in the Google Play Developer Console.
     *
     * @return Unique product ID, can't be null.
     */
    @NonNull
    public String getProductId() {
        return productId;
    }
}
