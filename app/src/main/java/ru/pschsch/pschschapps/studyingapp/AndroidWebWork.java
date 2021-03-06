package ru.pschsch.pschschapps.studyingapp;

import android.net.http.HttpResponseCache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Excluder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;

/*Здесь инфа с технострима по работе в сети: okHttp, Retrofit, JSON GSON, WebView
* Основные моменты работы с сетью:
* 1)Вся работа с сетью должна происходить вне основого потока, так как в случае неудачного соединения ссервером, или какой-то долгой
* операции взаимодействия с сервером, приложение повиснет.
* 2)Трафик нужно экономить - вытекает из 3-го пункта
* 3)Скорость и качесвто связи может быть плохим
* 4)Батарейка не вечная - фоновая активность должна быть минимальна, так как операционная система может выгружать фон.
* 5)Персональные данные пользователя
* Кратко о сетях: самый распространенный протокол - IP - Internet Protocol. v4  и v6 примерно. Несмотря на большие возможности
* v6, v4 еще активно используется. v4 имеет максимальный размер IP-адресов - 4,300,000,000.
* Некоторые IP-адреса зарезервирвованы: 127.0.0.1 например или 192.168....
* IP v6 поддержка на данный момент имеет смысл: в США активно растет использование этого протокола.
* Сетевое взаимодействие: сокет API. Сокеты - это два конца моста клиент-сервер. Поверх протокла IP работают два протокола
* TCP и UDP. UDP - обмен датаграммами. Протокол TCP , в отличие от UDP, гарантирует целостность отправляемых данных. Протокол
* UDP нужен в больших нагруженных системах, где обмен данными происходит непрерывно. В остальных случаях используется TCP.
* TCP также осуществляет контроль доставки.
* Типы сетевых взаимодействий: 1)Соединяемся, спрашиваем, получает ответ, отсоединяемся
* Установка сети по TCP, в плане трафика, не самая экономная операция, плюс нагрузка на сервер идет существенная.
* Этот механизм называется polling - механизм частых опросов. Итог: прост в реализации, но накладен по трафику и нагружает сервер
* 2)Long polling - механизм длинных опросов. Отличие в том, что соединение разрывается не сразу после передачи данных, а держится
* максимально долго по прошествии какого-то неопределенного количества времени неактивности. Быстрее, чем polling, не тратися трафик
* на установку соединения.
* Firebase игнориует DozeMode. О Firebase ниже:
* Работа с протоколом TCP через протокол HTTP:
* В Android можно напрямую работать с сокетами. Сокеты берутся из пакета java.net.
* Сокет - Java-обертка над нативным кодом, запускающим настоящие дескрипторы для подключения к серверу
* Простейший алгоритм обмена данными с сервером:
* 1)Создание сокета
* 2)Получение InputStream
* 3)Чтение блоками
* 4)Закрытие сокета
* 5)Обработка исключений
* В ходе такой операции могут возникнуть 2 исключения: UnknownHostException и IOException - 1 в результате неудачного подключения
* 2, в результате разырва соединения во время чтения данных с сервера.
* Пример кода: забыл про try с ресурсами, переделаем) Оператор try с ресурсами не работает, но должен работать, разберемся потом
* суть в том, что мы передает объект сокета оператору try в качестве ресурса и ресурс сам закроется, Socket реализует Closeable,
* так что работает с этой конструкцией как нужно.
* В рамках передачи  небольших данных можно использовать сокеты.
* Самый распространенный способ передачи данных - это использование протокола HTTP.
* Самый лучший способ, с точки зрения безопасности, передача данных через HTTP запросом POST. Данные, передаваемые в POST
* можно дополнительно шифровать для лучшей безопасности.
* Использование HTTPS - лучший вариант. Обеспечивание шифрования на уровне протокола SSL(TLS в более новых версиях, большиснтво
* HTTPS-соединений использует именно его в данный момент). преимущество перед обычным HTTP - не нужно самому писать шифрование
* данным, сертификат обеспечивает это сам.
* Обычно, на серверах используются самоподписанные сертификаты, с которыми Android по умолчанию не работает. Испольуется
* такая практика потому, что получить официальный SSL-сертификат, достаточно трудоемкая во временном, документном и финансовом
* планах. Поэтмоу, разработчики сами пишут сертификаты и подписывают их. Если попробовать обратиться к серверу с таким сертифи-
* катом, то кинет исключение UntrustedSertificate. Есть способ обойти это ограничение, в гугле можно этот код найти.
* Но, это крайне небезопасный способ, так как это чревато MITM-итами(подмены сертфиикатов)
* HTTPClient - старый способ передачи данных в Android - он deprecated, так как используется OkHTTP
* HTTPUrlConnecton - один класс и все! Работает через OkHTTP. Чень прост в использовании. На каждый запрос - новый экземпляр.
* Работает по HTTP, file, ftpa, mailto. Схема рабоыт очень проста: идет запрос от клиента к севреру, клиент получает запросю
* Не реализует Closeable, соответственно try with resources нельзя использовать.
* Пример:
* URL link = new URL("http://mail.ru");
* HttpURLConnection con = null;
* try{
* con = link.openConnection();
* con.setRequestMethod("POST");
* InputStream is = new BufferedInputStream ((con.getInputStream));
* readStream(is);
* }
* finally{
* disconnect
* }
* Для отправки данных:
* то же самое, только еще выходной поток обозначить
* Для работы с большим количесвтом запросов лучше использовать пул потоков.
* WebView: специальная вьюшка, отображающая HTML. Метод loadUrl загружает страницу. Самое простое исплльзование WebView.
* WebView может обрабатывать callback-и сервера с помощью слушателей.
* OAuth - открытая авторизация - механизм, при котором при открытии определнного URL с авторизацией, сервер перебрасывает
* на другой URL, который подставляет токен - специальный ключ, котоырй предоставляет доступ к API сайта. Для работы с сервисами,
* использующими авторизацию, используются API ответствующих серивисов либо самим. Самим это можно сделать через WebView.
* Такк ак чатсо у сервисов нет API. Токен можно получит один раз и использовать его в послдедующей работе.
* Client ID и Client Secret получаются у поставщика API при регистрации приложения у поставщика. У Google есть система по выдаче
* таких идентификаторов
* JSON: акое-то время, очень популярным форматом передачи данных был XML, так как он был очень популярным. Современный формат
* JSON - JavaScript Object Notation. Преимущества такого формата: компактен, легкочитаем(human readable), использует простые
* типы данных, гибок и очень популярен.
* Пример JSON:
* Синтаксис:
* {} - начало и конец JSON файла заключены в фигурные скобки.
* Простейшая строка JSON-файла выглядит в виде тег:значение, т.е карта.
* Пример:
* {
* hey: "guy",
* anumber: 243,
* anarray[  // Границы массива обозначаются квадратными скобками
* 1,
* 2,
* "string"
* ]
* }
* Простейшие данные хранения в JSON: строки, числовые значения, булевы значения, null, URI. Использует Юникод.
* Числа в JSON типа float. В Android есть специальный пакет org.json. Состоит из 5 классов, представляющих разные типа данных
* JSON: 1) JSONArray - массив JSON 2)JSONObject - JSON объект 3)JSONString - строка, 4) JSONTokener - разборщик JSON выражений
* 5)JSONException - если струткура JSON сформирвоана неверно, то бросится это исключение.
* JSONArray не поддерживает Iterator, следовательно может быть обработан только обычными циклами, типа for или while.
* Пример:
* public static SomeType t(String jsonString) throws JSONException{
* JSONTokener json = new JSONTokener(jsonString) //сначала обрабатываем входящий JSON, в нашем случае по идентификатору jsonString
* JSONObject data = (JSONObject) json.nextValue(); //сказали операционке, что это у нас JSON объект
* } //Это обязательные строки
* Массив обрабатывается, опять же, через обычные циклы:
* JSONArray arr = jsonObj.getJSONArray("arrayKEY");
* for(int i=0; i<arr.length(); ++i){
* final String arrString = arr.getString(i);
* someCollection.add(arrString) //например
* }
* Типичный парсинг JSON. В этих классах переопределн toString().
* Но у этого формата также есть недостатки. Для каждого объект а обработки нужен такой обработчик, что непросто делать при
* работе с большим количеством данных интернет-ресурса. Так как, плюс ко вмесу, нужно проверять, чтобы все нужные поля объекта
* доходили и обрабатывались. Тут на помощь приходит GSON - сторонняяя библиотека, разработанная Google для более удобной работы
* с JSON. GSON использует Reflection - анализирует поля на сопоставимость с JSON-форматом. Названия полей класса, в который идет
* обработка JSON должны совпадать с именами полей входящего JSON-объекта. В GSON-библитеку включены 2 аннотации:
* @SerializedName и @Expose - поле, помеченное аннотацией SerializedName должно обязательно быть во входящем JSON-файле
* Поле помеченное как @Expose является своего рода маркером для работы с Excluder-ом - объектом, который позволяет исключать
* поля, непомеченные как Expose
* jsonschema2pojo.org - позволяет конвертировать JSON в Java с нужными параметрамию Может конвертировать и в Gson.
* Доп.информация
* Коды ответов от серверов обохначены в виде интовых констант. Они различаются по диапазонам: 200-е - эти коды относятся к успешным
* операциям. 300-е - коды редиректа. 400-е - коды ошибок клиента. 500-е коды ошибок сервера. 200 - код успешного реквеста от сервера
* С помощью метода setConnectTimeout класса HttpURLConnection можно установить время таймаута установления соединения с сервером.
* Дефолтное время установления соединения - 30 секунд, что достаточно долго, соответственно , при нынешних скоростях, нет смысла
* столько ждать, поэтмоу можно с помощью этого метода установить значение в миллисекундах. Метод setInstanceFollowDirects(boolean bool)
* метод позволяет сказать, что наше приложение обрабатывает редиректы. Метод getResponseCode() - возвращает код ответа сервера,
* обычно нужен для проверки на 200-й код, т.е успешной операции, чтобы обработать неудачные операции.
* Входной и выходной поток лучше реализовать в двух потоках, так как может произойти блокировка одного из потоков чтения/записи.
* при работе с сокетами в https, нужна SSLSocketFactory из пакета java.net для создания сокета, работающего с сервером по протоколу
* HTTPS, и используя созданный инстанс фабрики сокетов, использвоать его для создания объекта SSLSocket.
* Использование OAuth с WebView: как уже было сказано, при успешном входе в каком-либо сервисе, сервис возвращает токен, предста-
* вляющий из себя просто длинную строку в 16-м коде, используемом при дальнейшей работе с функциями API сервиса, которые недоступны
* без авторизации. WebView реализуется обычно в отдельной Activity. OAuth 2 возвращает токен в 2 этапа - 1-й этап - сервер
* возвращает не токен, а специальный ключ. После получения токена, можно уже делать АPI запросы.
* Метод setPrettyPrinting из класса Gson вставляет символы табуляции и переноса строк с возвратом каретки между частями JSON-файла
* Это пригодится для получения объемных JSON-файлов, так как файлы возвращаются в виде одной очень длинной строки.
* Но, в релизе, этот методе лучше убирать, так как за счет этих символов, размер строки значительно вырастает, следовательно приме-нение такое:
* отладили этим методом, обработали как нужно, те поля, которые нужны, и убрали этот метод, так как и клиенту и серверу все равно
* в виде одной строки или нескольких представлен JSON, обработчик будет корректно работать с однострочным вариантом)
* *******************************************************************************************************************************
* OkHttp: работает с пулом подключений, имеет инструментарий для обработки разных результатов запросов, кэширование ответов,
* поддержку TLS, инструменты для восстановления после сбоев, синхронный и асинронный интерфейсы. Также в зависимости добавить
* библиотеку
* Простейший пример использования синхронного варианта:
* private final OkHttpClient client;
* public ConfigureTimeouts() throws Exception{
* client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
* }
* В данном примере через строитель клиента OkHttp идет настройка таймаутов на, в нашем случае, коннект и запись. Константа
* SECONDS класса TimeUnit - это соответственно время в секундах. Такеж можно создать один таймаут на все операции.
* Можно создавать на каждый запрос новый клиент с новой конфигурацией, при этом производительность приложения не страдает. Это
* связано с тем, что при создании пула потоков один раз, OkHttp переиспользует его в дальнейшем для новых клиентов, что очень
* хорошо сказывается на скорости работы.
* Для создания запроса на сервер используется класс Request - это класс тоже имеет вложенный класс Builder в виду того, что
* запросы также могут широко конфигурироваться. Метод build, в случае , если мы укажем какие-либо невалидные значения , вернет
* всегда нам валидный объект, только будут уже использованы параметры по умолчанию вместо тех, которые, по каким-либо причинам
* считаны невалидными.
* Для получения ответа от сервера используется класс Response. Простейший синронный вызов инстанса Response
* Response response = client.newCall(requestSomeInstance).execute();
* Для асинхронного вызова используется метод enqueue(), который ставит наш респонз в очереь на обработку.
* OkHttp "из коробки" поддерживает аутентификацию с помощью класса Authenticator. Authenticator обрабатывает только ошибки с
* кодом 401.
* Interceptors - механизм перехватчиков. Предоставляет возможность перехватывать и обрабатывать запросы и ответы в процессе выполнения
* Т.е, позволяет видоизменять Request до доставки на сервер и Response до доставки клиенту. Interceptor - это интерфейс
* Интерсепторы позволяют добавлять дополнительную информацию о клиенте в реквесты, что позволит собирать некую статистику, например
* Интерсепторы бывают 2-х типов: application и network. Application interceptors -работы с пользовательскими запросами
* network interceptors - нужны при обработке факты похода в сеть и работать с данными, которые отправятся на сервер.
* Application озволяют сохранять кеш. Добавляются интерсепторы с помощью методов addInterceptor(Interceptor instance)
* и addNetworkInterceptor(Interceptor instance). Первый уровня приложения, второй уровня сети. Добавляются также в билдер
* клиента OkHttp.
* Retrofit: позволяет представить HTTP API в виде Java интерфейсов. тоже HTTP клиент. Библиотека сама генерирует этот интерфейс.
* Использует аннотации. передается ему настроенный HTTP-клиент. Работает с методами серверного API через Java-методы.
* Основа библиотеки: 5 аннотаций: GET, POST, PUT, DELETE, HEAD - набор http-методов.
* Возможность пердачи параметров в URL и настройки заголовков.
* Прмиер использования:
* @GET("group/{id}/users")
* Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
* С помощью аннотации Path мы указываем, что должно быть подставлено вместо id в фигурных скобках - это специальный синтаксис,
* по которому это определяется.
* У Retrofit тоже есть билдер. Основной компонент, передающийся билдер - BaseUrl - это базовый юрл api сервиса, с которым будет
* идти работа, к этому юрлу будут подставляться те или иные методы, которые мы запрашиваем на выполнение.
* При педеаче какого-то класса в тело метода post, для его строкового представления используется встроенный в библиотеку
* конвертер, также использующийся в билдере. он называется ConverterFactory. Можно использовать базовый конвертер на основе
* Gson, который наш класс конвертирует в JSON. Для непосредственной работы с API сервера, создается объект класса сервиса на
* основе инстанса Retrofit-билдера, которому в качесвте параметра передается класс собственно сервиса
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
* */

public class AndroidWebWork {
    Socket s;
    //метод типо в AsyncTask выполняется)
    public Void SocketExample() {
        try {
            s = new Socket("123",123);
            //здесь лишь один из конструкторов, у сокета их больше
            ByteArrayOutputStream forByteBuffer = new ByteArrayOutputStream(2048); //OutputStream, выводящий данные в байт-буфер
            byte[] buffer = new byte[2048];
            int bytesRead; //переменная на проверку -1, т.е конец входного потока
            InputStream serverInputStream = s.getInputStream(); //получение входного потока от сервера
            while((bytesRead = serverInputStream.read(buffer))!=-1){
                forByteBuffer.write(buffer, 0, bytesRead); //идет запись в поток вывода
            }
        } catch (UnknownHostException uhe){
            System.out.println("UHE");
        } catch (IOException ioe){
            System.out.println("IOE");
        }
        return null; //вот для чего Void-обертка нужна)))
    }
    public static void toJsonAndBack() {  //метод переводит тип в JSON и обратно
        GsonBuilder gsone = new GsonBuilder().excludeFieldsWithoutExposeAnnotation(); //пример использования в GSONBuilder
        Gson gson = new Gson();// объект GSON
        AndroidWebWork target = new AndroidWebWork(); // объект нашего типа
        String json = gson.toJson(target); //с помощью метода из класса Gson переводим наш объект в JSON-объект
        AndroidWebWork target2 = gson.fromJson(json, AndroidWebWork.class); //Переводит JSON в наш объект.
        Excluder excluder = new Excluder();
        excluder.excludeFieldsWithoutExposeAnnotation(); //этот метод позволит искллючить поля, непомеченные как Expose.
        OkHttpClient client; //ссылка на OkHttp-клиент
    }



}
