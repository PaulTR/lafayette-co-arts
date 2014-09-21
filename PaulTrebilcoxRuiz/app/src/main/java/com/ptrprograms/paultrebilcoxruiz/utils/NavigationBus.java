package com.ptrprograms.paultrebilcoxruiz.utils;

/**
 * Created by paulruiz on 9/20/14.
 */
import com.squareup.otto.Bus;

/**
 * Created by PaulTR on 5/12/14.
 */
public class NavigationBus extends Bus {
    private static final NavigationBus navigationBus = new NavigationBus();

    public static NavigationBus getInstance() {
        return navigationBus;
    }

    private NavigationBus() {

    }

}