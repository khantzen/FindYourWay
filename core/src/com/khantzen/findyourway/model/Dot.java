package com.khantzen.findyourway.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

import static com.khantzen.findyourway.constants.GameConstants.*;

public class Dot {
    private Brain brain;

    private float x;
    private float y;

    private int color;

    private boolean isDead;
    private boolean isWinner;
    private float fitness;


    public double getFitness() {
        return this.fitness;
    }

    private Dot(builder builder) {
        this.brain = builder.brain;
        this.x = builder.x;
        this.y = builder.y;

        this.color = builder.color;

        this.isDead = false;
        this.isWinner = false;
    }

    public boolean isEnded() {
        return this.isDead || this.isWinner || this.brain.isTerminated();
    }

    public boolean isAlive() {
        return this.isDead;
    }

    public Brain getBrain() {
        return brain;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void applyTranslation(int step, List<Obstacle> obstacleList) {
        if (this.brain.isTerminated() || this.isDead || this.isWinner) {
            return;
        }

        // float xStep = (targetX - x) / 10;
        // float yStep = (targetY - y) / 10;

        float vectorAngle = this.brain.getNextDirection();

        x += (20 * Math.cos(vectorAngle));
        y += (20 * Math.sin(vectorAngle));

        if (x <= 0 || x >= SCREEN_WIDTH
                || y < 0 || y >= SCREEN_HEIGHT // If dot leave the window he is dead
                || brain.getCounter() > step
        ) {
            this.isDead = true;
        }

        if (!this.isDead) {
            for (Obstacle ob : obstacleList) {
                if ((x >= ob.getX() && x <= (ob.getX() + ob.getWidth()))
                        && (y >= ob.getY() && y <= (ob.getY() + ob.getHeight()))) {
                    this.isDead = true;
                }
            }
        }

        double distToGoalSquare = Math.pow(x - GOAL_X, 2) + Math.pow(y - GOAL_Y, 2);

        if (distToGoalSquare < 121) { // Goal area radius is 11
            this.isWinner = true;
        }
    }

    public void calculFitness() {
        if (this.isWinner) {
            // Goal area radius is 11 so the greatest fitness radius canno't be greater than 1/121,
            // We add 10 000f / step² so the greater the step are the lesser the fitness will be
            int stepSquare = this.brain.getCounter() * this.brain.getCounter();
            this.fitness = 10 * (1f / 121f + 10000f / (float) stepSquare);
        } else {
            // The nearest we are from the goal the greater the fitness is
            double distToGoalSquare = Math.pow(x - GOAL_X, 2) + Math.pow(y - GOAL_Y, 2);
            this.fitness = (float) (1 / distToGoalSquare); // < 1 / 121
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(this.color == 0 ? Color.BLACK : new Color(this.color));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x, y, 2.5f);
        shapeRenderer.end();
    }

    public static class builder {

        private float x;
        private float y;
        private Brain brain;
        private int color;


        public builder x(float x) {
            this.x = x;
            return this;
        }

        public builder y(float y) {
            this.y = y;
            return this;
        }

        public builder brain(Brain brain) {
            this.brain = brain;
            return this;
        }

        public builder color(int color) {
            this.color = color;
            return this;
        }

        public Dot build() {
            return new Dot(this);
        }
    }
}
