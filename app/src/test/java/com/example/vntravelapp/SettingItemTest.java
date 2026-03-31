package com.example.vntravelapp;

import com.example.vntravelapp.models.SettingItem;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SettingItemTest {

    @Test
    public void settingItemStoresFields() {
        SettingItem item = new SettingItem("Dark mode", "Toggle theme", 123);
        assertEquals("Dark mode", item.getTitle());
        assertEquals("Toggle theme", item.getSubtitle());
        assertEquals(123, item.getIconRes());
    }
}

