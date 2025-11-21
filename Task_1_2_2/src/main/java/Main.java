public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.replace("one", 1.0);
        System.out.println(hashTable.get("one"));
    }
}
