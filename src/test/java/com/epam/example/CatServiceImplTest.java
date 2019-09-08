package com.epam.example;

import com.epam.example.impl.CatServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CatServiceImplTest {

    @Mock
    private CatService catService;

    @InjectMocks
    CatServiceImpl cats = new CatServiceImpl(catService);

    @Test
    public void testAdd() {
        // определяем поведение калькулятора
        when(cats.add(20.0, 30.0)).thenReturn(50.0);

        // проверяем действие сложения
        assertEquals(cats.add(20, 30), 50.0, 0);

        // проверям выполнение действия
        verify(catService).add(20.0, 30.0);

        // определение поведения с использованием doReturn
        doReturn(15.0).when(catService).add(10.0, 5.0);

        // проверяем действие сложения
        assertEquals(catService.add(10.0, 5.0), 15.0, 0);
        verify(catService).add(10.0, 5.0);
    }

    /*
    *   Для проверки количества вызовов определенных методов Mockito предоставляет следующие методы :
        atLeast (int min) - не меньше min вызовов;
        atLeastOnce () - хотя бы один вызов;
        atMost (int max) - не более max вызовов;
        times (int cnt) - cnt вызовов;
        never () - вызовов не было;
    * */
    @Test
    public void testCallMethod() {
        // определяем поведение (результаты)
        when(catService.subtract(15.0, 25.0)).thenReturn(10.0);
        when(catService.subtract(35.0, 25.0)).thenReturn(-10.0);

        // вызов метода subtract и проверка результата
        assertEquals(catService.subtract(15.0, 25.0), 10, 0);
        assertEquals(catService.subtract(15.0, 25.0), 10, 0);

        assertEquals(catService.subtract(35.0, 25.0), -10, 0);

        // проверка вызова методов
        verify(catService, atLeastOnce()).subtract(35.0, 25.0);
        verify(catService, atLeast(2)).subtract(15.0, 25.0);

        // проверка - был ли метод вызван 2 раза?
        verify(catService, times(2)).subtract(15.0, 25.0);
        // проверка - метод не был вызван ни разу
        verify(catService, never()).divide(10.0, 20.0);
    }

    @Test(expected = TooFewActualInvocations.class)
    public void testCallMethodWithException() {
        // определяем поведение (результаты)
        when(catService.subtract(15.0, 25.0)).thenReturn(10.0);
        when(catService.subtract(35.0, 25.0)).thenReturn(-10.0);

        // вызов метода subtract и проверка результата
        assertEquals(catService.subtract(15.0, 25.0), 10, 0);
        assertEquals(catService.subtract(15.0, 25.0), 10, 0);

        assertEquals(catService.subtract(35.0, 25.0), -10, 0);

        // проверка вызова методов
        verify(catService, atLeastOnce()).subtract(35.0, 25.0);
        verify(catService, atLeast(2)).subtract(15.0, 25.0);

        // проверка - был ли метод вызван 2 раза?
        verify(catService, times(2)).subtract(15.0, 25.0);
        // проверка - метод не был вызван ни разу
        verify(catService, never()).divide(10.0, 20.0);

        /* Если снять комментарий со следующей проверки, то
         * ожидается exception, поскольку метод "subtract"
         * с параметрами (35.0, 25.0) был вызван 1 раз
         */
        verify(catService, atLeast(2)).subtract(35.0, 25.0);

        /* Если снять комментарий со следующей проверки, то
         * ожидается exception, поскольку метод "subtract"
         * с параметрами (15.0, 25.0) был вызван 2 раза, а
         * ожидался всего один вызов
         */
        verify(catService, atMost(1)).subtract(15.0, 25.0);
    }

    @Test(expected = RuntimeException.class)
    public void generateExceptionWithMockito() {
        when(catService.divide(15.0, 3)).thenReturn(5.0);

        assertEquals(catService.divide(15.0, 3), 5.0, 0);
        // проверка вызова метода
        verify(catService).divide(15.0, 3);

        // создаем исключение
        RuntimeException exception = new RuntimeException("Division by zero");
        // определяем поведение
        doThrow(exception).when(catService).divide(15.0, 0);

        assertEquals(catService.divide(15.0, 0), 0.0, 0);
        verify(catService).divide(15.0, 0);
    }

    @Test
    public void testThenAnswer() {
        // определение поведения mock для метода с параметрами
        when(catService.add(11.0, 12.0)).thenAnswer(answer);
        assertEquals(catService.add(11.0, 12.0), 23.0, 0);
    }

    /*
     *Иногда описание поведения mock объекта требует определенной проверки с усложнением логики.
     *  В этом случае можно использовать интерфейс Answer<T>,
     *  который позволяет реализовать заглушки методов со сложным поведением.
     *  В следующем тесте testThenAnswer при вызове метода сложения с определенными параметрами
     *  catService.add(11.0, 12.0) будет вызван метод answer, который подготовит ответ.
     *  Параметр InvocationOnMock позволяет получить информацию о вызываемом методе и параметрах.
     *
     * */
    // метод обработки ответа
    private Answer<Double> answer = new Answer<Double>() {
        public Double answer(InvocationOnMock invocation) {
            // получение объекта mock
            Object mock = invocation.getMock();
            System.out.println("mock object : " + mock.toString());

            // аргументы метода, переданные mock
            Object[] args = invocation.getArguments();
            double d1 = (Double) args[0];
            double d2 = (Double) args[1];
            double d3 = d1 + d2;
            System.out.println("" + d1 + " + " + d2);

            return d3;
        }
    };

    /*
     * Mockito позволяет подключать к реальным объектам «шпиона» spy, контролировать возвращаемые методами значения
     * и отслеживать количество вызовов методов. В следующем тесте создадим шпиона scalc, который подключим к
     * реальному калькулятору и будем вызывать метод double15(). Необходимо отметить, что метод реального объекта
     * double15 должен вернуть значение 15.
     * Однако Mockito позволяет переопределить значение и согласно вновь назначенному условию
     * новое значение должно быть 23.
     * */
    @Test
    public void testSpy() {
        CatServiceImpl scalc = spy(new CatServiceImpl(catService));
        when(scalc.double15()).thenReturn(23.0);

        // вызов метода реального класса
        scalc.double15();
        // проверка вызова метода
        verify(scalc).double15();

        // проверка возвращаемого методом значения
        assertEquals(23.0, scalc.double15(), 0);
        // проверка вызова метода не менее 2-х раз
        verify(scalc, atLeast(2)).double15();
    }

    /*
     * Фреймворк Mockito позволяет выполнить проверку вызова определенного метода в течение заданного в timeout времени.
     * Задержка времени определяется в милисекундах.
     * */
    @Test
    public void testTimout() {
        // определение результирующего значения mock для метода
        when(catService.add(11.0, 12.0)).thenReturn(23.0);
        // проверка значения
        assertEquals(catService.add(11.0, 12.0), 23.0, 0);

        // проверка вызова метода в течение 10 мс
        verify(catService, timeout(100)).add(11.0, 12.0);
    }

    /*
     * В следующем тесте при создании mock объектов используются java классы Iterator и Comparable.
     * После этого определяются условия проверок и выполняются тесты.
     * */
    @Test
    public void testJavaClasses() {
        // создание объекта mock
        Iterator<String> mis = mock(Iterator.class);
        // формирование ответов
        when(mis.next()).thenReturn("Привет").thenReturn("Mockito");
        // формируем строку из ответов
        String result = mis.next() + ", " + mis.next();
        // проверяем
        assertEquals("Привет, Mockito", result);

        Comparable<String> mcs = mock(Comparable.class);
        when(mcs.compareTo("Mockito")).thenReturn(1);
        assertEquals(1, mcs.compareTo("Mockito"));

        Comparable<Integer> mci = mock(Comparable.class);
        when(mci.compareTo(anyInt())).thenReturn(1);
        assertEquals(1, mci.compareTo(5));
    }
}
