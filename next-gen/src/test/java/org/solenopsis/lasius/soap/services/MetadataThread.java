/*
 * Copyright (C) 2015 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.lasius.soap.services;

import org.solenopsis.keraiai.soap.security.SecurityMgr;
import org.solenopsis.keraiai.wsdl.metadata.MetadataPortType;

/**
 *
 * @author Scot P. Floess
 */
public class MetadataThread extends Thread {

    private final MetadataPortType port;
    final int index;
    private final SecurityMgr securityMgr;

    public MetadataThread(final MetadataPortType port, final int index, final SecurityMgr securityMgr) {
        this.port = port;
        this.index = index;
        this.securityMgr = securityMgr;
    }

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();

        final StringBuilder sb = new StringBuilder();

        System.out.println("Thread [" + index + "] is running...");

        try {
//            if (index % 10 == 0) {
//                System.out.println("Thread [" + index + "] is logging out the session...");
//
//                try {
//                    securityMgr.logout(securityMgr.getSession());
//                } catch (final LogoutException l) {
//                    System.out.println("Thread [" + index + "] got a logout exception...");
//                }
//            }

            ListMetadata.emitMetadata("Thread [" + index + "] processing...", port, "33.0", sb);
        } catch (final Exception e) {
            System.out.println("Trouble processing [" + index + "]");
            e.printStackTrace();
        }

        System.out.println("Thread [" + index + "] DONE running -> [" + (System.currentTimeMillis() - startTime) + " ms] ... [" + sb.length() + " bytes]");
    }
}
