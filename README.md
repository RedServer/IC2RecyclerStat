# IC2 Recycler Stat
Простой мод для сбора статистики использования утилизатора. Позволяет узнать, что больше всего утилизируют игроки. 
Мод полезен для настройки чёрного списка предметов, запрещенных к утилизации :smiling_imp:

### Использование
* Установите мод на сервер (можно и на клиент, если интересует одиночная игра).
* Мод сразу начнёт собирать статистику по мере загрузки чанков с механизмами; подсчитывается каждый утилизированный предмет.
* Сохраните дамп статистики с помощью команды `/saveRecyclerDump`. Файл будет создан в папке **logs** 
(в нём будет ID предмета и общее кол-во). Обратите внимание, что в списке могут присутствовать запрещенные предметы, 
ибо мод не проверяет их наличие в списке.
