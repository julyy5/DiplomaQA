package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import page.PaymentGate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;
import static data.DataHelper.*;
import static data.SQLHelper.dbCleanData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentGateTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void cleanAllDataDB() {
        dbCleanData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Оплата по валидной карте, проверка всплывающего сообщения")
    public void payValidCardMessage() {
        var PayByCard = new PaymentGate();
        var info = approvedCard();
        PayByCard.sendingData(info);
        $(".notification__title").shouldHave(Condition.exactText("Успешно"), Duration.ofSeconds(25));
        $(".notification__content").shouldHave(Condition.exactText("Операция одобрена Банком."), Duration.ofSeconds(25));
        $(".notification_status_ok").shouldBe(Condition.visible);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата по валидной карте, проверка статуса в бд")
    public void payValidCardStatus() {
        var PayByCard = new PaymentGate();
        var info = approvedCard();
        PayByCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var actualPaymentStatus = SQLHelper.getPaymentStatus();
        var expectedPaymentStatus = "APPROVED";
        assertEquals(expectedPaymentStatus, actualPaymentStatus);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата по валидной карте, проверка соответствия id в бд")
    public void payValidCardId() {
        var PayByCard = new PaymentGate();
        var info = approvedCard();
        PayByCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var expectedTransactionId = SQLHelper.getTransactionId();
        var actualPaymentId = SQLHelper.getPaymentId();
        assertEquals(expectedTransactionId, actualPaymentId);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата по валидной карте, проверка соответствия заявленой на сайте суммы платежа с суммой из БД")
    public void payValidCardAmount() {
        var PayByCard = new PaymentGate();
        var info = approvedCard();
        PayByCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var expectedAmount = "45000";
        var actualAmount = SQLHelper.getAmount();
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    @DisplayName("Оплата невалидной картой, проверка всплывающего сообщения")
    public void payDeclinedCardMessage() {
        var PayByDeclinedCard = new PaymentGate();
        var info = declinedCard();
        PayByDeclinedCard.sendingData(info);
        $(".notification_status_error .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(25));
        $(".notification_status_error .notification__content").shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(25));
        $(".notification_status_error").shouldBe(Condition.visible);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата невалидной картой, проверка статуса в БД")
    public void payDeclinedCardStatus() {
        var PayByDeclinedCard = new PaymentGate();
        var info = declinedCard();
        PayByDeclinedCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var actualPaymentStatus = SQLHelper.getPaymentStatus();
        var expectedPaymentStatus = "DECLINED";
        assertEquals(expectedPaymentStatus, actualPaymentStatus);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата невалидной картой, проверка соответствия id в бд")
    public void payDeclinedCardId() {
        var PayByDeclinedCard = new PaymentGate();
        var info = declinedCard();
        PayByDeclinedCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var expectedTransactionId = SQLHelper.getTransactionId();
        var actualPaymentId = SQLHelper.getPaymentId();
        assertEquals(expectedTransactionId, actualPaymentId);
    }

    @SneakyThrows
    @Test
    @DisplayName("Оплата невалидной картой, проверка соответствия заявленой на сайте суммы платежа с суммой из БД")
    public void payDeclinedCardAmount() {
        var PayByDeclinedCard = new PaymentGate();
        var info = declinedCard();
        PayByDeclinedCard.sendingData(info);
        TimeUnit.SECONDS.sleep(15);
        var expectedAmount = "45000";
        var actualAmount = SQLHelper.getAmount();
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    @DisplayName("Оплата неизвестной картой")
    public void payFailCard() {
        var PayByFailCard = new PaymentGate();
        var info = failedCard();
        PayByFailCard.sendingData(info);
        $(".notification_status_error .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(25));
        $(".notification_status_error .notification__content").shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(25));
        $(".notification_status_error").shouldBe(Condition.visible);
    }

    //Валидация полей
    @Test
    @DisplayName("Неполный номер карты")
    public void payInvalidCardData() {
        var PayByInvalidCardData = new PaymentGate();
        var info = invalidCard();
        PayByInvalidCardData.sendingData(info);
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Оплата просроченной на месяц картой")
    public void payMinusMonthCard() {
        var PayByMinusMonthCard = new PaymentGate();
        var info = minusMonthData();
        PayByMinusMonthCard.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Месяц верхнее граничное значение >12")
    public void payInvalidMonth() {
        var PayByInvalidMonth = new PaymentGate();
        var info = invalidMonthData();
        PayByInvalidMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Месяц нижнее граничное значение '00'")
    public void payNullMonth() {
        var PayByNullMonth = new PaymentGate();
        var info = emptyMonthData();
        PayByNullMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Месяц одной цифрой")
    public void payOneMonth() {
        var PayByOneMonth = new PaymentGate();
        var info = oneMonthData();
        PayByOneMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Прошедший год")
    public void payInvalidYear() {
        var PayByInvalidYear = new PaymentGate();
        var info = invalidYearData();
        PayByInvalidYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Истёк срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Год из будущего")
    public void payNullYear() {
        var PayByNullYear = new PaymentGate();
        var info = plusYearData();
        PayByNullYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Год одной цифрой")
    public void payOneYear() {
        var PayByOneYear = new PaymentGate();
        var info = oneYearData();
        PayByOneYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Символы в поле 'Владелец'")
    public void paySymbolCardHolder() {
        var PayBySymbolCardHolder = new PaymentGate();
        var info = symbolCardHolder();
        PayBySymbolCardHolder.sendingData(info);
        $x("//*[text()=\"Владелец\"]/..//span/input").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Цифры в поле 'Владелец'")
    public void payNumeralCardHolder() {
        var PayByNumeralCardHolder = new PaymentGate();
        var info = numeralCardHolder();
        PayByNumeralCardHolder.sendingData(info);
        $x("//*[text()=\"Владелец\"]/..//span/input").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Кириллица в поле 'Владелец'")
    public void payCyrillicCardHolder() {
        var PayByCyrillicCardHolder = new PaymentGate();
        var info = cyrillicCardHolder();
        PayByCyrillicCardHolder.sendingData(info);
        $x("//*[text()=\"Владелец\"]/..//span/input").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Неполный CVC")
    public void payInvalidCVC() {
        var PayByInvalidCVC = new PaymentGate();
        var info = invalidCVCData();
        PayByInvalidCVC.sendingData(info);
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять все поля")
    public void emptyData() {
        var PayByEmptyData = new PaymentGate();
        PayByEmptyData.sendingEmptyData();
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Владелец']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"), Duration.ofSeconds(15));
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять только поле 'Номер карты'")
    public void emptyCardNumber() {
        var PayByEmptyCardNumber = new PaymentGate();
        var info = approvedCard();
        PayByEmptyCardNumber.sendingEmptyCardNumber(info);
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять только поле 'Месяц'")
    public void emptyMonth() {
        var PayByEmptyMonth = new PaymentGate();
        var info = approvedCard();
        PayByEmptyMonth.sendingEmptyMonth(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять только поле 'Год'")
    public void emptyYear() {
        var PayByEmptyYear = new PaymentGate();
        var info = approvedCard();
        PayByEmptyYear.sendingEmptyYear(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять только поле 'Владелец'")
    public void emptyCardHolder() {
        var PayByEmptyCardHolder = new PaymentGate();
        var info = approvedCard();
        PayByEmptyCardHolder.sendingEmptyCardHolder(info);
        $x("//*[text()='Владелец']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять только поле 'CVC/CVV'")
    public void emptyCVC() {
        var PayByEmptyCVC = new PaymentGate();
        var info = approvedCard();
        PayByEmptyCVC.sendingEmptyCVC(info);
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }


}
