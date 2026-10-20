package org.pekar.monix.kernel.io;

import java.nio.ByteBuffer;

/**
 * Read-only object stored on disk
 */
public class Record
{
    private final int id, dataSize, dataOffset;
    private final ByteBuffer bytes;
    private static final int INT_SIZE = Integer.SIZE / 8;

    public Record(int id, byte[] data, int dataSize)
    {
        if (dataSize <= 0)
        {
            throw new IllegalArgumentException("Data size cannot be <= 0");
        }

        int headerCapacity = INT_SIZE * 2;
        this.id = id;
        this.dataSize = dataSize;
        this.dataOffset = headerCapacity;

        ByteBuffer buffer = ByteBuffer.allocate(headerCapacity + dataSize);
        buffer.putInt(this.id);
        buffer.putInt(dataSize);

        this.bytes = buffer.asReadOnlyBuffer();
    }

    /**
     * Deserializes a byte array into a Record instance
     * @param recordData Serializes Record instance
     * @return Deserialized bytes into Record instance
     */
    public static Record fromBytes(byte[] recordData)
    {
        ByteBuffer buffer = ByteBuffer.wrap(recordData);
        int id = buffer.getInt(), dataSize = buffer.getInt(), offset = INT_SIZE * 2;
        byte[] data = new byte[dataSize];
        buffer.get(offset, data);

        return new Record(id, data, dataSize);
    }

    public ByteBuffer getBytes()
    {
        return this.bytes;
    }

    public int getId()
    {
        return this.id;
    }

    public int getDataSize()
    {
        return this.dataSize;
    }

    public byte[] getData()
    {
        byte[] bytes = new byte[this.dataSize];
        this.bytes.get(this.dataOffset, bytes);

        return bytes;
    }
}
