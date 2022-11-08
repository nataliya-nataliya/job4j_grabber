package ru.job4j.assertj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BoxTest {
    @Test
    void isThisSphere() {
        Box box = new Box(0, 10);
        String name = box.whatsThis();
        assertThat(name).isEqualTo("Sphere");
    }

    @Test
    void isTetrahedron() {
        Box box = new Box(4, 10);
        String name = box.whatsThis();
        assertThat(name).isEqualTo("Tetrahedron")
                .startsWith("T");
    }

    @Test
    void isCube() {
        Box box = new Box(8, 10);
        String name = box.whatsThis();
        assertThat(name).isEqualTo("Cube")
                .doesNotContainAnyWhitespaces();
    }

    @Test
    void when8vertexes() {
        Box box = new Box(8, 1);
        int number = box.getNumberOfVertices();
        assertThat(number).isEqualTo(8);
    }

    @Test
    void when2vertexes() {
        Box box = new Box(2, 0);
        int number = box.getNumberOfVertices();
        assertThat(number).isEqualTo(-1);
    }

    @Test
    void  isExist2vertices() {
        Box box = new Box(2, 1);
        boolean exist = box.isExist();
        assertThat(exist).isFalse();
    }

    @Test
    void  isExist4vertices() {
        Box box = new Box(4, 2);
        boolean exist = box.isExist();
        assertThat(exist).isTrue();
    }

    @Test
    void  getAreaOfCube() {
        Box box = new Box(8, 2);
        double area = box.getArea();
        assertThat(area).isEqualTo(24);
    }

    @Test
    void  getAreaOfSphere() {
        Box box = new Box(0, 2);
        double area = Math.round(box.getArea() * 100.0) / 100.0;
        assertThat(area).isEqualTo(50.27);
    }
}