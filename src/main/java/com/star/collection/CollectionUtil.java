package com.star.collection;

import com.star.collection.iter.IterUtil;
import com.star.collection.set.SetUtil;
import com.star.lang.Editor;
import com.star.lang.Filter;
import com.star.object.ObjectUtil;
import com.star.reflect.FieldUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 集合工具类
 */
public final class CollectionUtil {

    /**
     * 两个集合去并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 并集
     */
    public static <T> Collection<T> union(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection1)) {
            list.addAll(collection2);
        } else if (isEmpty(collection2)) {
            list.addAll(collection1);
        } else {
            getCollection(collection1, collection2, list, TypeEnum.UNION);
        }
        return list;
    }


    private CollectionUtil() {
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 多个集合取交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 交集
     */
    public static <T> Collection<T> intersection(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (!isEmpty(collection1) && !isEmpty(collection2)) {
            getCollection(collection1, collection2, list, TypeEnum.INTERSECTION);
        }
        return list;
    }


    /**
     * 多个集合取并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 并集
     */
    @SafeVarargs
    public static <T> Collection<T> union(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> union = union(collection1, collection2);
        for (final Collection<T> collection : collections) {
            union = union(union, collection);
        }
        return union;
    }

    /**
     * 多个集合取差集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 差集
     */
    public static <T> Collection<T> disjunction(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (!isEmpty(collection1) && !isEmpty(collection2)) {
            getCollection(collection1, collection2, list, TypeEnum.DISJUNCTION);
        }
        return list;
    }

    /**
     * 多个集合取交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 交集
     */
    @SafeVarargs
    public static <T> Collection<T> intersection(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> intersection = intersection(collection1, collection2);
        for (final Collection<T> collection : collections) {
            intersection = intersection(intersection, collection);
        }
        return intersection;
    }

    private static <T> void getCollection(final Collection<T> collection1, final Collection<T> collection2, final ArrayList<T> list, final TypeEnum
            typeEnum) {
        final Map<T, Integer> map1 = IterUtil.countMap(collection1);
        final Map<T, Integer> map2 = IterUtil.countMap(collection2);
        final Set<T> sets = SetUtil.newHashSet(collection2);
        sets.addAll(collection1);
        int flag;
        for (final T instance : sets) {
            final int count1 = Objects.isNull(map1.get(instance)) ? 0 : map1.get(instance);
            final int count2 = Objects.isNull(map2.get(instance)) ? 0 : map2.get(instance);

            switch (typeEnum) {
                case UNION:
                    flag = Math.max(count1, count2);
                    break;
                case INTERSECTION:
                    flag = Math.min(count1, count2);
                    break;
                default:
                    flag = Math.abs(count1 - count2);
                    break;
            }

            for (int i = 0; i < flag; i++) {
                list.add(instance);
            }
        }
    }

    /**
     * 多个集合取差集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 差集
     */
    @SafeVarargs
    public static <T> Collection<T> disjunction(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> disjunction = disjunction(collection1, collection2);
        for (final Collection<T> collection : collections) {
            disjunction = union(disjunction, collection);
        }
        return disjunction;
    }

    /**
     * 过滤集合
     *
     * @param collection 集合
     * @param filter     过滤器
     * @param <T>        泛型
     * @return 集合
     */
    public static <T> Collection<T> filter(final Collection<T> collection, final Filter<T> filter) {
        final Collection<T> result = ObjectUtil.clone(collection);
        result.clear();

        for (final T instance : collection) {
            if (filter.accept(instance)) {
                result.add(instance);
            }
        }
        return result;
    }

    /**
     * 处理集合中的数据
     *
     * @param collection 集合
     * @param editor     过滤器
     * @param <T>        泛型
     * @return 集合
     */
    public static <T> Collection<T> filter(final Collection<T> collection, final Editor<T> editor) {
        final Collection<T> result = ObjectUtil.clone(collection);
        if (!isEmpty(result)) {
            result.clear();
            T modified;
            for (final T instance : collection) {
                modified = editor.edit(instance);
                if (!Objects.isNull(modified)) {
                    result.add(modified);
                }
            }
        }
        return result;
    }

    /**
     * 提取集合中元素的值封装成集合
     *
     * @param iterable 集合
     * @param editor   过滤器
     * @return 集合
     */
    public static List<Object> extract(final Iterable<?> iterable, final Editor<Object> editor) {
        final List<Object> fieldValueList = new ArrayList<>();
        for (final Object bean : iterable) {
            fieldValueList.add(editor.edit(bean));
        }
        return fieldValueList;
    }

    /**
     * 提取集合中元素的值封装成集合
     *
     * @param collection 集合
     * @param fieldName  属性名
     * @return 集合
     */
    public static List<Object> getFieldValues(final Iterable<?> collection, final String fieldName) {
        return extract(collection, bean -> {
            if (bean instanceof Map) {
                return ((Map<?, ?>) bean).get(fieldName);
            } else {
                return FieldUtil.getFieldValue(bean, fieldName);
            }
        });
    }

    /**
     * 交，并，补
     */
    enum TypeEnum {
        UNION, INTERSECTION, DISJUNCTION
    }
}
