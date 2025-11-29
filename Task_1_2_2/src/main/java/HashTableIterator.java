import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


class HashTableIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    private final HashTable<K, V> tableRef;
    private int expectedModCount;
    private int bucketIndex = 0;
    private HashNode<K, V> current = null;
    private HashNode<K, V> lastReturned = null;

    HashTableIterator(HashTable<K, V> table) {
        this.tableRef = table;
        this.expectedModCount = table.modCount;
        advanceToNext();
    }

    private void checkForComodification() {
        if (tableRef.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    private void advanceToNext() {
        while ((current == null) && bucketIndex < tableRef.table.length) {
            current = tableRef.table[bucketIndex++];
        }
    }

    @Override
    public boolean hasNext() {
        checkForComodification();
        return current != null;
    }

    @Override
    public Map.Entry<K, V> next() {
        checkForComodification();
        if (current == null) {
            throw new NoSuchElementException();
        }
        lastReturned = current;
        current = current.next;
        if (current == null) {
            advanceToNext();
        }
        return lastReturned;
    }

    @Override
    public void remove() {
        checkForComodification();
        if (lastReturned == null) {
            throw new IllegalStateException();
        }
        tableRef.remove(lastReturned.key);
        expectedModCount = tableRef.modCount;
        lastReturned = null;
    }
}
