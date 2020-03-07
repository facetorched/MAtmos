package eu.ha3.matmos.core;

import java.util.Map;
import java.util.Set;

public class MappedProvider<T extends Versioned> implements Provider<T> {
    private Map<String, T> map;

    public MappedProvider(Map<String, T> map) {
        this.map = map;
    }

    @Override
    public boolean exists(String name) {
        return this.map.containsKey(name);
    }

    @Override
    public int version(String name) {
        return this.map.containsKey(name) ? this.map.get(name).version() : -1;
    }

    @Override
    public T get(String name) {
        return this.map.get(name);
    }

    @Override
    public T instance() {
        return null;
    }

    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }
}
