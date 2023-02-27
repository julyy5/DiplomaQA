package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String month;
        public String year;
        public String cardHolder;
        public String cvc;
    }

    //APPROVED card data
    private static final String validCard = "4444 4444 4444 4441";

    private static String validMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    private static String validYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String validCardHolder() {
        Faker faker = new Faker(new Locale("EN"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String validCVC() {
        int min = 100;
        int max = 999;
        int a = (int) (Math.random() * ((max - min) + 1)) + min;
        return String.valueOf(a);
    }

    public static CardInfo approvedCard() {
        return new CardInfo(validCard, validMonth(), validYear(), validCardHolder(), validCVC());
    }

    //DECLINED card data
    private static final String invalidCard = "4444 4444 4444 4442";

    public static CardInfo declinedCard() {
        return new CardInfo(invalidCard, validMonth(), validYear(), validCardHolder(), validCVC());
    }


    //Неизвестный номер карты
    private static final String failCardData = "4444 4444 4444 4444";

    public static CardInfo failedCard() {
        return new CardInfo(failCardData, validMonth(), validYear(), validCardHolder(), validCVC());
    }

    //Неполный номер карты
    private static final String invalidCardData = "4444 4444 4444 444";

    public static CardInfo invalidCard() {
        return new CardInfo(invalidCardData, validMonth(), validYear(), validCardHolder(), validCVC());
    }

    //Один месяц назад
    private static String minusMonth() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static CardInfo minusMonthData() {
        return new CardInfo(validCard, minusMonth(), validYear(), validCardHolder(), validCVC());
    }

    //Несуществующий месяц
    private static String invalidMonth = "13";

    public static CardInfo invalidMonthData() {
        return new CardInfo(validCard, invalidMonth, validYear(), validCardHolder(), validCVC());
    }

    //Месяц "00"
    private static String emptyMonth = "00";

    public static CardInfo emptyMonthData() {
        return new CardInfo(validCard, emptyMonth, validYear(), validCardHolder(), validCVC());
    }

    //Месяц одной цифрой
    private static String oneMonth = "1";

    public static CardInfo oneMonthData() {
        return new CardInfo(validCard, oneMonth, validYear(), validCardHolder(), validCVC());
    }

    //Прошедший год
    private static String invalidYear() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static CardInfo invalidYearData() {
        return new CardInfo(validCard, validMonth(), invalidYear(), validCardHolder(), validCVC());
    }

    //Год в будущем
    private static String plusYear() {
        return LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static CardInfo plusYearData() {
        return new CardInfo(validCard, validMonth(), plusYear(), validCardHolder(), validCVC());
    }

    //Год одной цифрой
    private static String oneYear = "5";

    public static CardInfo oneYearData() {
        return new CardInfo(validCard, validMonth(), oneYear, validCardHolder(), validCVC());
    }

    //Неполный CVC
    public static String invalidCVC() {
        int min = 10;
        int max = 99;
        int a = (int) (Math.random() * ((max - min) + 1)) + min;
        return String.valueOf(a);
    }

    public static CardInfo invalidCVCData() {
        return new CardInfo(validCard, validMonth(), validYear(), validCardHolder(), invalidCVC());
    }

    //Данные для валидации поля "Владелец"
    private static String symbol = "!@#$%^&+=";

    public static CardInfo symbolCardHolder() {
        return new CardInfo(validCard, validMonth(), validYear(), symbol, validCVC());
    }

    private static String numeral = "1234567890";

    public static CardInfo numeralCardHolder() {
        return new CardInfo(validCard, validMonth(), validYear(), numeral, validCVC());
    }

    private static String cyrillic() {
        Faker faker = new Faker(new Locale("RU"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static CardInfo cyrillicCardHolder() {
        return new CardInfo(validCard, validMonth(), validYear(), cyrillic(), validCVC());
    }

}


