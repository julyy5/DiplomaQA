package page;

import data.DataHelper;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.*;

public class PaymentGate {
    public void sendingData(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"0000 0000 0000 0000\"]").setValue(info.cardNumber);
        $x("//input[@placeholder=\"08\"]").setValue(info.month);
        $x("//input[@placeholder=\"22\"]").setValue(info.year);
        $x("//*[text()=\"Владелец\"]/..//span/input").setValue(info.cardHolder);
        $x("//input[@placeholder=\"999\"]").setValue(info.cvc);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyData() {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyCardNumber(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"08\"]").setValue(info.month);
        $x("//input[@placeholder=\"22\"]").setValue(info.year);
        $x("//*[text()=\"Владелец\"]/..//span/input").setValue(info.cardHolder);
        $x("//input[@placeholder=\"999\"]").setValue(info.cvc);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyMonth(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"0000 0000 0000 0000\"]").setValue(info.cardNumber);
        $x("//input[@placeholder=\"22\"]").setValue(info.year);
        $x("//*[text()=\"Владелец\"]/..//span/input").setValue(info.cardHolder);
        $x("//input[@placeholder=\"999\"]").setValue(info.cvc);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyYear(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"0000 0000 0000 0000\"]").setValue(info.cardNumber);
        $x("//input[@placeholder=\"08\"]").setValue(info.month);
        $x("//*[text()=\"Владелец\"]/..//span/input").setValue(info.cardHolder);
        $x("//input[@placeholder=\"999\"]").setValue(info.cvc);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyCardHolder(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"0000 0000 0000 0000\"]").setValue(info.cardNumber);
        $x("//input[@placeholder=\"08\"]").setValue(info.month);
        $x("//input[@placeholder=\"22\"]").setValue(info.year);
        $x("//input[@placeholder=\"999\"]").setValue(info.cvc);
        $x("//span[(text() = \"Продолжить\")]").click();
    }

    public void sendingEmptyCVC(DataHelper.CardInfo info) {
        $x("//span[(text() = \"Купить\")]").click();
        $x("//*[(text() = \"Оплата по карте\")]").shouldBe(Condition.visible);
        $x("//input[@placeholder=\"0000 0000 0000 0000\"]").setValue(info.cardNumber);
        $x("//input[@placeholder=\"08\"]").setValue(info.month);
        $x("//input[@placeholder=\"22\"]").setValue(info.year);
        $x("//*[text()=\"Владелец\"]/..//span/input").setValue(info.cardHolder);
        $x("//span[(text() = \"Продолжить\")]").click();
    }


}
