package store;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;


public class WarehouseElementTest {

    private static Stream<Arguments> provideStringsfoorParamsFromString() {
        return Stream.of(
            Arguments.of("test", new ArrayList<String>() {{
                add("test");
            }}),
            Arguments.of("test1|test2|(bruuuuh| )", new ArrayList<String>() {{
                add("test1");
                add("test2");
                add("(bruuuuh| )");
            }}),
            Arguments.of("(test1|test2|(bruuuuh|frfr)|test3)", new ArrayList<String>() {{
                add("test1");
                add("test2");
                add("(bruuuuh|frfr)");
                add("test3");
            }}),
            Arguments.of("test1|test2|(bruuuuh|(frfr|wthevenisthis))|test3", new ArrayList<String>() {{
                add("test1");
                add("test2");
                add("(bruuuuh|(frfr|wthevenisthis))");
                add("test3");
            }})
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideStringsfoorParamsFromString")
    void testParamsFromString(String inputString, ArrayList<String> expectedParams) {
        ArrayList<String> returnedParams = WarehouseElement.paramsFromString(inputString);
        assertTrue(expectedParams.equals(returnedParams));
    }

    public static void main(String[] args) {
        WarehouseElementTest test = new WarehouseElementTest();
        test.testParamsFromString("test1|test2|(bruuuuh|frfr)", new ArrayList<String>() {{
            add("test1");
            add("test2");
            add("(bruuuuh|frfr)");
        }});
    }

}
