package AILib.agents;

import AILib.layers.Layer;

/*
*  Classes that implement the Agent interface are often neural networks,
*  can process data and save themselves to a file
*/

public interface Agent {
    double[] start(double[] input);   // Process input data

    void addLayer(Layer layer);       // Adds Layer to neural network
    void addAll(Layer... layers);     // Equals to Agent::addLayer(Layer layer), but can add several layers

    void save(String fileName);       // Saves Agent structure and data to file
    void save();                      // Equals to Agent::save(String fileName), but filepath is instance hashcode
}
