package com.khantzen.findyourway.constants;

public class GameConstants {
    // Screen params
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    // Population
    public static final int POPULATION_SIZE = 1000;
    public static final int BRAIN_STEP = 400; // initial step length

    public static final int DOT_START_X = 200;
    public static final int DOT_START_Y = 100;

    // Goal
    public static final int GOAL_X = 750;
    public static final int GOAL_Y = 750;

    // Path finding
    public static final boolean ACTIVATE_OBSTACLE = true; // Add obstacles, see GameBoard::initObstacle to edit them

    // Evolution
    public static final float MUTATION_RATE = 0.01f;

}
