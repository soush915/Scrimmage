package com.sousheelvunnam.scrimmage;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

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
        Parse.initialize(this, "DurEJeaqyo0dbO6ltKxnp4oYC5Rhs5MFpmJkpgmF", "g0v1yPWLA2RpSfhVpZvGewAO6XkCNbhlJSy27nN9");
    }

}
