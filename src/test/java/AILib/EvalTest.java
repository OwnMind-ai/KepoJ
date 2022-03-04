package AILib;

import org.junit.jupiter.api.Test;

public class EvalTest {
    @Test
    public void firstTest(){
        long time = System.nanoTime();
        Lambda f = ((x, y) -> x + 1 / Math.pow(y, 2));
        for (int i = 0; i < 1000; i++)
            System.out.println(f.run(1, i));
        System.out.println(System.nanoTime() - time);
    }

    @Test
    public void secondTest(){
        long time = System.nanoTime();
        Lambda d = (x, y) -> x / y;
        Lambda p = Math::pow;
        Lambda f = ((x, y) ->
                x + d.run(1, p.run(y, 2))
        );
        for (int i = 0; i < 1000; i++)
            System.out.println(f.run(1, i));
        System.out.println(System.nanoTime() - time);
    }

    interface Lambda{
        double run(double x, double y);
    }
}
