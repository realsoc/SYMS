package com.example.hugo.syms;

import com.squareup.otto.Bus;

/**
 * Created by Hugo on 11/12/2014.
 */
public class BusProvider {
        private static final Bus BUS = new Bus();

        private BusProvider() {

        }

        public static Bus getInstance() {
            return BUS;
        }
}
