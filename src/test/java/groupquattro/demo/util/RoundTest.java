package groupquattro.demo.util;

import groupquattro.demo.utils.Round;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class RoundTest {


    @Test
    public void test1(){
        double d = Round.round(1.556, 2);

        assertEquals(d, 1.56);

    }
    @Test
    public void test2(){
        double d = Round.round(1.5262, 2);

        assertEquals(d, 1.53);

    }
    @Test
    public void test3(){
        double d = Round.round(0.009, 2);

        assertEquals(d, 0.01);

    }
    @Test
    public void test4(){
        double d = Round.round(-0.234, 2);

        assertEquals(d, -0.23);

    }

    @Test
    public void test5(){
        double d = Round.round(-0.234, 3);

        assertEquals(d, -0.234);

    }

    @Test
    public void test6(){
        double d = Round.round(-0.234, 1);

        assertEquals(d, -0.2);

    }
}
