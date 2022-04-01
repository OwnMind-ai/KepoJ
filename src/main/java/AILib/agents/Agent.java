package AILib.agents;

/*
*  Classes that implement the Agent interface are often neural networks,
*  can process data and save themselves to a file
*/

public interface Agent {
    double[] react(double[] data);   // Process environment state

    int outputLength();
}
