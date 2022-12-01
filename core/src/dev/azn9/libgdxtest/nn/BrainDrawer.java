package dev.azn9.libgdxtest.nn;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.azn9.libgdxtest.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrainDrawer {

    private Brain brain;

    private float xOffset;
    private float yOffset;

    public BrainDrawer(Brain brain, float xOffset, float yOffset) {
        this.brain = brain;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    static boolean printed = false;

    public void draw(ShapeRenderer shapeRenderer) {
        if (this.brain == null) {
            return;
        }

        Map<Neuron, Tuple2<Float, Float>> neuronPositions = new HashMap<>();

        if (!printed) {
            System.out.println("Output layer: " + this.brain.getOutputLayer());
        }

        for (int i = 0; i <= this.brain.getOutputLayer(); i++) {
            List<Neuron> neuronsOnLayer = this.brain.getAllNeuronsOnLayer(i);

            if (neuronsOnLayer == null || neuronsOnLayer.isEmpty()) {
                continue;
            }

            if (!printed) {
                System.out.println("Layer: " + i + " contains " + neuronsOnLayer.size() + " neurons");
            }

            float x = this.xOffset + i * 50;
            float y = this.yOffset;

            for (Neuron neuron : neuronsOnLayer) {
                y += 50;
                neuronPositions.put(neuron, new Tuple2<>(x, y));

                if (!printed) {
                    System.out.println("Neuron: x=" + x + ", y=" + y);
                }

                shapeRenderer.setColor(0, 0, 0, 1);
                shapeRenderer.circle(x, y, 10);
            }
        }

        for (int i = 0; i <= this.brain.getOutputLayer(); i++) {
            List<Neuron> neuronsOnLayer = this.brain.getAllNeuronsOnLayer(i);

            if (neuronsOnLayer == null || neuronsOnLayer.isEmpty()) {
                continue;
            }

            for (Neuron neuron : neuronsOnLayer) {
                for (Synapse synapse : neuron.getOutputSynapses()) {
                    Neuron outputNeuron = synapse.getOutputNeuron();

                    Tuple2<Float, Float> neuronPosition = neuronPositions.get(neuron);
                    Tuple2<Float, Float> outputNeuronPosition = neuronPositions.get(outputNeuron);

                    if (neuronPosition == null || outputNeuronPosition == null) {
                        continue;
                    }

                    float weight = synapse.getWeight();

                    if (weight < 0.5F) {
                        shapeRenderer.setColor(1, 0, 0, 1);
                    } else {
                        shapeRenderer.setColor(0, 0, 1, 1);
                    }

                    int width = (int) (Math.abs(weight - 0.5F) * 10) + 1;

                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rectLine(
                            neuronPosition.getT(),
                            neuronPosition.getU(),
                            outputNeuronPosition.getT(),
                            outputNeuronPosition.getU(),
                            width
                    );
                }
            }
        }

        if (!printed) {
            printed = true;
        }
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }
}
