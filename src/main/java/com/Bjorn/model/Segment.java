package com.Bjorn.model;

public class Segment {
    public static final int CRC_LENGTH = 2;
    public static final int KEY_SIZE_LENGTH = 2;
    public static final int VALUE_SIZE_LENGTH = 4;
    private final byte[] bytes;

    private Segment(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public static Segment fromByteArray(byte[] data) {
        return new Segment(data);
    }

    public static Segment fromKeyValuePair(byte[] key, byte[] value) {
        int keySize = key.length;
        int valueSize = value.length;
        int totalSize = CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH + keySize + valueSize;

        byte[] segment = new byte[totalSize];

        // Set key size
        segment[2] = (byte) ((keySize >> 8) & 0xFF);
        segment[3] = (byte) (keySize & 0xFF);

        // Set value size
        segment[4] = (byte) ((valueSize >> 24) & 0xFF);
        segment[5] = (byte) ((valueSize >> 16) & 0xFF);
        segment[6] = (byte) ((valueSize >> 8) & 0xFF);
        segment[7] = (byte) (valueSize & 0xFF);

        System.arraycopy(key, 0, segment, CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH, keySize);

        System.arraycopy(value, 0, segment, CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH + keySize, valueSize);

        byte[] crc = new Segment(segment).crc16();
        segment[0] = crc[0];
        segment[1] = crc[1];

        return new Segment(segment);
    }

    public byte[] getKey() {
        int keySize = getKeySize();
        return extractBytes(CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH, keySize);
    }

    public byte[] getValue() {
        int keySize = getKeySize();
        int valueSize = getValueSize();
        return extractBytes(CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH + keySize, valueSize);
    }

    public int getKeySize() {
        return ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
    }

    public int getValueSize() {
        return ((bytes[4] & 0xff) << 24) | ((bytes[5] & 0xff) << 16) |
                ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff);
    }

    public byte[] getCrc() {
        return extractBytes(0, CRC_LENGTH);
    }

    private byte[] extractBytes(int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(bytes, offset, result, 0, length);
        return result;
    }

    public boolean isChecksumValid() {
        byte[] crc = crc16();
        return crc[0] == bytes[0] && crc[1] == bytes[1];
    }

    private byte[] crc16() {
        return crc16(bytes);
    }

    public boolean isSegmentValid() {
        return isChecksumValid() && getKeySize() > 0 && getValueSize() >= 0
                && bytes.length == CRC_LENGTH + KEY_SIZE_LENGTH + VALUE_SIZE_LENGTH + getKeySize() + getValueSize();
    }

    private byte[] crc16(byte[] segment) {
        int crc = 0xFFFF; // Initial CRC value
        int polynomial = 0x1021; // CRC-16 polynomial

        for (int index = CRC_LENGTH; index < segment.length; index++) {
            byte b = segment[index];
            crc ^= (b & 0xFF) << 8;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }

        return new byte[]{(byte) ((crc >> 8) & 0xFF), (byte) (crc & 0xFF)};
    }
}
