package AILib.agents;

import AILib.layers.Layer;

/*
*  Classes that implement the Agent interface are often neural networks,
*  can process data and save themselves to a file
*/

public interface Agent {
    double[] react(double[] data);   // Process environment state
}
