# Дипломный проект по профессии «Тестировщик».

***

## О проекте

Целью дипломного проекта является автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API
Банка.
Приложением является веб-сервис, который предлагает купить тур по определённой цене двумя способами:

1. Обычная оплата по дебетовой карте.
2. Уникальная технология: выдача кредита по данным банковской карты.


[*Ссылка на полное описание требований к дипломному проекту](https://github.com/netology-code/qa-diploma)

### Инструменты необходимые для настройки среды тестирования

1. Docker Desktop [(руководство по установке)](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)
2. IntelliJ IDEA [(официальная страница с бесплатной версией)](https://www.jetbrains.com/idea/download/#section=windows)
3. Google Chrome

### Запуск тестов

1. Запустить Docker Desktop
2. Клонировать репозиторий с GitHub набрав в окне терминала команду:

```
    git clone https://github.com/Gena800/Diplom_Netology
```

3. Для запуска симулятора банковских сервисов в окне терминала ввести команду:

```
   docker-compose up
```

4. В новом окне терминала запустить архив Java, содержащий приложение веб-сервиса:

* для MySQL

```
   java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
```

* для Postgres

```
    java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar
```

5. В новом окне терминала запустить тестирование командой:

* для MySQL

```
   ./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"
```

* для Postgres

```
    ./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"
```

### Отчёты о проведенном тестировании

* Для просмотра отчета Gradle, необходимо открыть файл index.html в браузере, который находится:

```
    ./build/reports/tests/test/index.html
```

* Для генерации отчёта Allure ввести в терминале команду:

```
    ./gradlew allureServe
```

* по окончании формирования отчет откроется в браузере автоматически

### Отчетная документация

* [План автоматизации тестирования](txt%2FPlan.md)
* [Отчёт по итогам тестирования](txt%2FReport.md)
* [Отчёт по итогам автоматизации](txt%2FSummary.md)

[![Build status](https://ci.appveyor.com/api/projects/status/821ooo9ralf6v0gv?svg=true)](https://ci.appveyor.com/project/Gena800/diplom-netology)