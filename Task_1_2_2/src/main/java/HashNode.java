import java.util.Map;

class HashNode<K, V> implements Map.Entry<K, V> {
    final int hash;
    final K key;
    V value;
    HashNode<K, V> next;

    HashNode(int hash, K key, V value, HashNode<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
