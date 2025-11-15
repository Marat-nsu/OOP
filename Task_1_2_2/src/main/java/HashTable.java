import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    HashNode<K, V>[] table;
    int size;
    final float loadFactor;
    int threshold;
    int modCount;

    public HashTable() {
        this.table = (HashNode<K, V>[]) new HashNode[DEFAULT_INITIAL_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * loadFactor);
    }

    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Invalid load factor");
        }
        this.table = (HashNode<K, V>[]) new HashNode[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    private int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    public V put(K key, V value) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }
        addEntry(h, key, value, idx);
        return null;
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        HashNode<K, V> e = table[bucketIndex];
        table[bucketIndex] = new HashNode<>(hash, key, value, e);
        size++;
        modCount++;
        if (size > threshold) {
            resize(2 * table.length);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        HashNode<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= newCapacity) {
            return;
        }
        HashNode<K, V>[] newTable = (HashNode<K, V>[]) new HashNode[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            HashNode<K, V> e = oldTable[i];
            while (e != null) {
                HashNode<K, V> next = e.next;
                int idx = indexFor(e.hash, newCapacity);
                e.next = newTable[idx];
                newTable[idx] = e;
                e = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
        modCount++;
    }

    public V get(K key) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                return e.value;
            }
        }
        return null;
    }

    public V remove(K key) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        HashNode<K, V> prev = null;
        for (HashNode<K, V> e = table[idx]; e != null; prev = e, e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                if (prev == null) {
                    table[idx] = e.next;
                } else {
                    prev.next = e.next;
                }
                V old = e.value;
                size--;
                modCount++;
                return old;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        modCount++;
    }

    public V replace(K key, V newValue) {
        int h = hash(key);
        int idx = indexFor(h, table.length);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (e.hash == h && Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = newValue;
                return old;
            }
        }
        return null;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HashTable)) {
            return false;
        }
        HashTable<K, V> that = (HashTable<K, V>) o;
        if (this.size != that.size) {
            return false;
        }
        for (Map.Entry<K, V> entry : this) {
            K key = entry.getKey();
            V val = entry.getValue();
            if (!that.containsKey(key) || !Objects.equals(that.get(key), val)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Map.Entry<K, V> e : this) {
            hash += Objects.hash(e.getKey(), e.getValue());
        }
        return hash;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, V> e : this) {
            sb.append(e.getKey()).append('=').append(e.getValue()).append(", ");
        }
        sb.setLength(sb.length() - 2);
        return sb.append('}').toString();
    }
}
