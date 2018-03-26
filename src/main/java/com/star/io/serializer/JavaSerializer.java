package com.star.io.serializer;

import com.star.exception.IORuntimeException;
import com.star.string.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

/**
 * java序列化实现
 *
 * @author j2cache
 */
public class JavaSerializer implements Serializer {

    @Override
    public String name() {
        return "java";
    }

    @Override
    public byte[] serialize(Object obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("seriaze obj failure,the reason is {}", e.getMessage()), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        final ByteArrayInputStream bais = new ByteArrayInputStream(Objects.requireNonNull(bytes));
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IORuntimeException(StringUtil.format("deserialize obj failure,the reason is {}", e.getMessage()), e);
        }
    }
}
