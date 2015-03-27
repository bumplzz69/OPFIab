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

package org.onepf.opfiab;

import android.support.annotation.NonNull;

import org.onepf.opfiab.listener.BillingListener;
import org.onepf.opfiab.listener.OnConsumeListener;
import org.onepf.opfiab.listener.OnInventoryListener;
import org.onepf.opfiab.listener.OnPurchaseListener;
import org.onepf.opfiab.listener.OnSetupListener;
import org.onepf.opfiab.listener.OnSkuDetailsListener;
import org.onepf.opfiab.model.event.SetupResponse;
import org.onepf.opfiab.model.event.billing.BillingRequest;
import org.onepf.opfiab.model.event.billing.BillingResponse;
import org.onepf.opfiab.model.event.billing.ConsumeResponse;
import org.onepf.opfiab.model.event.billing.InventoryResponse;
import org.onepf.opfiab.model.event.billing.PurchaseResponse;
import org.onepf.opfiab.model.event.billing.SkuDetailsResponse;
import org.onepf.opfutils.OPFChecks;

import java.util.Collection;
import java.util.HashSet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class AdvancedIabHelper extends SimpleIabHelper {

    private final BillingRequestScheduler scheduler = BillingRequestScheduler.getInstance();

    protected final Collection<OnSetupListener> setupListeners = new HashSet<>();
    protected final Collection<OnPurchaseListener> purchaseListeners = new HashSet<>();
    protected final Collection<OnInventoryListener> inventoryListeners = new HashSet<>();
    protected final Collection<OnSkuDetailsListener> skuDetailsListeners = new HashSet<>();
    protected final Collection<OnConsumeListener> consumeListeners = new HashSet<>();

    AdvancedIabHelper() {
        super();
    }

    @Override
    protected void postRequest(@NonNull final BillingRequest billingRequest) {
        final BillingBase billingBase = getBillingBase();
        if (billingBase.getSetupResponse() == null) {
            // Lazy setup
            OPFIab.setup();
            scheduler.schedule(this, billingRequest);
        } else if (!billingBase.isBusy()) {
            // No need to schedule anything
            super.postRequest(billingRequest);
        } else if (!billingRequest.equals(billingBase.getPendingRequest())) {
            // If request is not already being precessed, schedule it for later
            scheduler.schedule(this, billingRequest);
        }
    }

    @SuppressFBWarnings({"BC_UNCONFIRMED_CAST"})
    public void onEventMainThread(@NonNull final BillingResponse event) {
        switch (event.getType()) {
            case CONSUME:
                for (final OnConsumeListener listener : consumeListeners) {
                    listener.onConsume((ConsumeResponse) event);
                }
                break;
            case PURCHASE:
                for (final OnPurchaseListener listener : purchaseListeners) {
                    listener.onPurchase((PurchaseResponse) event);
                }
                break;
            case SKU_DETAILS:
                for (final OnSkuDetailsListener listener : skuDetailsListeners) {
                    listener.onSkuDetails((SkuDetailsResponse) event);
                }
                break;
            case INVENTORY:
                for (final OnInventoryListener listener : inventoryListeners) {
                    listener.onInventory((InventoryResponse) event);
                }
                break;
        }
    }

    public void onEventMainThread(@NonNull final SetupResponse event) {
        for (final OnSetupListener listener : setupListeners) {
            listener.onSetup(event);
        }
    }

    public void addSetupListener(@NonNull final OnSetupListener setupListener) {
        final BillingBase billingBase = getBillingBase();
        setupListeners.add(setupListener);
        // Deliver last setup even right away
        final SetupResponse setupResponse = billingBase.getSetupResponse();
        if (setupResponse != null) {
            setupListener.onSetup(setupResponse);
        }
    }

    public void addPurchaseListener(@NonNull final OnPurchaseListener purchaseListener) {
        OPFChecks.checkThread(true);
        purchaseListeners.add(purchaseListener);
    }

    public void addInventoryListener(@NonNull final OnInventoryListener inventoryListener) {
        OPFChecks.checkThread(true);
        inventoryListeners.add(inventoryListener);
    }

    public void addSkuDetailsListener(@NonNull final OnSkuDetailsListener skuInfoListener) {
        OPFChecks.checkThread(true);
        skuDetailsListeners.add(skuInfoListener);
    }

    public void addConsumeListener(@NonNull final OnConsumeListener consumeListener) {
        OPFChecks.checkThread(true);
        consumeListeners.add(consumeListener);
    }

    public void addBillingListener(@NonNull final BillingListener billingListener) {
        addSetupListener(billingListener);
        addPurchaseListener(billingListener);
        addInventoryListener(billingListener);
        addSkuDetailsListener(billingListener);
        addConsumeListener(billingListener);
    }

    public void register() {
        BillingEventDispatcher.getInstance().register(this);
    }

    public void unregister() {
        BillingEventDispatcher.getInstance().unregister(this);
        scheduler.dropQueue(this);
    }
}
