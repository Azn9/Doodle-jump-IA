package dev.azn9.libgdxtest.nn;

import java.util.ArrayList;
import java.util.List;

public class Synapse {

    private final Brain brain;
    private Neuron inputNeuron;
    private Neuron outputNeuron;
    private float weight;
    private boolean enabled;

    public Synapse(Brain brain, Neuron inputNeuron, Neuron outputNeuron, float weight) {
        this.brain = brain;
        brain.addSynapse(this);

        this.inputNeuron = inputNeuron;
        this.outputNeuron = outputNeuron;

        if (this.outputNeuron.getLayer() == -1) {
            int newLayer = this.inputNeuron.getLayer() + 1;
            if (newLayer >= brain.getOutputLayer()) {
                brain.setOutputLayer(newLayer + 1);
            }

            this.outputNeuron.setLayer(newLayer);
        }

        this.weight = weight;
    }

    public void engage(float outputValue) {
        if (this.enabled) {
            return;
        }

        this.enabled = true;
        this.outputNeuron.addToInput(outputValue * this.weight);
    }

    public void mutate() {
        if (Math.random() < 0.9D) {
            this.weight = (float) Math.random();
        } else {
            int newLayer = this.inputNeuron.getLayer() + 1;

            System.out.println("Creating new neuron on layer " + newLayer);

            if (newLayer == this.outputNeuron.getLayer()) {
                System.out.println("Need to create a new layer");

                this.brain.setOutputLayer(this.brain.getOutputLayer() + 1);
                for (int i = this.brain.getOutputLayer(); i >= newLayer; i--) {
                    List<Neuron> neuronsOnLayer = this.brain.getAllNeuronsOnLayer(i);

                    if (neuronsOnLayer == null || neuronsOnLayer.isEmpty()) {
                        continue;
                    }

                    for (Neuron neuron : new ArrayList<>(neuronsOnLayer)) {
                        neuron.setLayer(i + 1);
                    }
                }
            }

            Neuron newNeuron = new Neuron(this.brain);
            newNeuron.setLayer(newLayer);

            this.inputNeuron.removeSynapse(this);
            Synapse newSynapse = new Synapse(this.brain, this.inputNeuron, newNeuron, 1f);
            this.inputNeuron.addOutputSynapse(newSynapse);

            this.inputNeuron = newNeuron;
        }
    }

    public Neuron getInputNeuron() {
        return this.inputNeuron;
    }

    public Neuron getOutputNeuron() {
        return this.outputNeuron;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void reset() {
        this.enabled = false;
    }

    public float getWeight() {
        return this.weight;
    }
}
