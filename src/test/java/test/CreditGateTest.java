package test;

import data.SQLHelper;
import org.junit.jupiter.api.AfterAll;
import page.CreditGate;
import com.codeborne.selenide.Condition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static data.DataHelper.*;
import static data.DataHelper.invalidCVCData;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static data.SQLHelper.dbCleanData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditGateTest {
    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void cleanAllDataDB() {
        dbCleanData();
    }

    @SneakyThrows
    @Test
    @DisplayName("Кредит по валидной карте")
    public void creditValidCard() {
        var PayByCredit = new CreditGate();
        var info = approvedCard();
        PayByCredit.sendingData(info);
        $(".notification__title").shouldHave(Condition.exactText("Успешно"), Duration.ofSeconds(25));
        $(".notification__content").shouldHave(Condition.exactText("Операция одобрена Банком."), Duration.ofSeconds(25));
        $(".notification_status_ok").shouldBe(Condition.visible);
        TimeUnit.SECONDS.sleep(15);
        var actualCreditStatus = SQLHelper.getCreditStatus();
        var expectedCreditStatus = "APPROVED";
        assertEquals(expectedCreditStatus, actualCreditStatus);
        var expectedBankId = SQLHelper.getBankId();
        var actualPaymentId = SQLHelper.getPaymentId();
        assertEquals(expectedBankId, actualPaymentId);
    }

    @SneakyThrows
    @Test
    @DisplayName("Кредит невалидной картой")
    public void payInvalidCard() {
        var PayByDeclinedCredit = new CreditGate();
        var info = declinedCard();
        PayByDeclinedCredit.sendingData(info);
        $(".notification_status_error .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(25));
        $(".notification_status_error .notification__content").shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(25));
        $(".notification_status_error").shouldBe(Condition.visible);
        TimeUnit.SECONDS.sleep(15);
        var actualCreditStatus = SQLHelper.getCreditStatus();
        var expectedCreditStatus = "DECLINED";
        assertEquals(expectedCreditStatus, actualCreditStatus);
        var expectedBankId = SQLHelper.getBankId();
        var actualPaymentId = SQLHelper.getPaymentId();
        assertEquals(expectedBankId, actualPaymentId);
    }

    @Test
    @DisplayName("Оплата неизвестной картой")
    public void payFailCard() {
        var PayByFailCard = new CreditGate();
        var info = failedCard();
        PayByFailCard.sendingData(info);
        $(".notification_status_error .notification__title").shouldHave(Condition.exactText("Ошибка"), Duration.ofSeconds(25));
        $(".notification_status_error .notification__content").shouldHave(Condition.exactText("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(25));
        $(".notification_status_error").shouldBe(Condition.visible);
    }

    //Валидация палей
    @Test
    @DisplayName("Неполный номер карты")
    public void payInvalidCardData() {
        var PayInvalidCardData = new CreditGate();
        var info = invalidCard();
        PayInvalidCardData.sendingData(info);
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Несуществующий месяц")
    public void payInvalidMonth() {
        var PayByInvalidMonth = new CreditGate();
        var info = invalidMonthData();
        PayByInvalidMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Несуществующий месяц '00'")
    public void payNullMonth() {
        var PayByNullMonth = new CreditGate();
        var info = emptyMonthData();
        PayByNullMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Месяц одной цифрой")
    public void payOneMonth() {
        var PayByNullMonth = new CreditGate();
        var info = oneMonthData();
        PayByNullMonth.sendingData(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Прошедший год")
    public void payInvalidYear() {
        var PayByInvalidYear = new CreditGate();
        var info = invalidYearData();
        PayByInvalidYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Истёк срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Год из будущего")
    public void payNullYear() {
        var PayByNullYear = new CreditGate();
        var info = plusYearData();
        PayByNullYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Год одной цифрой")
    public void payOneYear() {
        var PayByNullYear = new CreditGate();
        var info = oneYearData();
        PayByNullYear.sendingData(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Неполный CVC")
    public void payInvalidCVC() {
        var PayByInvalidCVC = new CreditGate();
        var info = invalidCVCData();
        PayByInvalidCVC.sendingData(info);
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Символы в поле 'Владелец'")
    public void payInvalidCardHolder() {
        var PayByNullYear = new CreditGate();
        var info = invalidCardHolder();
        PayByNullYear.sendingData(info);
        $x("//*[text()=\"Владелец\"]/..//span/input").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять все поля")
    public void emptyData() {
        var PayEmptyData = new CreditGate();
        PayEmptyData.sendingEmptyData();
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
        $x("//*[text()='Владелец']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"), Duration.ofSeconds(15));
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять поле 'Номер карты'")
    public void emptyCardNumber() {
        var PayByEmptyCardNumber = new CreditGate();
        var info = approvedCard();
        PayByEmptyCardNumber.sendingEmptyCardNumber(info);
        $x("//*[text()='Номер карты']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять поле 'Месяц'")
    public void emptyMonth() {
        var PayByEmptyMonth = new CreditGate();
        var info = approvedCard();
        PayByEmptyMonth.sendingEmptyMonth(info);
        $x("//*[text()='Месяц']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять поле 'Год'")
    public void emptyYear() {
        var PayByEmptyYear = new CreditGate();
        var info = approvedCard();
        PayByEmptyYear.sendingEmptyYear(info);
        $x("//*[text()='Год']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять поле 'Владелец'")
    public void emptyCardHolder() {
        var PayByEmptyCardHolder = new CreditGate();
        var info = approvedCard();
        PayByEmptyCardHolder.sendingEmptyCardHolder(info);
        $x("//*[text()='Владелец']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"), Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Не заполнять поле 'CVC/CVV'")
    public void emptyCVC() {
        var PayByEmptyCVC = new CreditGate();
        var info = approvedCard();
        PayByEmptyCVC.sendingEmptyCVC(info);
        $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']").shouldHave(Condition.exactText("Неверный формат"), Duration.ofSeconds(15));
    }
}
