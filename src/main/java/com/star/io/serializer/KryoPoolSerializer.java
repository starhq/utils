package com.star.io.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.star.io.FastByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * kryopool序列化实现
 *
 * @author j2cache
 */
public class KryoPoolSerializer implements Serializer {

    @Override
    public String name() {
        return "kryo-pool";
    }

    @Override
    public byte[] serialize(Object obj) {
        final KryoPool pool = KryoPoolSingleton.getInstance().getKryoPool();
        final Kryo kryo = pool.borrow();
        try (FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream(); Output output = new Output(fbaos)) {
            kryo.writeClassAndObject(output, Objects.requireNonNull(obj));
            output.flush();
            pool.release(kryo);
            return fbaos.toByteArray();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        final KryoPool pool = KryoPoolSingleton.getInstance().getKryoPool();
        final Kryo kryo = pool.borrow();
        try (Input ois = new Input(new ByteArrayInputStream(Objects.requireNonNull(bytes)))) {
            final Object result = kryo.readClassAndObject(ois);
            pool.release(kryo);
            return result;
        }
    }

    /**
     * 连接池单例
     *
     * @author starhq
     */
    private static class KryoPoolSingleton {
        /**
         * 工厂
         */
        private static KryoFactory factory;
        /**
         * 池
         */
        private static KryoPool pool;


        private KryoPoolSingleton() {
        }

        /**
         * 获取对象实例
         *
         * @return 对象实例
         */
        public static KryoPoolSingleton getInstance() {
            return KryoPoolHolder.INSTANCE;
        }

        /**
         * 获取工厂类
         *
         * @return 工厂类
         */
        private KryoFactory getFactory() {
            if (factory == null) {
                factory = Kryo::new;
            }
            return factory;
        }

        /**
         * 获取池
         *
         * @return 获取池
         */
        public KryoPool getKryoPool() {
            if (pool == null) {
                pool = new KryoPool.Builder(getFactory()).softReferences().build();
            }
            return pool;
        }

        /**
         * 对象持有类
         */
        private static class KryoPoolHolder {
            /**
             * 初始化单例
             */
            public static final KryoPoolSingleton INSTANCE = new KryoPoolSingleton();
        }
    }
}
