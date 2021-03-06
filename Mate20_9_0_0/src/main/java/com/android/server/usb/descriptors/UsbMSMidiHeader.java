package com.android.server.usb.descriptors;

import com.android.server.usb.descriptors.report.ReportCanvas;

public final class UsbMSMidiHeader extends UsbACInterface {
    private static final String TAG = "UsbMSMidiHeader";

    public UsbMSMidiHeader(int length, byte type, byte subtype, int subclass) {
        super(length, type, subtype, subclass);
    }

    public int parseRawDescriptors(ByteStream stream) {
        stream.advance(this.mLength - stream.getReadCount());
        return this.mLength;
    }

    public void report(ReportCanvas canvas) {
        super.report(canvas);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MS Midi Header: ");
        stringBuilder.append(ReportCanvas.getHexString(getType()));
        stringBuilder.append(" SubType: ");
        stringBuilder.append(ReportCanvas.getHexString(getSubclass()));
        stringBuilder.append(" Length: ");
        stringBuilder.append(getLength());
        canvas.writeHeader(3, stringBuilder.toString());
    }
}
