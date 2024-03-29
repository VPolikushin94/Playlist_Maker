# Описание приложения

Проект представляет собой приложение музыкального плеера, используещее API Apple Music. Приложение предоставляет следующую функциональность:

- Поиск трека

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/7a4c0245-69fa-4121-a55b-eb86d63bae70" width="250"/>

- Воспроизведение трека и отображение детальной информации о нем

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/fc49be28-302d-42a7-b9f2-2bc748ca02bf" width="250"/>

- Создание плейлиста

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/8922f96a-17d4-4236-9fc5-baac95f59e1a" width="250"/>

- Добавление в плейлист

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/969b881f-57a7-49d0-829b-7f44985279af" width="250"/>

- Просмотр плейлистов и их редактирование

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/9cd0c8cf-4bf0-4b61-a9d9-7e676bc4c15e" width="250"/>
  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/6466ac86-1c06-40b2-836c-4767795fe862" width="250"/>
  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/5515fd36-6364-4083-b7ff-91dc60d44082" width="250"/>

- Просмотр избранного

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/1a435e44-40fc-4c8e-87d3-2dba7fde2576" width="250"/>

- Настройки

  <img src="https://github.com/VPolikushin94/Playlist_Maker/assets/121296133/9651979d-c84a-47c0-a725-fa3721a17288" width="250"/>

## Общие требования

- Приложение должно поддерживать устройства, начиная с Android 7.0 (minSdkVersion = 24)

## Экран поиска трека

На этом экране пользователь может искать треки по любому непустому набору слов поискового запроса. Результаты поиска
представляют собой список, содержащий краткую информацию о треке.

## Экран плеера

Нажав на элемент списка найденных треков (а так же в списке избранных треков и в плейлисте), пользователь попадает на
экран плеера с подробным описанием трека. На данном экране пользователь может воспроизвести трек, добавить его в избранное и плейлист.

## Экран плейлиста 

Пользователь имеет возможность отредактировать название и описание плейлиста, а также удалить или поделиться им через share dialog.

## Экран медиатека

На данном экране пользователь, с помощью свайпа, может переключаться между списками избранных треков и созданных плейлистов.

## Экран настройки

Пользователь может выбрать светлую/темную тему, поделиться приложением, написать на почту в поддержку и перейти на сайт с пользовательским соглашением.

## Стэк

MVVM, Kotlin, Coroutines, Room, Shared Preferences, Koin, Retrofit, Gson, ListAdapter, DiffUtil, ViewBinding, Navigation Component

