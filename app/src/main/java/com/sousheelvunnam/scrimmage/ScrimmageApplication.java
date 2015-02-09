package com.sousheelvunnam.scrimmage;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Sousheel on 12/6/2014.
 */
public class ScrimmageApplication extends Application{

    /*private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }*/

    public void onCreate() {
        Fabric.with(this, new Crashlytics());
        Parse.initialize(this, "DurEJeaqyo0dbO6ltKxnp4oYC5Rhs5MFpmJkpgmF", "g0v1yPWLA2RpSfhVpZvGewAO6XkCNbhlJSy27nN9");
    }

}
