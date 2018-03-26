package com.star.io.serializer;

import com.star.exception.IORuntimeException;
import com.star.string.StringUtil;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * fst序列化实现
 *
 * @author j2cache
 */
public class FSTSerializer implements Serializer {

    @Override
    public String name() {
        return "fst";
    }

    @Override
    public byte[] serialize(Object obj) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (FSTObjectOutput fOut = new FSTObjectOutput(out)) {
            fOut.writeObject(obj);
            fOut.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("seriaze obj failure,the reason is {}", e.getMessage()), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        try (FSTObjectInput inputStream = new FSTObjectInput(new ByteArrayInputStream(Objects.requireNonNull(bytes)))) {
            return inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IORuntimeException(StringUtil.format("deserialize obj failure,the reason is {}", e.getMessage()), e);
        }
    }

}
