/********************************************************************************
 * Copyright (c) 2020 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.emfcloud.model_comparison;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

public class UUIDType5 {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static final UUID NAMESPACE_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_OID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_X500 = UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8");

    public static UUID nameUUIDFromNamespaceAndString(UUID namespace, String name) {
        return nameUUIDFromNamespaceAndBytes(namespace, Objects.requireNonNull(name, "name == null").getBytes(UTF8));
    }

    public static UUID nameUUIDFromNamespaceAndBytes(UUID namespace, byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-1 not supported");
        }
        md.update(toBytes(Objects.requireNonNull(namespace, "namespace is null")));
        md.update(Objects.requireNonNull(name, "name is null"));
        byte[] sha1Bytes = md.digest();
        sha1Bytes[6] &= 0x0f;  /* clear version        */
        sha1Bytes[6] |= 0x50;  /* set to version 5     */
        sha1Bytes[8] &= 0x3f;  /* clear variant        */
        sha1Bytes[8] |= 0x80;  /* set to IETF variant  */
        return fromBytes(sha1Bytes);
    }

    private static UUID fromBytes(byte[] data) {
        // Based on the private UUID(bytes[]) constructor
        long msb = 0;
        long lsb = 0;
        assert data.length >= 16;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }

    private static byte[] toBytes(UUID uuid) {
        // inverted logic of fromBytes()
        byte[] out = new byte[16];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++)
            out[i] = (byte) ((msb >> ((7 - i) * 8)) & 0xff);
        for (int i = 8; i < 16; i++)
            out[i] = (byte) ((lsb >> ((15 - i) * 8)) & 0xff);
        return out;
    }
}
