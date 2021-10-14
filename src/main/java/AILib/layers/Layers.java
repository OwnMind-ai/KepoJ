package AILib.layers;

import AILib.entities.AIFunctions;

public enum Layers {
    INPUT_LAYER{

        @Override
        public Layer getInstance(double[] data) {
            return new InputLayer((int) data[0]);
        }

        @Override
        public Class getLayerClass() {
            return InputLayer.class;
        }

        @Override
        public int getDataLength() {
            return 1;
        }
    },
    STATIC_LAYER{
        @Override
        public Layer getInstance(double[] data) {
            return new StaticLayer((int) data[0], AIFunctions.values()[(int) data[1]]);
        }

        @Override
        public Class getLayerClass() {
            return StaticLayer.class;
        }

        @Override
        public int getDataLength() {
            return 2;
        }
    },
    CONVOLUTIONAL_LAYER{
        @Override
        public Layer getInstance(double[] data) {
            return new ConvolutionalLayer(
                    (int) data[0],
                    (int) data[1],
                    (int) data[2],
                    (int) data[3],
                    AIFunctions.values()[(int) data[4]]
            );
        }

        @Override
        public Class getLayerClass() {
            return ConvolutionalLayer.class;
        }

        @Override
        public int getDataLength() {
            return 5;
        }
    },
    CLUSTERED_LAYER{
        @Override
        public Layer getInstance(double[] data) {
            return null;
        }

        @Override
        public Class getLayerClass() {
            return ClusteredLayer.class;
        }

        @Override
        public int getDataLength() {
            return 0;
        }
    };

    public abstract Layer getInstance(double[] data);

    public abstract Class getLayerClass();

    public abstract int getDataLength();

    public static int getLayerID(Class layerClass){
        for (int i = 0; i < Layers.values().length; i++) {
            if (Layers.values()[i].getLayerClass().equals(layerClass))
                return i;
        }
        return -1;
    }
}
