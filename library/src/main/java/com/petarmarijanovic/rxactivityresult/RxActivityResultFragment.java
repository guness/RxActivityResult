package com.petarmarijanovic.rxactivityresult;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by petar on 26/07/2017.
 */
public class RxActivityResultFragment extends Fragment {

    private SparseArray<BehaviorSubject<ActivityResult>> resultSubjects = new SparseArray<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSubject(requestCode).onNext(new ActivityResult(resultCode, data));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * TODO: Write JavaDoc.
     */
    public Single<ActivityResult> start(final Intent intent, final @Nullable Bundle options) {
        int requestCode = RequestCodeGenerator.generate();

        startActivityForResult(intent, requestCode, options);

        return getSubject(requestCode).firstOrError();
    }

    /**
     * TODO: Write JavaDoc.
     */
    public Single<ActivityResult> start(final PendingIntent pendingIntent) {
        int requestCode = RequestCodeGenerator.generate();
        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, null, 0, 0, 0, null);
            return getSubject(requestCode).firstOrError();
        } catch (IntentSender.SendIntentException e) {
            Log.w("RxActivityResult", "Failed to start pendingIntent", e);
            return Single.just(new ActivityResult(Activity.RESULT_CANCELED, null));
        }
    }

    private BehaviorSubject<ActivityResult> getSubject(int key) {
        BehaviorSubject<ActivityResult> subject = resultSubjects.get(key, BehaviorSubject.<ActivityResult>create());
        resultSubjects.put(key, subject);
        return subject;
    }
}
