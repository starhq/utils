package com.star.template.db.table;

import com.star.template.db.model.Table;

/**
 * 生成表实例后做些回掉用
 * <p>
 *
 * @author starhq
 */
public interface Listener {

    void onCreated(Table table);
}
