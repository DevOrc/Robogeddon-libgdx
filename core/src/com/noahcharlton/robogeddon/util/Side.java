package com.noahcharlton.robogeddon.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used for marking methods
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Side {

    String SERVER = "";
    String CLIENT = "";
    String BOTH = "";

    String value();


}
