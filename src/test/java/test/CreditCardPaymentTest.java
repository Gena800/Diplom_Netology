package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DbHelper;

import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import page.CardPage;
import page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class CreditCardPaymentTest {
    HomePage mainPage;
    CardPage cardPage;

    @BeforeEach
    void shouldOpenPage() {
        DbHelper.cleanTables();
        mainPage = open("http://localhost:8080", HomePage.class);
        cardPage = mainPage.clickCreditCard();
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
        cardPage.cardData(DataHelper.getApprovedCard());
        cardPage.notificationOk();

        assertEquals("APPROVED", DbHelper.getCreditStatusDB());
        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }


    @Test
    @DisplayName("2.Покупка тура при вводе данных карты со статусом DECLINED")
    public void shouldDeclinedCard() {
        cardPage.cardData(DataHelper.getDeclinedCard());
        cardPage.notificationFailed();

        assertEquals("DECLINED", DbHelper.getCreditCount());
        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("3.Покупка тура при отправке пустых значений в полях формы")
    public void shouldAllFieldsAreEmpty() {
        cardPage.cardData(DataHelper.getAllFieldsAreEmpty());
        cardPage.allFieldsWithErrors();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    //Проверка валидации полей ввода данных по вкладке "Купить в кредит"

    //Поле "Номер карты"

    @Test
    @DisplayName("4.Поле номер карты пустое")
    public void shouldEmptyCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumberEmpty());
        cardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("5.Поле номер карты заполнено значением меньше 16 цифр")
    public void should15DigitsInTheCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumber15Digits());
        cardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("6.Поле номер карты заполнено значением больше 16 цифр")
    public void should17DigitInTheCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumber17Digits());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("7.Поле номер карты заполнено буквами")
    public void shouldLettersDigitInTheCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumberLetters());
        cardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("8.Поле номер карты заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumberSpecialCharacters());
        cardPage.invalidFormatCardNumberField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("9.Поле номер карты заполнено нулевым значением")
    public void shouldZeroDigitInTheCardNumberField() {
        cardPage.cardData(DataHelper.getFieldCardNumberZero());
        cardPage.invalidFormatCardNumberField();
        cardPage.allFieldsWithErrors();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

//    Поле "Месяц"

    @Test
    @DisplayName("10.Поле месяц пустое")
    public void shouldEmptyMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthEmpty());
        cardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("11.Поле месяц заполнено значением меньше 2 цифр")
    public void should1DigitInTheMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthOne());
        cardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("12.Поле месяц заполнено значением больше 2 цифр")
    public void shouldThanTwoDigitInTheMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthMoreThanTwo());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("13.Поле месяц заполнено буквами")
    public void shouldLettersDigitInTheMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthLetters());
        cardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("14.Поле месяц заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthSpecialCharacters());
        cardPage.invalidFormatMonthField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("15.Поле месяц заполнено несуществующим месяцем")
    public void shouldNotExistMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthDoesNotExist());
        cardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("16.Поле месяц заполнено прошлым месяцем текущего года")
    public void shouldPreviousThisYearMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthPreviousThisYear());
        cardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("17.Поле месяц заполнено нулевым значением")
    public void shouldZeroTheMonthField() {
        cardPage.cardData(DataHelper.getFieldMonthZero());
        cardPage.invalidCardExpirationDateMonth();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    //Поле "Год"

    @Test
    @DisplayName("18.Поле год пустое")
    public void shouldEmptyYearField() {
        cardPage.cardData(DataHelper.getFieldYearEmpty());
        cardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("19.Поле год заполнено значением меньше 2 цифр")
    public void should1DigitInTheYearField() {
        cardPage.cardData(DataHelper.get1DigitInTheYearField());
        cardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("20.Поле год заполнено значением больше 2 цифр")
    public void shouldThanTwoDigitInTheYearField() {
        cardPage.cardData(DataHelper.getDigitInTheYearFieldMoreThanTwo());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("21.Поле год заполнено буквами")
    public void shouldLetterDigitInTheYearField() {
        cardPage.cardData(DataHelper.getInTheYearFieldLetter());
        cardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("22.Поле год заполнено спецсимволами")
    public void shouldSpecialCharactersDigitInTheYearField() {
        cardPage.cardData(DataHelper.getDigitInTheYearFieldSpecialCharacters());
        cardPage.invalidFormatYearField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("23.Поле год заполнено нулевым значением")
    public void shouldZeroInTheYearField() {
        cardPage.cardData(DataHelper.getInTheYearFieldZero());
        cardPage.invalidFormatCardExpired();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("24.Поле год заполнено значением предыдущего года")
    public void shouldYearPrecedingTheCurrentOne() {
        cardPage.cardData(DataHelper.getYearBeforeCurrent());
        cardPage.invalidFormatCardExpired();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("25.Поле год заполнено значением отстоящим от текущего более чем на 6 лет")
    public void shouldYear6YearsAheadOfCurrentYear() {
        cardPage.cardData(DataHelper.getExceeds6YearsField());
        cardPage.invalidCardExpirationDateYear();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }


    //Поле "Владелец"

    @Test
    @DisplayName("26.Поле владелец пустое")
    public void shouldEmptyOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerEmpty());
        cardPage.invalidFormatRequiredOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("27.Поле владелец заполнено 1 буквой")
    public void shouldOneLetterOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerOneLetter());
        cardPage.invalidFormatOwnerFieldValueMustContainMoreThanOneLetter();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("28.Поле владелец заполнено значением через пробел, состоящим из двойного имени")
    public void shouldFullNameInTheOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerFullName());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("29.Поле владелец заполнено значением через пробел, состоящим из имени через дефис")
    public void shouldNameSeparatedByAHyphenInTheOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerNameSeparatedByAHyphen());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("30.Поле владелец заполнено значением на кириллице")
    public void shouldInCyrillicOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerInCyrillic());
        cardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("31.Поле владелец заполнено цифрами")
    public void shouldNumbersInTheOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerInNumbers());
        cardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("32.Поле владелец заполнено спецсимволами")
    public void shouldSpecialCharacterOwnerField() {
        cardPage.cardData(DataHelper.getFieldOwnerSpecialCharacter());
        cardPage.invalidFormatOwnerField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    //Поле "CVC/CVV"

    @Test
    @DisplayName("33.Поле CVC пустое")
    public void shouldEmptyCVCField() {
        cardPage.cardData(DataHelper.getFieldCVCEmpty());
        cardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("34.Поле CVC заполнено значением меньше 3 цифр")
    public void shouldLessThan3DigitsCVCField() {
        cardPage.cardData(DataHelper.getCVCFieldLessThan3Digits());
        cardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("35.Поле CVC заполнено значением больше 3 цифр")
    public void shouldMoreThan3DigitsCVCField() {
        cardPage.cardData(DataHelper.getCVCFieldMoreThan3Digits());
        cardPage.notificationOk();

        assertEquals(1, DbHelper.getOrderCount());
        assertEquals(1, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("36.Поле CVC заполнено буквами")
    public void shouldLettersCVCField() {
        cardPage.cardData(DataHelper.getCVCFieldLetters());
        cardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("37.Поле CVC заполнено спецсимволами")
    public void shouldSpecialCharactersCVCField() {
        cardPage.cardData(DataHelper.getCVCFieldSpecialCharacters());
        cardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }

    @Test
    @DisplayName("38.Поле CVC заполнено нулевым значением")
    public void shouldZeroTheCVCField() {
        cardPage.cardData(DataHelper.getCVCFieldZero());
        cardPage.invalidFormatCVCField();

        assertEquals(0, DbHelper.getOrderCount());
        assertEquals(0, DbHelper.getCreditCount());
    }
    @Test
    @DisplayName("39.Покупка тура при вводе данных карты со статусом APPROVED Order_entity credit_id")
    public void shouldApprovedCardAndCheckDbOrder() {
        cardPage.cardData(DataHelper.getApprovedCard());
        cardPage.notificationOk();

        assertEquals("APPROVED", DbHelper.getCreditStatusDB());
        assertEquals(1, DbHelper.getOrderCountCreditId());
        assertEquals(0, DbHelper.getOrderCountPaymentId());
        assertEquals(1, DbHelper.getCreditCount());
    }
}
