#!/bin/bash
set -e

# Очистка
rm -rf out
mkdir -p out/classes
mkdir -p out/doc

# Компиляция
javac -d out/classes src/main/java/*.java

# Генерация javadoc
javadoc -d out/doc src/main/java/*.java

# Создание jar
jar cfe out/HeapSortApp.jar Main -C out/classes .

# Запуск
java -jar out/HeapSortApp.jar
