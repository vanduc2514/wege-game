package game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases for parsing arguments")
class WegeCLITest {

    @Test
    @DisplayName("Test validate argument with empty values")
    void shouldValidate_withEmptyArguments() {
        List<String> fakeEmtpyValues = List.of();
        boolean actual = WegeCLI.validateApplicationArguments(fakeEmtpyValues);
        assertTrue(actual);
    }

    @Test
    @DisplayName("Test validate argument with positive single digit numbers")
    void shouldValidate_withPositiveSingleDigitNumbers() {
        List<String> fakePositiveSingleDigitNumbers = List.of("1", "1", "9");
        boolean actual = WegeCLI.validateApplicationArguments(fakePositiveSingleDigitNumbers);
        assertTrue(actual);
    }

    @Test
    @DisplayName("Test validate argument contain negative single digit number")
    void shouldInvalidate_containNegativeSingleDigitNumber() {
        List<String> fakeContainNegativeNumber = List.of("1", "-1", "9");
        boolean actual = WegeCLI.validateApplicationArguments(fakeContainNegativeNumber);
        assertFalse(actual);
    }

    @Test
    @DisplayName("Test validate argument contain positive multiple digit number")
    void shouldInvalidate_containMultipleDigitNumber() {
        List<String> fakeContainMultipleDigit = List.of("1", "99", "99");
        boolean actual = WegeCLI.validateApplicationArguments(fakeContainMultipleDigit);
        assertFalse(actual);
    }

    @Test
    @DisplayName("Test validate argument contain 0 number")
    void shouldInvalidate_contain0number() {
        List<String> fakeContainMultipleDigit = List.of("0", "1", "1");
        boolean actual = WegeCLI.validateApplicationArguments(fakeContainMultipleDigit);
        assertFalse(actual);
    }
    
    @Test
    @DisplayName("Test validate argument contain non number value ")
    void shouldInvalidate_containNonNumberValue() {
        List<String> fakeContainNonNumberValue = List.of("a", "1", "1");
        boolean actual = WegeCLI.validateApplicationArguments(fakeContainNonNumberValue);
        assertFalse(actual);
    }

    @Test
    @DisplayName("Test parse argument with empty values")
    void shouldParseArgument_withEmptyValues() {
        List<String> fakeEmptyValues = List.of();
        List<Integer> actual = WegeCLI.parseApplicationArguments(fakeEmptyValues);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Test parse argument with values")
    void shouldParseArgument_withValues() {
        List<String> fakeValues = List.of("1", "2", "3");
        List<Integer> actual = WegeCLI.parseApplicationArguments(fakeValues);
        assertEquals(fakeValues.size(), actual.size());
    }

    @Test
    @DisplayName("Test parse argument contain non number value")
    void shouldThrowException_whenParseContainNonNumberValue() {
        List<String> fakeContainNonNumberValue = List.of("a", "1", "2");
        assertThrows(Exception.class, () -> WegeCLI.parseApplicationArguments(fakeContainNonNumberValue));
    }

    @Test
    @DisplayName("Test parse game argument with no argument provided")
    void shouldParse_withNoArgument() {
        List<Integer> fakeNoArgument = List.of();
        WegeGameSetting gameSetting = WegeCLI.parseGameArguments(fakeNoArgument);
        assertNotNull(gameSetting);
    }

    @Test
    @DisplayName("Test parse game argument with 1 argument provided")
    void shouldParse_with1Argument() {
        List<Integer> fakeNoArgument = List.of(3);
        WegeGameSetting gameSetting = WegeCLI.parseGameArguments(fakeNoArgument);
        assertNotNull(gameSetting);
    }

    @Test
    @DisplayName("Test parse game argument with more than 1 argument provided")
    void shouldParse_with2Argument() {
        List<Integer> fakeNoArgument = List.of(1, 2, 3);
        WegeGameSetting gameSetting = WegeCLI.parseGameArguments(fakeNoArgument);
        assertNotNull(gameSetting);
    }

}