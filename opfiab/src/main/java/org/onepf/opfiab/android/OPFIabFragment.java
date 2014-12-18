/*
 * Copyright 2012-2014 One Platform Foundation
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

package org.onepf.opfiab.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.onepf.opfiab.OPFIab;
import org.onepf.opfiab.model.event.ActivityResultEvent;
import org.onepf.opfiab.model.event.FragmentLifecycleEvent;

import de.greenrobot.event.EventBus;

import static org.onepf.opfiab.model.event.LifecycleEvent.Type.ATTACH;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.CREATE;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.DESTROY;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.DETACH;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.PAUSE;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.RESUME;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.START;
import static org.onepf.opfiab.model.event.LifecycleEvent.Type.STOP;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OPFIabFragment extends Fragment {

    @NonNull
    public static OPFIabFragment newInstance() {
        return new OPFIabFragment();
    }


    @NonNull
    protected final EventBus eventBus = OPFIab.getEventBus();

    public OPFIabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        eventBus.post(new FragmentLifecycleEvent(CREATE, this));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventBus.post(new FragmentLifecycleEvent(ATTACH, this));
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.post(new FragmentLifecycleEvent(START, this));
    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus.post(new FragmentLifecycleEvent(RESUME, this));
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.post(new FragmentLifecycleEvent(PAUSE, this));
    }

    @Override
    public void onStop() {
        super.onStop();
        eventBus.post(new FragmentLifecycleEvent(STOP, this));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBus.post(new FragmentLifecycleEvent(DETACH, this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.post(new FragmentLifecycleEvent(DESTROY, this));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventBus.post(new ActivityResultEvent(getActivity(), requestCode, resultCode, data));
    }
}