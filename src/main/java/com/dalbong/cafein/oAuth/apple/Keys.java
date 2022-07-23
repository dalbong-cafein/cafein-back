package com.dalbong.cafein.oAuth.apple;

import lombok.Data;

import java.util.List;

@Data
public class Keys {

    private List<Key> keys;

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

}
