package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DbHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.CardPage;
import page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DebitCardPaymentTest {
    HomePage mainPage;
    CardPage CardPage;

    @BeforeEach
    void shouldOpenPage() {
        DbHelper.cleanTables();
        mainPage = open("http://localhost:8080", HomePage.class);
        CardPage = mainPage.clickDebitCard();
    }

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    //Покупка тура через вкладку "Купить в кредит"
    @Test
    @DisplayName("1.Покупка тура при вводе данных карты со статусом APPROVED")
    public void shouldApprovedCard() {
        CardPage.cardData(DataHelper.getApprovedCard());
        CardPage.notificationOk();

        assertEquals("APPROVED", DbHelper.getPaymentStatusDB());
        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }


    @Test
    @DisplayName("2.Покупка тура при вводе данных карты со статусом DECLINED")
    public void shouldDeclinedCard() {
        CardPage.cardData(DataHelper.getDeclinedCard());
        CardPage.notificationFailed();

        assertEquals("DECLINED", DbHelper.getPaymentStatusDB());
        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("3.Покупка тура при отправке пустых значений в полях формы")
    public void shouldAllFieldsAreEmpty() {
        CardPage.cardData(DataHelper.getAllFieldsAreEmpty());
        CardPage.allFieldsWithErrors();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    //Проверка валидации полей ввода данных по вкладке "Купить в кредит"

    //Поле "Номер карты"

    @Test
    @DisplayName("4.Поле номер карты пустое")
    public void shouldEmptyCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumberEmpty());
        CardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("5.Поле номер карты заполнено значением меньше 16 цифр")
    public void should15DigitsInTheCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumber15Digits());
        CardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("6.Поле номер карты заполнено значением больше 16 цифр")
    public void should17DigitInTheCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumber17Digits());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("7.Поле номер карты заполнено буквами")
    public void shouldLettersDigitInTheCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumberLetters());
        CardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("8.Поле номер карты заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumberSpecialCharacters());
        CardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("9.Поле номер карты заполнено нулевым значением")
    public void shouldZeroDigitInTheCardNumberField() {
        CardPage.cardData(DataHelper.getFieldCardNumberZero());
        CardPage.invalidFormatCardNumberField();
        CardPage.allFieldsWithErrors();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

//    Поле "Месяц"

    @Test
    @DisplayName("10.Поле месяц пустое")
    public void shouldEmptyMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthEmpty());
        CardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("11.Поле месяц заполнено значением меньше 2 цифр")
    public void should1DigitInTheMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthOne());
        CardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("12.Поле месяц заполнено значением больше 2 цифр")
    public void shouldThanTwoDigitInTheMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthMoreThanTwo());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("13.Поле месяц заполнено буквами")
    public void shouldLettersDigitInTheMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthLetters());
        CardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("14.Поле месяц заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthSpecialCharacters());
        CardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("15.Поле месяц заполнено несуществующим месяцем")
    public void shouldNotExistMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthDoesNotExist());
        CardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("16.Поле месяц заполнено прошлым месяцем текущего года")
    public void shouldPreviousThisYearMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthPreviousThisYear());
        CardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("17.Поле месяц заполнено нулевым значением")
    public void shouldZeroTheMonthField() {
        CardPage.cardData(DataHelper.getFieldMonthZero());
        CardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    //Поле "Год"

    @Test
    @DisplayName("18.Поле год пустое")
    public void shouldEmptyYearField() {
        CardPage.cardData(DataHelper.getFieldYearEmpty());
        CardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("19.Поле год заполнено значением меньше 2 цифр")
    public void should1DigitInTheYearField() {
        CardPage.cardData(DataHelper.get1DigitInTheYearField());
        CardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("20.Поле год заполнено значением больше 2 цифр")
    public void shouldThanTwoDigitInTheYearField() {
        CardPage.cardData(DataHelper.getDigitInTheYearFieldMoreThanTwo());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("21.Поле год заполнено буквами")
    public void shouldLetterDigitInTheYearField() {
        CardPage.cardData(DataHelper.getInTheYearFieldLetter());
        CardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("22.Поле год заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheYearField() {
        CardPage.cardData(DataHelper.getDigitInTheYearFieldSpecialCharacters());
        CardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("23.Поле год заполнено нулевым значением")
    public void shouldZeroInTheYearField() {
        CardPage.cardData(DataHelper.getInTheYearFieldZero());
        CardPage.invalidFormatCardExpired();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("24.Поле год заполнено значением предыдущего года")
    public void shouldYearPrecedingTheCurrentOne() {
        CardPage.cardData(DataHelper.getYearBeforeCurrent());
        CardPage.invalidFormatCardExpired();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("25.Поле год заполнено значением отстоящим от текущего более чем на 6 лет")
    public void shouldYear6YearsAheadOfCurrentYear() {
        CardPage.cardData(DataHelper.getExceeds6YearsField());
        CardPage.invalidCardExpirationDateYear();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }


    //Поле "Владелец"

    @Test
    @DisplayName("26.Поле владелец пустое")
    public void shouldEmptyOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerEmpty());
        CardPage.invalidFormatRequiredOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("27.Поле владелец заполнено 1 буквой")
    public void shouldOneLetterOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerOneLetter());
        CardPage.invalidFormatOwnerFieldValueMustContainMoreThanOneLetter();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("28.Поле владелец заполнено значением через пробел, состоящим из двойного имени")
    public void shouldFullNameInTheOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerFullName());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("29.Поле владелец заполнено значением через пробел, состоящим из имени через дефис")
    public void shouldNameSeparatedByAHyphenInTheOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerNameSeparatedByAHyphen());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("30.Поле владелец заполнено значением на кириллице")
    public void shouldInCyrillicOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerInCyrillic());
        CardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("31.Поле владелец заполнено цифрами")
    public void shouldNumbersInTheOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerInNumbers());
        CardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("32.Поле владелец заполнено спецсимволами")
    public void shouldSpecialCharacterOwnerField() {
        CardPage.cardData(DataHelper.getFieldOwnerSpecialCharacter());
        CardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    //Поле "CVC/CVV"

    @Test
    @DisplayName("33.Поле CVC пустое")
    public void shouldEmptyCVCField() {
        CardPage.cardData(DataHelper.getFieldCVCEmpty());
        CardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("34.Поле CVC заполнено значением меньше 3 цифр")
    public void shouldLessThan3DigitsCVCField() {
        CardPage.cardData(DataHelper.getCVCFieldLessThan3Digits());
        CardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("35.Поле CVC заполнено значением больше 3 цифр")
    public void shouldMoreThan3DigitsCVCField() {
        CardPage.cardData(DataHelper.getCVCFieldMoreThan3Digits());
        CardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("36.Поле CVC заполнено буквами")
    public void shouldLettersCVCField() {
        CardPage.cardData(DataHelper.getCVCFieldLetters());
        CardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("37.Поле CVC заполнено спецсимволами")
    public void shouldSpecialCharactersCVCField() {
        CardPage.cardData(DataHelper.getCVCFieldSpecialCharacters());
        CardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }

    @Test
    @DisplayName("38.Поле CVC заполнено нулевым значением")
    public void shouldZeroTheCVCField() {
        CardPage.cardData(DataHelper.getCVCFieldZero());
        CardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getPaymentCount());
    }
    @Test
    @DisplayName("39.Покупка тура при вводе данных карты со статусом APPROVED Order_entity payment_id")
    public void shouldApprovedCardAndCheckDbOrder() {
        CardPage.cardData(DataHelper.getApprovedCard());
        CardPage.notificationOk();

        assertEquals("APPROVED", DbHelper.getPaymentStatusDB());
        assertEquals(1, DbHelper.getOrderCountPaymentId());
        assertEquals(0, DbHelper.getOrderCountCreditId());
        assertEquals(1, DbHelper.getPaymentCount());
    }
}
