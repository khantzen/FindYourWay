package com.khantzen.findyourway.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.khantzen.findyourway.constants.GameConstants;
import com.khantzen.findyourway.model.Brain;
import com.khantzen.findyourway.model.Dot;
import com.khantzen.findyourway.model.Obstacle;

import java.util.*;
import java.util.stream.Collectors;

import static com.khantzen.findyourway.constants.GameConstants.*;

public class GameBoard {
    private List<Dot> population;
    private int step;
    private int simulationCounter = 1;
    private String textToDisplay;
    private List<Obstacle> obstacleList;

    public void init() {
        this.population = new ArrayList<>();

        this.step = BRAIN_STEP;

        for (int i = 0; i < GameConstants.POPULATION_SIZE; i++) {
            Brain brain = new Brain();

            brain.buildDirection(this.step);

            Dot dot = new Dot.builder()
                    .x(DOT_START_X).y(DOT_START_Y)
                    .brain(brain)
                    .color(0)
                    .build();

            population.add(dot);
        }

        this.obstacleList = initObstacle();

        textToDisplay = "First simulation";
        System.out.print("Start simulation " + simulationCounter);
    }

    private List<Obstacle> initObstacle() {
        List<Obstacle> obstacles = new ArrayList<>();

        if (ACTIVATE_OBSTACLE) {
            Obstacle ob1 = new Obstacle.Builder().x(350).y(0).width(25).height(400).build();
            //obstacles.add(ob1);
            Obstacle ob2 = new Obstacle.Builder().x(400).y(300).width(25).height(200).build();
            obstacles.add(ob2);
        }

        return obstacles;
    }

    public void calculate() {
        if (population.stream().allMatch(Dot::isEnded)) { // Simulation ended
            this.naturalSelection();
        } else {
            population.stream()
                    .filter(d -> !d.isEnded())
                    .forEach(d -> d.applyTranslation(this.step, this.obstacleList));
        }
    }

    private void naturalSelection() {
        this.population.forEach(Dot::calculFitness);

        double fitnessSum = population.stream()
                .mapToDouble(Dot::getFitness).sum();

        List<Dot> sortedDot =
                population.stream()
                        .sorted(Comparator.comparingDouble(Dot::getFitness))
                        .collect(Collectors.toList());

        Dot firstDot = sortedDot.get(sortedDot.size() - 1);

        List<Dot> newGeneration = new ArrayList<>();

        Brain bestBrain = firstDot.getBrain();

        if (firstDot.isWinner()) {
            this.step = bestBrain.getCounter();
            System.out.println("- We have a winner | " + this.step + " steps");
        } else {
            System.out.println();
        }

        Brain newBrain = bestBrain.copy();

        firstDot = new Dot.builder()
                .x(DOT_START_X).y(DOT_START_Y)
                .brain(newBrain)
                .color(0xff0000ff)
                .build();

        newGeneration.add(firstDot);

        for (int i = 1; i < GameConstants.POPULATION_SIZE; i++) {

            Brain brainForMutation = this.pickBrainForMutation(fitnessSum);

            Brain mutatedBrain = brainForMutation.copy();

            mutatedBrain.mutate();

            Dot newDot = new Dot.builder()
                    .x(DOT_START_X).y(DOT_START_Y)
                    .brain(mutatedBrain)
                    .color(0)
                    .build();

            newGeneration.add(newDot);
        }

        Collections.reverse(newGeneration);

        this.population = newGeneration;
        simulationCounter++;

        System.out.println("END");
        System.out.print("Start simulation " + simulationCounter);
        this.textToDisplay = "Simulation " + this.simulationCounter + " | Best score : " + this.step;
    }

    private Brain pickBrainForMutation(double fitnessSum) {
        Random rand = new Random();

        float random = (float) (rand.nextFloat() * fitnessSum);

        float currentSum = 0;

        for (Dot dot : population) {
            currentSum += dot.getFitness();
            if (currentSum > random) {
                return dot.getBrain();
            }
        }

        throw new RuntimeException("Not supposed to happen");
    }

    public void renderObject(ShapeRenderer shapeRenderer) {
        this.renderArea(shapeRenderer);
        this.renderPopulation(shapeRenderer);
        this.obstacleList.forEach(o -> o.renderObstacle(shapeRenderer));
    }

    private void renderPopulation(ShapeRenderer shapeRenderer) {
        population.stream().filter(d -> !d.isEnded()).forEach(d -> d.render(shapeRenderer));
    }

    private void renderArea(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(GOAL_X, GOAL_Y, 10f);
        shapeRenderer.end();

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();
    }

    public void renderText(SpriteBatch batch) {
        BitmapFont font = new BitmapFont();
        font.setColor(Color.PURPLE);
        font.draw(batch, this.textToDisplay, 15, 25);
    }
}
