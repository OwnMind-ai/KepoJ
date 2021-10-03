package AILib.agents;

import AILib.layers.Layer;

public interface Agent {
    double[] start(double[] input);

    void addLayer(Layer layer);
    void addAll(Layer... layers);

    void save(String fileName);
    void save();
}
