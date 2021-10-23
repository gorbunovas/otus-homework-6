# Идемпотентность
Идемпотентность в сервисе Заказ реализована с помощью уникального заголовка Idempotency-Key. 
Клиент пользователя создает ключ запроса и пересылает его вместе с запросом создания заказа на сервер, если запрос с таким ключем уже приходил, клиенту возвращается ошибка 409, если запрос новый, то клиенту возвращается идентификатор заказа.   

# Установка
Выполнить команда в директории charts:
helm upgrade --install billing .\hw-billing\ -f .\hw-billing\values.yaml
helm upgrade --install notification .\hw-notification\ -f .\hw-notification\values.yaml
helm upgrade --install order .\hw-order\ -f .\hw-order\values.yaml

Тесты postman находятся в директории tests/Hw7

# REST

[Описание REST интерфейса](/api/rest/restful.yaml)

![scheme](/api/rest/rest.png)

# Event Notification
[Описание ASYNC интерфейса](/api/event-notificaton/notification.yaml)
[Описание REST интерфейса](/api/event-notificaton/rest.yaml)

![scheme](/api/event-notificaton/notification.png)

# Event Collaboration

[Описание интерфейса](/api/event-collaboration/collaboration.yaml)

![scheme](/api/event-collaboration/collaboration.png)


# Предпочтительный вариант
В данном случае предпочтительным вариантом будет способ REST.
Большинство действий пользователя являются синхронными: положить деньги на счёт, сделать заказ. Если что-то пойдёт не так, то мы должны быстро оповестить клиента. 
Почтовое взаимодействие при этом не является real-tame каналом, так как зависит от внешних провайдеров, но при этом сервис отправки почты может реализовать внутри себя очередь отправки, если внешний провайдер не отвечает.

Главный минус подхода - его надёжность, так как если упадёт один сервис, то вся система будет недоступна. Что бы повысить надёжность, нужно внедрять стратегии обработки ошибок либо внутри сервисов, либо средствами service-mash


