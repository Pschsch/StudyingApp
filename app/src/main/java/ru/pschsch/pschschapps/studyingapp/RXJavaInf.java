package ru.pschsch.pschschapps.studyingapp;
/*********************************Класс со всей информацией по либе RXJava*******************************************************
 * @RXJavaInf
 * Основой библиотеки RXJava выступают паттерны Наблюдатель(Observer) и Наблюдаемое(Observable).
 * Observable генерирует события, а Observer их обрабатывает. Понятие события в RXJava достаточно обширное: события здесь выступают
 * в роли потока данных. События в этом потоке данных имеют три типа: Next, Error и Completed. Next - передается следующее событие
 * Error- произошла ошибка, Completed - поток завершен и данных больше не будет.
 * Пример использования: допустим, происходит поиск чего-либо в Интернете по определенным критериям. В случае успешного поиска,
 * нам возвращается событие Next. По мере успешного завершения поиска возвращается Completed. Допустим, мы ищем отель. Успешный
 * поиск отеля вернется нам в виде объекта Observable. А нашим Observer мы подписываемся на Observable и обрабатывает прилетающие
 * к нам события. Итак, если нам прилетает очередной отель, нам прилетело событие Next и мы продолжаем поиск, если прилетает
 * Error, то поиск был прерван с ошибкой, уведомляем пользователя об этом. Если прилетает Completed - то соотвественно поиск был
 * успешно завершен и мы можем уведомить пользователя об этом. Но, далеко не всегда могут прилетать все 3 типа событий: например,
 * при обработке нажатий кнопки, будет прилетать только событие Next, так каккнопку можно нажать сколько угодно раз, что означает
 * что Completed мы не получим. И Error то же на вряд ли придет.
 * Более подробно о наблюдаемых и наблюдателях: класс Observable generic-типа (Observable<T>) - это значит, что наблюдаемое может
 * передавать события в виде любых объектов. Создадим Observable:
 * Observable типа String предоставляет данные наблюдателю в виде объектов класса String. Методом fromArray я создал наблюдаемое
 * которое будет передавать наблюдателю введенные строковые литералы. Методом subscribe(Observer<? super T>) класса Observable,
 * принимающий Observer в качестве аргумента, подписываем Observer на экземпляр Observable. Таким образом, Observer будет
 * вызывать метод onNext() на каждый строковый литерал , переданный наблюдаемому и, по завершении переданного массива строк,
 * вызовет метод onComplete(). Простейшее взаимодействие этих двух сущностей.
 *
 * Операторы RXJava: операторы в RXJava существуют нескольких типов:
 * 1.Операторы создания: эти операторы создают Observable
 * с разными свойствами: 1)from(в RXJava 2 нет отдельного оператора from, есть только конкретные, из чего создавать Observable)
 * Используется для создания наблюдаемого из массивов и коллекций. Ниже уже создал наблюдаемое из массива строк.
 * 2)range - возвращает последовательность чисел с каждым onNext(). range - стаический метод класса Observable, на вход принимает
 * 2 Integer-a: 1-й: start - с какого числа начать возвращать последовательность, 2-й - count - сколько чисел вернуть
 * 3)interval(long l, Timeunit unit) - этот метод возвращает последовательность long начиная с нуля с каждым onNext().
 * Метод onComplete() вызовется только на максимально возможном для long значении, так что этот Observable можно условно назвать
 * бесконечным генератором событий. В качестве параметра l указывается значение времени интервала между событиями, в качестве
 * Timeunit - соответсвенно какая временнАя величина будет использоваться: пример в коде.
 * У метода interval есть разные реализации, есть гибрид intervalRange
 * 4)fromCallable() - переводит синхронный код в асинхронный, пример в коде
 * 2. Операторы преобразования: простейший оператор - map: данный оператор определяет преобразование входных данных в выходные
 * Т.е преобразует элементы последовательности, генерируемой в Observable в нужные нам данные. Для этого map использует функцию
 * преобразования. По сути, этот оператор создает новый Observable с новым типом данных поверх старого Observable.
 * Пример: создадим функцию преобразования и 2 Observable в коде:
 * Если, например, мы преобразуем строки в целочисленный тип, то , если строка например не содержит число, то вызовется метод
 * onError() с брошщенным исключением, в этом случае, NumberFormatException
 * Оператор buffer(int buffer) - создает буфер, в который собираются элементы последовательности, которые затем отправляются одним
 * пакетом в виде метода onNext, например:
 * Буфер создается в виде List<T> где тип Т - это тип изначального Observable, метод buffer вернет Observable уже типа
 * Observable<List<T>>
 * 3. Операторы фильтрации: take() - берет определенное количество элементов из последовательности начиная с первого элемента
 * skip() - наоборот, пропускает элементы
 * distinct() - отсеивает дубликаты
 * filter() - фильтрует колбэки от Observable с помощью отдельной функции, которая передается этому оператору
 * 4.Операторы объединения: mergeWith() - объединяет элементы двух Observable в один Observable с общими элементами
 * zip() - объединяет два Observable в один с помощью функции типа <Observable1Type, Observable2Type, ReturnType>
 * Операторы условий: 1)takeUntil(func)
 * берет элементы из последовательности, пока не будет достигнут определенный элемент(работа по типу цикла while)
 * Условие оформляется в виде функции
 * 2)all() - проверяет, удовлетворяют ли все элементы последовательности условию, описанному в функции, если да, то в onNext()
 * возвращается один раз true(если указан тип Boolean)
 * Action - сокращенная версия Observer. Здесь мы можем выбирать, какие колбэки мы будем ловить, например, только onNext.
 * И подписываем, соответсвенно, не Observer, а Action
 *
 *
 */




import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import static java.lang.Integer.parseInt;

public class RXJavaInf {
    Observable<String> observable = Observable.fromArray("one", "two", "three");
    Observable<Integer> observable1 = Observable.range(12,25); /**Этот Observable вернет 25 событий объектами Integer
     начиная с 12*/
    Observable<Long> observable2 = Observable.interval(300, TimeUnit.MICROSECONDS);/**Данное наблюдаемое возвращает
     следующее значение long через каждые 300 микросекунд*/
    Observable<Long> intrangeobservable = Observable.intervalRange(10,30,30,30,TimeUnit.SECONDS);
    /**Этот Observable вернет 20 событий с периодичностью 30 секунд*/
    Observable<Integer> obsint = Observable.fromCallable(new RXJavaInf2());
    /**Это наблюдаемое переводит синхронные методы в асинхронные*/
    Observable<List<Long>> obsbuffer = Observable.interval(200, TimeUnit.MICROSECONDS).buffer(5);
    /**Этот Observable в методе onNext() будет возвращать лист из 5 лонгов с каждым вызовом, т.е [0,1,2,3,4], затем со следующим
     * вызовом [5,6,7,8,9] и т.д. Соответсвенно, Observer , который подписан на этот Observable должен иметь onNext() с
     * возвращаемым типом List<Long>*/
            class RXJavaInf2 implements Callable<Integer>{
                @Override
                public Integer call(){
                    return b("123");
                }
            }
            Observable<Integer> takeobserv = Observable.fromArray(3,4,5,6,7).take(3); /**Это наблюдаемое возьмет первые три элемента*/
    Observer<String> observer = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String o) {

        }
    };
    public void a(){
        observable.subscribe(observer);
    }
    public Integer b(String s) {
        try {
           int d = Integer.parseInt(s);

        } catch (Exception e) {
            Log.e("Error", "Не получилось");
        } return 0;
    }
    Observable<String> mapobserv= Observable.fromArray("1","2","3").map(func()) //Здесь по идее в map придет последовательность
    //из массива строк в операторе fromArray.

    public Integer func(String s){
        @Override
        public void call() {
            Integer d = Integer.parseInt(s);
            return d;
        }
    }
}

