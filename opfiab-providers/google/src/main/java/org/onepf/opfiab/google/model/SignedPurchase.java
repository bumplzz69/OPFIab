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

import org.onepf.opfiab.model.billing.Purchase;

/**
 * This model is an extension of purchase that additionally contains Google specific data.
 */
public class SignedPurchase extends Purchase {

    @NonNull
    private final String signature;

    public SignedPurchase(@NonNull final Purchase purchase, @NonNull final String signature) {
        super(purchase.getSku(),
              purchase.getType(),
              purchase.getProviderInfo(),
              purchase.getOriginalJson(),
              purchase.getToken(),
              purchase.getPurchaseTime(),
              purchase.isCanceled());
        this.signature = signature;
    }

    /**
     * Gets this purchase signature. Used for purchase verification.
     *
     * @return Purchase signature, cannot be null.
     */
    @NonNull
    public String getSignature() {
        return signature;
    }
}
