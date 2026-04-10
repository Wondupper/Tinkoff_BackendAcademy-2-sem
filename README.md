# Link Tracker

Проект сделан в рамках курса Академия Бэкенда.

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 23` с использованием `Spring Boot 3`.

Проект состоит из двух сервисов: **Bot** для взаимодействия с пользователями в Telegram и **Scrapper** для работы с внешними API.

## Настройка

**Создание файла `.env`**  
- В корневой директории проекта создайте файл `.env` с нужными переменными окружения:

```
TELEGRAM_TOKEN=your-telegram-token
GITHUB_TOKEN=your-github-token
SO_TOKEN_KEY=your-stackexchange-api-key
SO_ACCESS_TOKEN=your-stackexchange-access-token
```

- При запуске проекта нужно запускать сначала сервис **Bot**, а после **Scrapper**

## Доступ к Swagger UI

- **Bot**: `http://localhost:8080/swagger-ui`
- **Scrapper**: `http://localhost:8081/swagger-ui`

## Cron задача для Scrapper

Сервис Scrapper выполняет проверку обновлений каждую минуту:

```yaml
scheduler:
    link-updates:
        cron: "0 */1 * * * *"
```

