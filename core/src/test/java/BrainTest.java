import dev.azn9.libgdxtest.nn.Brain;
import dev.azn9.libgdxtest.nn.Neuron;

import java.util.Arrays;
import java.util.List;

public class BrainTest {

    public static void main(String[] args) {
        Brain brain = new Brain(2, 1);
        brain.initialize();

        System.out.println("Brain created");

        for (int i = 0; i <= brain.getOutputLayer(); i++) {
            System.out.println("Layer " + i + " contains " + brain.getAllNeuronsOnLayer(i).size() + " neurons");
        }

        List<Float> inputs = Arrays.asList(1f, 1f);
        brain.feedInput(inputs);

        brain.engage();

        System.out.println("Output: " + brain.getOutputNeurons().get(0).getOutputValue());

        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.println("==== Iteration " + i + " ====");

            brain.reset();
            brain.mutate();

            for (int j = 0; j <= brain.getOutputLayer(); j++) {
                List<Neuron> neurons = brain.getAllNeuronsOnLayer(j);

                if (neurons == null || neurons.isEmpty()) {
                    continue;
                }

                System.out.println("Layer " + j + " contains " + neurons.size() + " neurons");
            }

            brain.feedInput(inputs);
            brain.engage();
            System.out.println("Output: " + brain.getOutputNeurons().get(0).getOutputValue());
        }
    }

}
