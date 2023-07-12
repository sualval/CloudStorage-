# Дипломная работа “Облачное хранилище”

### Описание проекта
Разработать приложение - REST-сервис. Сервис должен предоставить REST интерфейс для возможности загрузки файлов и вывода списка уже загруженных файлов пользователя.
Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок,
а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

### Описание и запуск FRONT

1. Установите nodejs (версия не ниже 19.7.0) на компьютер, следуя [инструкции](https://nodejs.org/ru/download/current/).
2. Скачайте [FRONT](./netology-diplom-frontend) (JavaScript).
3. Перейдите в папку FRONT приложения и все команды для запуска выполняйте из неё.
4. Следуя описанию README.md FRONT проекта, запустите nodejs-приложение (`npm install`, `npm run serve`).
5. Далее нужно задать url для вызова своего backend-сервиса.
   1. В файле `.env` FRONT (находится в корне проекта) приложения нужно изменить url до backend, например: `VUE_APP_BASE_URL=http://localhost:8080`.
      1. Нужно указать корневой url вашего backend, к нему frontend будет добавлять все пути согласно спецификации
      2. Для `VUE_APP_BASE_URL=http://localhost:8080` при выполнении логина frontend вызовет `http://localhost:8080/login`
   2. Запустите FRONT снова: `npm run serve`.
   3. Изменённый `url` сохранится для следующих запусков.

### Руководство по запуску
- Запускаем приложение Docker Desktop;
- Открываем проект в среде разработки IntelliJ IDEA;
- Собираем jar файл 
- После успешной сборки в папке будет находиться jar файл:`diploma-0.0.1-SNAPSHOT.jar`;
- В терминале выполнить команду по сборке images и containers: ```docker compose up```;

### Для тестирования frontend + backend + sql нужно авторизовать пользователя:
- Отправить POST запрос `http://localhost:8080/signup`
- JSON -> ` {
  "login": "user1",
  "password": "12345"
  }`

## Демонстрация работы
![](https://github.com/sualval/image/blob/main/docker.jpg?raw=true)
![](https://github.com/sualval/image/blob/main/postreg.jpg)
![](https://github.com/sualval/image/blob/main/login.jpg?raw=true)
![](https://github.com/sualval/image/blob/main/3addfile.jpg?raw=trueg)
![](https://github.com/sualval/image/blob/main/4.jpg?raw=true)
![](https://github.com/sualval/image/blob/main/7.jpg?raw=true)