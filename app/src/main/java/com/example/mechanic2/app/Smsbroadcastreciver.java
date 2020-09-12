package com.example.mechanic2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class Smsbroadcastreciver extends BroadcastReceiver {
    public smsbroadcastlistner smsbroadcastlistner;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION) {


            Bundle extra = intent.getExtras();
            Status smsRetriever = (Status) extra.get(SmsRetriever.EXTRA_STATUS);
            switch (smsRetriever.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:

                    Intent intsms = extra.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    smsbroadcastlistner.onsucess(intsms);
                    break;

                case CommonStatusCodes.TIMEOUT:

                    smsbroadcastlistner.error();

                    break;
            }
        }
    }

    public interface smsbroadcastlistner {
        void onsucess(Intent intent);

        void error();
    }
}
