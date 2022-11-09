package ru.job4j.assertj;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class NameLoadTest {

    @Test
    void checkEmpty() {
        NameLoad nameLoad = new NameLoad();
        assertThatThrownBy(nameLoad::getMap)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("no data");
    }

    @Test
    void checkEmptyName() {
        NameLoad nameLoad = new NameLoad();
        assertThatThrownBy(nameLoad::parse)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("empty");
    }

    @Test
    void checkWithoutSymbolEqual() {
        NameLoad nameLoad = new NameLoad();
        String[] names = {"one", "two"};
        assertThatThrownBy(() -> nameLoad.parse(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining(names[0])
                .hasMessageContaining("=");
    }

    @Test
    void checkStartsWithEqual() {
        NameLoad nameLoad = new NameLoad();
        String[] names = {"=a"};
        assertThatThrownBy(() -> nameLoad.parse(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining(names[0])
                .hasMessageContaining("=a")
                .hasMessageContaining("key");
    }
    @Test
    void checkFinishesWithEqual() {
        NameLoad nameLoad = new NameLoad();
        String[] names = {"a="};
        assertThatThrownBy(() -> nameLoad.parse(names))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageMatching("^.+")
                .hasMessageContaining(names[0])
                .hasMessageContaining("a=")
                .hasMessageContaining("value");
    }

}