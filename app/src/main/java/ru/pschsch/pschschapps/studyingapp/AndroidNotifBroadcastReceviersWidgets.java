package ru.pschsch.pschschapps.studyingapp;
/*последняя лекция с технотрека
* Нотификации - push-и. - информация о событии вне открытого приложения. Нотификации д/б ожидаемы. Не надо их кидать часто и много.
* На приложение - 20 нотификаций. нотификации поддерживают кастомные лэйауты. Но пользователь ожтдает увидеть стандартную нотификацию
* Группировка нотификаций: можно объявить нотификации в одну группу, что нотификации по отдельности не захламлями панель нотфиикаций
* на устройстве пользователя. Перекрывающие нотификации - пример, звонок. Каналы нотификаций - начиная с 26 api. Разделяются
* нотификации по категориям важности, условно. Если пользователь отключит нотификации, то отключаются все нотификации.
* В нотификации должно быть время произошедшего события. Использование иконки приложения в черно-белом фиде в статус баре
* как правило, допустим, важная нотификация, но пользователь сразу по ней не перешел. тогда в таком случае в приложении должна быть
* предусмотрена возможность оповестить пользователя, какая информация была обновлена. Нотфиикация создается через билдер.
* Нотфикацию показывается внутри другого приложения
* Возможности: группировка, звук, вибрация, управление светодиодом, обновление по id, удалять, создавать layout для нотфикации
* задать иконку и картинку для отображения, задавать приоритет, задавать категорию. Удаление полезно, допустим при входе пользователя
* в приложение. После его входа, все нотификации соответсвенно нужно удалить из статус бара. Можно задать контакт по URI.
* Нотификации мжно реализовывать из другого потока
* Broadcast - широковещательные пакеты. используется для передачи информации другим приложениям об изменении в своем приложении или
* системе. Например, ловить звонок или смс. Или, ловить инфу о батарее. 2 типа бродкастов: обычные и ordered. Обычный бродкаст
* рассылается системой, неизвестно в каком порядке он дойдет приложению. Ordered - в порядке приоритета. Подписываться можно на
* изменения состояния сети, времени системы. Broadcast Receiver указанный в XML - условно deprecated, скорее всего он не будет
* работать.
* BroadcastReceiver - выполняются в UI потоке, могут быть sticky. Вызываются также через Intent как и другие основные компоненты
* приложения с помощью метода sendBroadcast(Intent intent)
* Pending Intent: нужен длоя запуска активити другого приложения, при этом используется контекст и все пермишины этого приложения,
* а не нашего.
*
*
*
* */
public class AndroidNotifBroadcastReceviersWidgets {
}
