package dev.azn9.libgdxtest.nn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Neuron {

    private final List<Synapse> outputSynapses = new ArrayList<>();
    private final Brain brain;

    private float inputValue;
    private float outputValue;
    private float bias;
    private int layer = -1;

    public Neuron(Brain brain) {
        this.brain = brain;
        brain.addNeuron(this);

        this.bias = (float) Math.random();
    }

    public Set<Neuron> engage() {
        this.outputValue = Utils.SIGMOID.apply(this.inputValue + this.bias);

        Set<Neuron> nextNeurons = new HashSet<>();

        for (Synapse synapse : this.outputSynapses) {
            synapse.engage(this.outputValue);
            nextNeurons.add(synapse.getOutputNeuron());
        }

        return nextNeurons;
    }

    public float getOutputValue() {
        return this.outputValue;
    }

    public void addToInput(float value) {
        this.inputValue += value;
    }

    public void resetInput() {
        this.inputValue = 0;
    }

    public void addOutputSynapse(Synapse synapse) {
        this.outputSynapses.add(synapse);
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        if (this.layer != -1) {
            this.brain.removeNeuronFromLayer(this, this.layer);
        }
        this.brain.addNeuronToLayer(this, layer);

        this.layer = layer;
    }

    public void mutate() {
        double random = Math.random();

        if (random < 0.9D) {
            this.bias = (float) Math.random();
        } else {
            int index = 0;
            List<Neuron> neurons = null;

            do {
                List<Neuron> original = this.brain.getAllNeuronsOnLayer(this.layer + (++index));

                if (original == null || original.isEmpty()) {
                    continue;
                }

                neurons = new ArrayList<>(original);
            } while (neurons == null && this.layer + index <= this.brain.getOutputLayer());

            if (neurons == null || neurons.isEmpty()) {
                return;
            }

            Neuron neuron;
            do {
                neuron = neurons.get((int) (Math.random() * neurons.size()));

                if (this.isConnectedTo(neuron)) {
                    neurons.remove(neuron);
                    neuron = null;
                }
            } while (!neurons.isEmpty() && this.isConnectedTo(neuron));

            if (neuron == null) {
                return;
            }

            System.out.println("Created new synapse");

            Synapse synapse = new Synapse(this.brain, this, neuron, (float) Math.random());
            this.outputSynapses.add(synapse);
        }
    }

    public boolean isConnectedTo(Neuron neuron) {
        for (Synapse synapse : this.outputSynapses) {
            if (synapse.getOutputNeuron() == neuron) {
                return true;
            }
        }

        return false;
    }

    public void removeSynapse(Synapse synapse) {
        this.outputSynapses.remove(synapse);
    }

    public List<Synapse> getOutputSynapses() {
        return outputSynapses;
    }
}
