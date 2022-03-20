package AILib.utills;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkReaderTest {

    @Test
    void read() throws IOException {
        NeuralNetworkReader reader = new NeuralNetworkReader(new FileReader("/home/ownmind/GitRepositories/AILib/src/test/java/AILib/utills/testFile.bin"));

        reader.read();
    }
}