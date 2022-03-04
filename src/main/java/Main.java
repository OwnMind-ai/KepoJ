import AILib.agents.QAgent;
import AILib.agents.SupervisedAgent;
import AILib.functions.StandardFunctions;
import AILib.layers.StaticLayer;
import AILib.utills.AgentChecker;

import java.util.Arrays;

public class Main {
    public static void main(String[] arg) {
        long time = System.nanoTime();
        Lambda f = ((x, y) -> x + 1 / Math.pow(y, 2));
        for (int i = 0; i < 1000; i++)
            f.run(1, i);
        System.out.println(System.nanoTime() - time);

        time = System.nanoTime();
        Lambda d = (x, y) -> x / y;
        Lambda p = Math::pow;
        Lambda f2 = ((x, y) ->
                x + d.run(1, p.run(y, 2))
        );
        for (int i = 0; i < 1000; i++)
            f2.run(1, i);
        System.out.println(System.nanoTime() - time);
    }
    interface Lambda{
        double run(double x, double y);
    }
}