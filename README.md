# Todo — приложение для ведения задач

Пет-проект на Kotlin: **MVVM, Room, Retrofit, RxJava, XML, Hilt**.

---

## 📋 Описание

**Todo** — Android-приложение для ведения задач, которое позволяет пользователям создавать, редактировать и удалять задачи. Каждая задача имеет название, описание, приоритет, дату выполнения и статус (выполнена / не выполнена).

**Цель проекта** — практика сетевого взаимодействия (Retrofit + RxJava), работа с классическим стеком Android (XML + Fragments), закрепление MVVM, Room, Hilt, уведомлений и тёмной темы.

> ⚠️ UI реализован на **XML layouts**, а не на Jetpack Compose, согласно требованиям задания.

---

## ✨ Основные функции

1. **Создание задачи** — название, описание, приоритет, дата выполнения.
2. **Просмотр списка задач** — сортировка по дате создания или приоритету.
3. **Просмотр деталей задачи** — переход на отдельный экран.
4. **Редактирование задачи** — изменение полей и отметка «выполнено».
5. **Удаление задачи** — свайпом или кнопкой.
6. **Уведомления** — напоминание за 15 минут до дедлайна.
7. **Тёмная тема** — переключение с сохранением выбора.
8. **Доп. задача: поиск** — строка поиска на главном экране с debounce в RxJava (заметок может быть очень много).

---

## 🛠️ Технологический стек

- **Язык:** Kotlin
- **Архитектура:** MVVM + Repository (Presentation → Domain → Data)
- **UI:** XML layouts, Fragments, Navigation Component, ViewBinding, Material 3
- **БД:** Room (RxJava3: `Flowable`, `Single`, `Completable`)
- **Сеть:** Retrofit + OkHttp + Gson + RxJava3 adapter
- **Асинхронность:** RxJava 3 + RxAndroid
- **DI:** Hilt
- **Уведомления:** NotificationManager, AlarmManager (точные будильники), `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`
- **Тема:** `AppCompatDelegate` + `values-night` + DataStore

**Бэкенд:** mock-сервер на [MockAPI.io](https://mockapi.io).

---

## 🗺️ План работы

- [x] 1. Инициализация проекта
- [x] 2. Зависимости
- [x] 3. Room (Entity, Dao, Database)
- [x] 4. Retrofit (DTO, ApiService, NetworkModule)
- [ ] 5. Repository (интерфейс, реализация)
- [ ] 6. ViewModel + RxJava
- [ ] 7. UI на XML (список, детали, редактирование)
- [ ] 8. Навигация
- [ ] 9. Уведомления
- [ ] 10. Тёмная тема
- [ ] 11. Поиск с debounce
- [ ] 12. Документация и полировка

**Прогресс:** 4 / 12

---

## 🚧 Текущий этап

**Этап 5 — Repository (интерфейс, реализация)**

Планирую:
- `TaskRepository` — интерфейс в `domain/`
- `TaskRepositoryImpl` — реализация в `data/`
- Связка Room + Retrofit через RxJava
- `RepositoryModule` (Hilt) для биндинга

---

## ✅ Что сделано

### Этап 1. Инициализация ✅
Создан проект Todo в Android Studio, настроен `.gitignore`, инициализирован Git-репозиторий, добавлен README.

### Этап 2. Зависимости ✅
Подключены Room, Retrofit, RxJava 3, Hilt, Navigation Component. Включён ViewBinding. Создан класс `TodoApp` с `@HiltAndroidApp`.

### Этап 3. Room ✅
Созданы `TaskEntity` (8 полей), `TaskDao` (RxJava3: `Flowable`, `Single`, `Completable`), `TodoDatabase`, `DatabaseModule` (Hilt).

### Этап 4. Retrofit ✅
Созданы `ApiConstants`, `TaskDto`, `TodoApiService` (5 эндпоинтов на RxJava3), `NetworkModule` (Retrofit + OkHttp + Gson + RxJava3 + Hilt), `TaskMapper`. Проверено: `GET /tasks` возвращает данные с MockAPI.

---

## 🚀 Запуск

### Требования
- Android Studio Meerkat (2024.1)+
- JDK 17+
- Android SDK 26+

### Шаги
1. `git clone https://github.com/SlavaDronov/Todo.git`
2. Открыть в Android Studio
3. Sync Gradle
4. Run на устройстве API 26+

---

## 📅 Формат коммитов

- `Init:` — инициализация проекта
- `Build:` — зависимости, сборка
- `Feature:` — новая функциональность
- `UI:` — интерфейс
- `Fix:` — исправления
- `Refactor:` — рефакторинг
- `Docs:` — документация

---

## 👤 Автор

**Slava Dronov** — [@SlavaDronov](https://github.com/SlavaDronov)

---

*Проект создан для обучения Android-разработке на Kotlin. UI на XML.*