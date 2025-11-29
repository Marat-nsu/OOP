import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class HashTableTest {

    /**
     * Тест 1: Создание пустой хеш-таблицы.
     * Проверяем: isEmpty() == true, size() == 0, get(any) == null, containsKey(any)
     * == false.
     */
    @Test
    void testCreationEmptyHashTable() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertTrue(ht.isEmpty());
        assertEquals(0, ht.size());
        assertNull(ht.get("any"));
        assertFalse(ht.containsKey("any"));
        assertNull(ht.replace("any", 42));
    }

    /**
     * Тест 2: Добавление пары ключ-значение (k, v).
     * Проверяем: put(k,v) возвращает null (новая), size++, get(k) == v,
     * containsKey(k) == true.
     * Также: put с существующим ключом возвращает старое значение, не меняет size.
     */
    @Test
    void testPut() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertNull(ht.put("one", 1));
        assertEquals(1, ht.size());
        assertEquals(1, ht.get("one"));
        assertTrue(ht.containsKey("one"));

        Integer old = ht.put("one", 2);
        assertEquals(1, old);
        assertEquals(1, ht.size());
        assertEquals(2, ht.get("one"));
    }

    /**
     * Тест 3: Удаление пары ключ-значение по ключу.
     * Проверяем: remove(k) возвращает v, size--, get(k) == null, containsKey(k) ==
     * false.
     * remove(non-existing) == null, size не меняется.
     */
    @Test
    void testRemove() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("one", 1);
        assertEquals(1, ht.size());

        Integer removed = ht.remove("one");
        assertEquals(1, removed);
        assertEquals(0, ht.size());
        assertNull(ht.get("one"));
        assertFalse(ht.containsKey("one"));

        assertNull(ht.remove("non-existing"));
        assertEquals(0, ht.size());
    }

    /**
     * Тест 4: Поиск значения по ключу.
     * Проверяем: get(k) возвращает v для существующего, null для несуществующего.
     */
    @Test
    void testGet() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("one", 1);
        ht.put("two", 2);

        assertEquals(1, ht.get("one"));
        assertEquals(2, ht.get("two"));
        assertNull(ht.get("three"));
    }

    /**
     * Тест 5: Обновление значения по ключу.
     * Проверяем: replace(k, newV) возвращает oldV для существующего, null
     * для несуществующего.
     * get(k) == newV после, size не меняется. replace не добавляет новую пару.
     */
    @Test
    void testReplace() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("one", 1);

        Integer old = ht.replace("one", 2);
        assertEquals(1, old);
        assertEquals(1, ht.size());
        assertEquals(2, ht.get("one"));

        assertNull(ht.replace("missing", 99));
        assertEquals(1, ht.size());
        assertNull(ht.get("missing"));
    }

    /**
     * Тест 6: Проверка наличия ключа.
     * Проверяем: containsKey(k) == true для существующего, false для
     * несуществующего.
     * Включая null ключ.
     */
    @Test
    void testContainsKey() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("one", 1);
        ht.put(null, 42);

        assertTrue(ht.containsKey("one"));
        assertTrue(ht.containsKey(null));
        assertFalse(ht.containsKey("missing"));
    }

    /**
     * Тест 7: Итерирование по элементам (парам (k, v)) с
     * ConcurrentModificationException.
     * Проверяем: Итератор проходит все пары, hasNext/next работают,
     * ConcurrentModificationException при изменении.
     * Включая remove() в итераторе (если поддерживается).
     */
    @Test
    void testIteratorWithConcurrentModification() {
        HashTable<String, Integer> ht = new HashTable<>();
        for (int i = 0; i < 3; i++) {
            ht.put("k" + i, i);
        }

        int count = 0;
        for (Map.Entry<String, Integer> entry : ht) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
            assertTrue(entry.getKey().startsWith("k"));
            count++;
        }
        assertEquals(3, count);

        Iterator<Map.Entry<String, Integer>> it = ht.iterator();
        assertTrue(it.hasNext());
        ht.put("extra", 99);
        assertThrows(ConcurrentModificationException.class, it::hasNext);

        HashTable<String, Integer> ht2 = new HashTable<>();
        ht2.put("a", 1);
        ht2.put("b", 2);
        Iterator<Map.Entry<String, Integer>> it2 = ht2.iterator();
        it2.next();
        it2.remove();
        assertEquals(1, ht2.size());
        assertNull(ht2.get("a"));
        assertThrows(IllegalStateException.class, it2::remove);
    }

    /**
     * Тест 8: Сравнение на равенство с другой хеш-таблицей.
     * Проверяем: equals(true) для идентичного содержимого (независимо от порядка),
     * false для разного size/содержимого.
     * equals(false) с null/Object другого типа.
     */
    @Test
    void testEquals() {
        HashTable<String, Integer> ht1 = new HashTable<>();
        HashTable<String, Integer> ht2 = new HashTable<>();
        ht1.put("x", 1);
        ht1.put("y", 2);
        ht2.put("y", 2);
        ht2.put("x", 1);

        assertTrue(ht1.equals(ht2));
        assertTrue(ht2.equals(ht1));
        assertEquals(ht1.hashCode(), ht2.hashCode());

        assertFalse(ht1.equals(null));
        assertFalse(ht1.equals("not a table"));

        HashTable<String, Integer> ht3 = new HashTable<>();
        ht3.put("x", 1);
        assertFalse(ht1.equals(ht3));

        ht3.put("y", 3);
        assertFalse(ht1.equals(ht3));
    }

    /**
     * Тест 9: Вывод в строку.
     * Проверяем: toString() содержит все пары в формате {k1=v1, k2=v2, ...}, {} для
     * пустой.
     */
    @Test
    void testToString() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertEquals("{}", ht.toString());

        ht.put("x", 1);
        ht.put("y", 2);
        String str = ht.toString();
        assertTrue(str.startsWith("{") && str.endsWith("}"));
        assertTrue(str.contains("x=1") || str.contains("1=x"));
        assertTrue(str.contains("y=2") || str.contains("2=y"));
        assertTrue(str.contains(",") || str.length() > 5);
    }

    @Test
    void testNullKeysAndValues() {
        HashTable<String, String> ht = new HashTable<>();
        ht.put(null, "null-value");
        ht.put("key", null);
        assertEquals("null-value", ht.get(null));
        assertNull(ht.get("key"));
        assertTrue(ht.containsKey(null));
        assertTrue(ht.containsKey("key"));
        assertEquals(2, ht.size());
    }

    /**
     * Тест конструкторов с параметрами (initialCapacity, loadFactor).
     * Проверяем: IllegalArgumentException для invalid args.
     */
    @Test
    void testConstructors() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0, 0.75f));  // Capacity <=0
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(16, 0f));  // Load <=0
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(16, Float.NaN));  // NaN

        HashTable<String, Integer> ht = new HashTable<>(32, 0.8f);
        assertEquals(0, ht.size());
    }

    @Test
    void testClear() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("one", 1);
        ht.clear();
        assertTrue(ht.isEmpty());
        assertEquals(0, ht.size());
        assertNull(ht.get("one"));
        assertFalse(ht.containsKey("one"));
    }
}