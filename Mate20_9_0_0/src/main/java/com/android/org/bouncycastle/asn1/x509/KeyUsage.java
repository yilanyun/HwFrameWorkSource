package com.android.org.bouncycastle.asn1.x509;

import com.android.org.bouncycastle.asn1.ASN1Object;
import com.android.org.bouncycastle.asn1.ASN1Primitive;
import com.android.org.bouncycastle.asn1.DERBitString;

public class KeyUsage extends ASN1Object {
    public static final int cRLSign = 2;
    public static final int dataEncipherment = 16;
    public static final int decipherOnly = 32768;
    public static final int digitalSignature = 128;
    public static final int encipherOnly = 1;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int keyEncipherment = 32;
    public static final int nonRepudiation = 64;
    private DERBitString bitString;

    public static KeyUsage getInstance(Object obj) {
        if (obj instanceof KeyUsage) {
            return (KeyUsage) obj;
        }
        if (obj != null) {
            return new KeyUsage(DERBitString.getInstance(obj));
        }
        return null;
    }

    public static KeyUsage fromExtensions(Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.keyUsage));
    }

    public KeyUsage(int usage) {
        this.bitString = new DERBitString(usage);
    }

    private KeyUsage(DERBitString bitString) {
        this.bitString = bitString;
    }

    public boolean hasUsages(int usages) {
        return (this.bitString.intValue() & usages) == usages;
    }

    public byte[] getBytes() {
        return this.bitString.getBytes();
    }

    public int getPadBits() {
        return this.bitString.getPadBits();
    }

    public String toString() {
        byte[] data = this.bitString.getBytes();
        StringBuilder stringBuilder;
        if (data.length == 1) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("KeyUsage: 0x");
            stringBuilder.append(Integer.toHexString(data[0] & 255));
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("KeyUsage: 0x");
        stringBuilder.append(Integer.toHexString((data[0] & 255) | ((data[1] & 255) << 8)));
        return stringBuilder.toString();
    }

    public ASN1Primitive toASN1Primitive() {
        return this.bitString;
    }
}
