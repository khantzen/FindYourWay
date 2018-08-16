package com.khantzen.findyourway;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.khantzen.findyourway.game.GameBoard;

public class FindYourWay extends ApplicationAdapter {
    private GameBoard gameBoard;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.gameBoard = new GameBoard();
        this.gameBoard.init();
    }

    @Override
    public void render() {
        this.gameBoard.calculate();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        this.gameBoard.renderText(batch);
        batch.end();

        this.gameBoard.renderObject(this.shapeRenderer);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
