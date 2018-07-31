package com.khantzen.findyourway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.khantzen.findyourway.constants.GameConstants.MUTATION_RATE;

public class Brain {
    private List<Float> directionList;
    private int counter;

    boolean isTerminated() {
        return this.counter == directionList.size();
    }

    public int getCounter() {
        return counter;
    }

    public void mutate() {
        Random rng = new Random();

        for (int i = 0; i < this.directionList.size(); i++) {
            if (rng.nextFloat() < MUTATION_RATE) {
                float newAngle = (float) (rng.nextFloat() * 2 * Math.PI);
                this.directionList.set(i, newAngle);
            }
        }

    }

    float getNextDirection() {
        float nextDirection = this.directionList.get(counter);
        this.counter++;
        return nextDirection;
    }


    public Brain copy() {
        Brain cloned = new Brain();

        cloned.directionList = new ArrayList<>();

        cloned.directionList.addAll(directionList);

        cloned.counter = 0;
        return cloned;
    }

    public void buildDirection(int counter) {
        Random random = new Random();
        this.directionList = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            float randomAngle = (float) (random.nextFloat() * 2 * Math.PI);
            this.directionList.add(randomAngle);
        }

        this.counter = 0;
    }
}
