package cz.csas.colmanbatch.addons.reader;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IdAwareMap<K, V> implements Map<K, V> {
    protected String idsDesc;

    protected K[] idFields;
    protected Map<K, V> originalMap;

    public IdAwareMap(Map<K, V> originalMap, K[] idFields){
        this.originalMap = originalMap;
        this.idFields = idFields;
        refreshIdsDesc();
    }

    @Override
    public String toString(){
        StringBuilder stb = new StringBuilder();
        stb.append(getClass()).append("@").append(Integer.toHexString(hashCode())).append("[");
        stb.append(this.idsDesc);
        stb.append("]");
        return stb.toString();
    }

    protected void refreshIdsDesc() {
        StringBuilder stb = new StringBuilder();
        for (K idField: idFields){
            if (stb.length() > 0) {
                stb.append(",");
            }
            stb.append(idField).append(":").append(originalMap.get(idField));
        }

        idsDesc = stb.toString();
    }

    @Override
    public int size() {
        return originalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return originalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return originalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return originalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return originalMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        V result = originalMap.put(key, value);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V remove(Object key) {
        V result = originalMap.remove(key);
        refreshIdsDesc();
        return result;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        originalMap.putAll(m);
        refreshIdsDesc();
    }

    @Override
    public void clear() {
        originalMap.clear();
        refreshIdsDesc();
    }

    @Override
    public Set<K> keySet() {
        return originalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return originalMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> origResult = originalMap.entrySet();
        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
        for(Entry<K, V> e : origResult){
            result.add(new IdAwareEntry<>(e));
        }
        return originalMap.entrySet();
    }

    public class IdAwareEntry<EK,EV> implements Entry<EK, EV>{
        protected Entry<EK, EV> originalEntry;

        public IdAwareEntry(Entry<EK, EV> originalEntry) {
            this.originalEntry = originalEntry;
        }

        @Override
        public EK getKey() {
            return originalEntry.getKey();
        }

        @Override
        public EV getValue() {
            return originalEntry.getValue();
        }

        @Override
        public EV setValue(EV value) {
            EV result = originalEntry.setValue(value);
            refreshIdsDesc();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdAwareEntry<?, ?> that = (IdAwareEntry<?, ?>) o;
            return originalEntry.equals(that.originalEntry);
        }

        @Override
        public int hashCode() {
            return Objects.hash(originalEntry);
        }
    }

    @Override
    public boolean equals(Object o) {
        return originalMap.equals(o);
    }

    @Override
    public int hashCode() {
        return originalMap.hashCode();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return originalMap.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        originalMap.forEach(action);
        refreshIdsDesc();
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        originalMap.replaceAll(function);
        refreshIdsDesc();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V result = originalMap.putIfAbsent(key, value);
        refreshIdsDesc();
        return result;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean result = originalMap.remove(key, value);
        refreshIdsDesc();
        return result;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean result = originalMap.replace(key, oldValue, newValue);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V replace(K key, V value) {
        V result = originalMap.replace(key, value);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V result = originalMap.computeIfAbsent(key, mappingFunction);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V result = originalMap.computeIfPresent(key, remappingFunction);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V result = originalMap.compute(key, remappingFunction);
        refreshIdsDesc();
        return result;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V result = originalMap.merge(key, value, remappingFunction);
        refreshIdsDesc();
        return result;
    }
}
