package android.hardware.wifi.V1_0;

public final class RttBw {
    public static final int BW_10MHZ = 2;
    public static final int BW_160MHZ = 32;
    public static final int BW_20MHZ = 4;
    public static final int BW_40MHZ = 8;
    public static final int BW_5MHZ = 1;
    public static final int BW_80MHZ = 16;

    public static final java.lang.String dumpBitfield(int r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.hardware.wifi.V1_0.RttBw.dumpBitfield(int):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:31)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:296)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:199)
Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: not-int
	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:568)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:56)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:102)
	... 5 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.wifi.V1_0.RttBw.dumpBitfield(int):java.lang.String");
    }

    public static final String toString(int o) {
        if (o == 1) {
            return "BW_5MHZ";
        }
        if (o == 2) {
            return "BW_10MHZ";
        }
        if (o == 4) {
            return "BW_20MHZ";
        }
        if (o == 8) {
            return "BW_40MHZ";
        }
        if (o == 16) {
            return "BW_80MHZ";
        }
        if (o == 32) {
            return "BW_160MHZ";
        }
        return "0x" + Integer.toHexString(o);
    }
}
