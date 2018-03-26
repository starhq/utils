package com.star.io.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.star.io.FastByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * kryo序列化实现
 *
 * @author j2cache
 */
public class KryoSerializer implements Serializer {

    /**
     * kryo做成threadlocal的
     */
    private static final ThreadLocal<Kryo> KRYOS = ThreadLocal.withInitial(Kryo::new);


    @Override
    public String name() {
        return "kryo";
    }

    @Override
    public byte[] serialize(Object obj) {
        final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        try (Output output = new Output(baos)) {
            KRYOS.get().writeClassAndObject(output, obj);
            output.flush();
            return baos.toByteArray();
        }
    }

    @Override
    public Object deserialize(byte[] bits) {
        try (Input ois = new Input(new ByteArrayInputStream(Objects.requireNonNull(bits)))) {
            return KRYOS.get().readClassAndObject(ois);
        }
    }
}
