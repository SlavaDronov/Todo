# Todo — приложение для ведения задач

Пет-проект на Kotlin: **MVVM, Room, Retrofit, RxJava, XML, Hilt**.

Android-приложение для создания, редактирования и удаления задач с приоритетом,
дедлайном и статусом выполнения. Напоминания о дедлайнах через `AlarmManager`,
тёмная тема, поиск с debounce и синхронизация с REST API.

---

## 📸 Скриншоты

| Главный экран | Тёмная тема |
|---------------|-------------|
| ![Главный экран](screenshots/screenshot_list.png) | ![Тёмная тема](screenshots/screenshot_dark.png) |

| Детали задачи | Редактирование |
|---------------|----------------|
| ![Детали](screenshots/screenshot_details.png) | ![Редактирование](screenshots/screenshot_edit.png) |

---

## 📋 Описание

**Todo** — Android-приложение для ведения задач, которое позволяет пользователям
создавать, редактировать и удалять задачи. Каждая задача имеет название,
описание, приоритет, дату выполнения и статус.

**Цель проекта** — практика сетевого взаимодействия (Retrofit + RxJava),
работа с классическим стеком Android (XML + Fragments), закрепление MVVM,
Room, Hilt, уведомлений и тёмной темы.

> ⚠️ UI реализован на **XML layouts**, а не на Jetpack Compose, согласно
> требованиям задания.

---

## ✨ Основные функции

1. **Создание задачи** — название, описание, приоритет, дата выполнения.
2. **Просмотр списка задач** — сортировка по дате создания или приоритету.
3. **Просмотр деталей задачи** — переход на отдельный экран.
4. **Редактирование задачи** — изменение полей и отметка «выполнено».
5. **Удаление задачи** — свайпом или кнопкой.
6. **Уведомления** — напоминание за 15 минут до дедлайна.
7. **Тёмная тема** — светлая / тёмная / системная с сохранением выбора.
8. **Поиск с debounce** — строка поиска с задержкой 300 мс.
9. **Синхронизация с REST API** — загрузка и отправка через Retrofit + MockAPI.

---

## 🛠️ Технологический стек

- **Язык:** Kotlin
- **Архитектура:** MVVM + Repository (Presentation → Domain → Data)
- **UI:** XML layouts, Fragments, Navigation Component, ViewBinding, Material 3
- **БД:** Room (RxJava3: `Flowable`, `Single`, `Completable`)
- **Сеть:** Retrofit + OkHttp + Gson + RxJava3 adapter
- **Асинхронность:** RxJava 3 + RxAndroid
- **DI:** Hilt
- **Уведомления:** NotificationManager, AlarmManager (точные будильники),
  `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`
- **Тема:** `AppCompatDelegate` + `values-night` + DataStore
- **Дата и время:** `MaterialDatePicker` + `MaterialTimePicker`, формат `dd.MM.yyyy HH:mm`
- **Поиск:** `BehaviorSubject` + `debounce` + `switchMap` + SQL `LIKE`

**Бэкенд:** mock-сервер на [MockAPI.io](https://mockapi.io).

---

## 📁 Структура проекта

```
Todo/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           │
│           ├── java/com/dron/todo/
│           │   ├── TodoApp.kt                            — Application с @HiltAndroidApp
│           │   ├── MainActivity.kt                       — Activity с NavHost + Toolbar
│           │   │
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── entity/
│           │   │   │   │   └── TaskEntity.kt             — сущность задачи
│           │   │   │   ├── dao/
│           │   │   │   │   └── TaskDao.kt                — DAO с RxJava3
│           │   │   │   ├── TodoDatabase.kt               — база Room
│           │   │   │   └── ThemePreferences.kt           — DataStore для темы
│           │   │   ├── remote/
│           │   │   │   ├── ApiConstants.kt               — BASE_URL
│           │   │   │   ├── dto/
│           │   │   │   │   └── TaskDto.kt                — модель JSON
│           │   │   │   ├── api/
│           │   │   │   │   └── TodoApiService.kt         — эндпоинты Retrofit
│           │   │   │   └── mapper/
│           │   │   │       └── TaskMapper.kt             — DTO ↔ Entity
│           │   │   └── repository/
│           │   │       └── TaskRepositoryImpl.kt         — реализация
│           │   │
│           │   ├── domain/
│           │   │   └── repository/
│           │   │       └── TaskRepository.kt             — интерфейс
│           │   │
│           │   ├── presentation/
│           │   │   ├── list/
│           │   │   │   ├── TaskListFragment.kt           — экран списка
│           │   │   │   ├── TaskListViewModel.kt          — ViewModel
│           │   │   │   ├── TaskListAdapter.kt            — RecyclerView.Adapter
│           │   │   │   └── SortMode.kt                   — enum сортировки
│           │   │   ├── details/
│           │   │   │   ├── TaskDetailsFragment.kt        — экран деталей
│           │   │   │   └── TaskDetailsViewModel.kt
│           │   │   └── edit/
│           │   │       ├── TaskEditFragment.kt           — экран редактирования
│           │   │       └── TaskEditViewModel.kt
│           │   │
│           │   ├── di/
│           │   │   ├── DatabaseModule.kt                 — Hilt: Room
│           │   │   ├── NetworkModule.kt                  — Hilt: Retrofit
│           │   │   ├── RepositoryModule.kt               — Hilt: Repository
│           │   │   └── PreferencesModule.kt              — Hilt: DataStore
│           │   │
│           │   └── util/
│           │       ├── NotificationHelper.kt             — канал + уведомления
│           │       ├── AlarmScheduler.kt                 — AlarmManager
│           │       ├── AlarmReceiver.kt                  — BroadcastReceiver
│           │       ├── PermissionHelper.kt               — запрос разрешений
│           │       ├── DateTimeUtils.kt                  — форматирование даты
│           │       └── ThemeManager.kt                   — переключение темы
│           │
│           └── res/
│               ├── layout/
│               │   ├── activity_main.xml
│               │   ├── fragment_task_list.xml
│               │   ├── fragment_task_details.xml
│               │   ├── fragment_task_edit.xml
│               │   └── item_task.xml
│               ├── navigation/
│               │   └── nav_graph.xml                     — граф навигации
│               ├── menu/
│               │   ├── menu_main.xml                     — иконки Toolbar
│               │   └── menu_theme.xml                    — подменю темы
│               ├── drawable/
│               │   ├── ic_add.xml                        — иконка FAB
│               │   ├── ic_theme.xml                      — иконка темы
│               │   └── ic_notification.xml               — иконка уведомления
│               ├── values/
│               │   ├── colors.xml
│               │   ├── strings.xml
│               │   └── themes.xml                        — светлая тема
│               └── values-night/
│                   └── themes.xml                        — тёмная тема
│
├── gradle/
│   └── libs.versions.toml                                — version catalog
├── build.gradle.kts                                      — корневой build
└── README.md
```

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

## 🗺️ Этапы разработки

- [x] 1. Инициализация проекта
- [x] 2. Зависимости
- [x] 3. Room (Entity, Dao, Database)
- [x] 4. Retrofit (DTO, ApiService, NetworkModule)
- [x] 5. Repository (интерфейс, реализация)
- [x] 6. ViewModel + RxJava
- [x] 7. UI на XML (список задач)
- [x] 8. Навигация (детали, редактирование)
- [x] 9. Уведомления
- [x] 10. Тёмная тема
- [x] 11. Поиск с debounce
- [x] 12. Документация и полировка

**Прогресс:** 12 / 12 ✅

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
