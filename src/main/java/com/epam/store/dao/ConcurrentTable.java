package com.epam.store.dao;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ConcurrentTable<R, C, V> {
    Table<R, C, V> table;

    public ConcurrentTable() {
        table = HashBasedTable.create();
    }

    public synchronized V put(R rowKey, C columnKey, V value) {
        return table.put(rowKey, columnKey, value);
    }

    public V get(R rowKey, C columnKey) {
        return table.get(rowKey, columnKey);
    }

    public boolean contains(R rowKey, C columnKey) {
        return table.contains(rowKey, columnKey);
    }

    public boolean containsRow(R rowKey) {
        return table.containsRow(rowKey);
    }

    public boolean containsColumn(C columnKey) {
        return table.containsColumn(columnKey);
    }
}
