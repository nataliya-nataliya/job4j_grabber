package ru.job4j.assertj;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SimpleConvertTest {
    @Test
    void checkGeneralArray() {
        SimpleConvert simpleConvert = new SimpleConvert();
        String[] array = simpleConvert.toArray("one", "two", "three", "four", "five");
        assertThat(array).hasSize(5)
                .contains("one")
                .contains("one", Index.atIndex(0))
                .containsAnyOf("zero", "two", "six")
                .doesNotContain("one", Index.atIndex(1));
    }

    @Test
    void checkSatisfyArray() {
        SimpleConvert simpleConvert = new SimpleConvert();
        String[] array = simpleConvert.toArray("one", "two", "three", "four", "five");
        assertThat(array).isNotNull()
                .allSatisfy(e -> {
                    assertThat(e.length()).isLessThan(8);
                    assertThat(e.length()).isGreaterThan(2);
                })
                .anySatisfy(e -> assertThat(e).startsWith("o"));
    }

    @Test
    void checkGeneralList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        List<String> list = simpleConvert.toList("one", "one", "two", "three", "four", "five");
        assertThat(list).hasSize(6)
                .contains("one")
                .containsOnly("one", "two", "three", "four", "five")
                .containsAnyOf("twenty", "one")
                .startsWith("one")
                .endsWith("five")
                .containsSequence("two", "three");
    }

    @Test
    void checkElementsOfList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        List<String> list = simpleConvert.toList("one", "two", "three", "four", "five");
        assertThat(list).isNotNull()
                .allSatisfy(e -> {
                    assertThat(e.length()).isLessThan(6);
                    assertThat(e.length()).isGreaterThan(2);
                })
                .anySatisfy(e -> {
                    assertThat(e).startsWith("o");
                })
                .anyMatch(e -> e.length() == 3);
    }
    @Test
    void checkNavigationList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        List<String> list = simpleConvert.toList("one", "two", "three", "four", "five");
        assertThat(list).first().isEqualTo("one");
        assertThat(list).element(0).isNotNull()
                .isEqualTo("one");
        assertThat(list).last().isNotNull()
                .isEqualTo("five");
    }

    @Test
    void checkFilteredList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        List<String> list = simpleConvert.toList("one", "two", "three", "four", "five");
        assertThat(list).filteredOn(e -> e.length() == 4).first().isEqualTo("four");
        assertThat(list).filteredOnAssertions(e -> assertThat(e).startsWith("t"))
                .hasSize(2)
                .last().isEqualTo("three");
    }

    @Test
    void checkGeneralSetList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        Set<String> list = simpleConvert.toSet("one", "one", "two", "two", "three", "four", "five");
        assertThat(list).hasSize(5)
                .contains("one")
                .containsOnly("one", "two", "three", "four", "five")
                .containsAnyOf("twenty", "one")
                .containsSequence("two", "three");
    }

    @Test
    void checkFilteredSetList() {
        SimpleConvert simpleConvert = new SimpleConvert();
        Set<String> list = simpleConvert.toSet("one", "one", "two", "two", "three", "four", "five");
        assertThat(list).filteredOn(e -> e.length() == 3).hasSize(2);
        assertThat(list).filteredOnAssertions(e -> assertThat(e).startsWith("t"))
                .hasSize(2);
    }
    @Test
    void assertMap() {
        SimpleConvert simpleConvert = new SimpleConvert();
        Map<String, Integer> map = simpleConvert.toMap(
             "1", "2", "3");
        assertThat(map).hasSize(3)
                .containsKeys("1", "2", "3")
                .containsValues(0, 1, 2)
                .doesNotContainKey("0")
                .doesNotContainValue(3)
                .containsEntry("2", 1);
    }
}