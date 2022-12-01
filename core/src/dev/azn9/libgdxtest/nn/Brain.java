package dev.azn9.libgdxtest.nn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Brain {

    private final int inputNeuronsCount;
    private final int outputNeuronsCount;

    private final List<Neuron> inputNeurons = new ArrayList<>();
    private final List<Neuron> outputNeurons = new ArrayList<>();

    private final List<Neuron> allNeurons = new ArrayList<>();
    private final List<Synapse> allSynapses = new ArrayList<>();

    private final Map<Integer, List<Neuron>> neuronsByLayer = new HashMap<>();

    private int outputLayer = 1;

    public Brain(int inputNeurons, int outputNeurons) {
        this.inputNeuronsCount = inputNeurons;
        this.outputNeuronsCount = outputNeurons;
    }

    public void initialize() {
        for (int i = 0; i < inputNeuronsCount; i++) {
            this.inputNeurons.add(new Neuron(this));
        }

        for (int i = 0; i < outputNeuronsCount; i++) {
            this.outputNeurons.add(new Neuron(this));
        }

        for (Neuron inputNeuron : this.inputNeurons) {
            inputNeuron.setLayer(0);

            for (Neuron outputNeuron : this.outputNeurons) {
                outputNeuron.setLayer(1);

                inputNeuron.addOutputSynapse(new Synapse(this, inputNeuron, outputNeuron, (float) Math.random()));
            }
        }
    }

    public void feedInput(List<Float> inputs) {
        if (inputs.size() != inputNeuronsCount) {
            throw new IllegalArgumentException("Invalid input size");
        }

        for (int i = 0; i < inputNeuronsCount; i++) {
            this.inputNeurons.get(i).addToInput(inputs.get(i));
        }
    }

    public void engage() {
        Set<Neuron> nextNeurons = new HashSet<>(this.inputNeurons);

        do {
            Set<Neuron> newNextNeurons = new HashSet<>();

            for (Neuron neuron : nextNeurons) {
                newNextNeurons.addAll(neuron.engage());
            }

            nextNeurons = newNextNeurons;
        } while (!nextNeurons.isEmpty());
    }

    public void reset() {
        this.allNeurons.forEach(Neuron::resetInput);
        this.allSynapses.forEach(Synapse::reset);
    }

    public List<Neuron> getInputNeurons() {
        return this.inputNeurons;
    }

    public List<Neuron> getOutputNeurons() {
        return this.outputNeurons;
    }

    public void mutate() {
        for (Neuron neuron : new ArrayList<>(this.allNeurons)) {
            neuron.mutate();
        }

        for (Synapse synapse : new ArrayList<>(this.allSynapses)) {
            synapse.mutate();
        }
    }

    public void addNeuron(Neuron neuron) {
        this.allNeurons.add(neuron);
    }

    public void addSynapse(Synapse synapse) {
        this.allSynapses.add(synapse);
    }

    public int getOutputLayer() {
        return this.outputLayer;
    }

    public void setOutputLayer(int outputLayer) {
        this.outputLayer = outputLayer;
    }

    public void shutdown() {

    }

    public void removeNeuronFromLayer(Neuron neuron, int layer) {
        this.neuronsByLayer.get(layer).remove(neuron);
    }

    public void addNeuronToLayer(Neuron neuron, int layer) {
        this.neuronsByLayer.computeIfAbsent(layer, k -> new ArrayList<>()).add(neuron);
    }

    public List<Neuron> getAllNeuronsOnLayer(int i) {
        return this.neuronsByLayer.get(i);
    }

    public List<Float> getOutput() {
        List<Float> output = new ArrayList<>();

        for (Neuron outputNeuron : this.outputNeurons) {
            output.add(outputNeuron.getOutputValue());
        }

        return output;
    }
}
