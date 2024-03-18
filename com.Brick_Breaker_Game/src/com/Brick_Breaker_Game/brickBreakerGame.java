package com.Brick_Breaker_Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class brickBreakerGame extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_RADIUS = 10;
    private static final int BRICK_WIDTH = 80;
    private static final int BRICK_HEIGHT = 30;
    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 10;
    private static final double BALL_SPEED = 5;
    private static final double PADDLE_SPEED = 8;

    private Canvas canvas;
    private GraphicsContext gc;
    private Paddle paddle;
    private Ball ball;
    private Brick[][] bricks;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        paddle = new Paddle(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(WIDTH / 2, HEIGHT / 2, BALL_RADIUS);
        bricks = new Brick[NUM_ROWS][NUM_COLS];
        createBricks();

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                paddle.moveLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                paddle.moveRight();
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Brick Breaker");
        primaryStage.show();
    }

    private void createBricks() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                int x = j * (BRICK_WIDTH + 5) + 50;
                int y = i * (BRICK_HEIGHT + 5) + 50;
                bricks[i][j] = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
            }
        }
    }

    private void update() {
        paddle.update();
        ball.update();

        // Check paddle-ball collision
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseY();
        }

        // Check ball-brick collision
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Brick brick = bricks[i][j];
                if (brick.isVisible() && ball.getBounds().intersects(brick.getBounds())) {
                    brick.setVisible(false);
                    ball.reverseY();
                    score += 10;
                }
            }
        }

        // Check game over
        if (ball.getY() > HEIGHT) {
            // Game over logic
            System.out.println("Game Over");
        }
    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.BLUE);
        paddle.render(gc);
        gc.setFill(Color.RED);
        ball.render(gc);
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Brick brick = bricks[i][j];
                if (brick.isVisible()) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
                }
            }
        }
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Paddle {
    private double x;
    private double y;
    private double width;
    private double height;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void moveLeft() {
        if (x > 0) {
            x -= BrickBreakerGame.PADDLE_SPEED;
        }
    }

    public void moveRight() {
        if (x + width < BrickBreakerGame.WIDTH) {
            x += BrickBreakerGame.PADDLE_SPEED;
        }
    }

    public void update() {
        // Paddle update logic, if any
    }

    public void render(GraphicsContext gc) {
        gc.fillRect(x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
}

class Ball {
    private double x;
    private double y;
    private double radius;
    private double dx;
    private double dy;

    public Ball(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = BrickBreakerGame.BALL_SPEED;
        this.dy = -BrickBreakerGame.BALL_SPEED;
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < 0 || x > BrickBreakerGame.WIDTH) {
            dx = -dx;
        }
        if (y < 0 || y > BrickBreakerGame.HEIGHT) {
            dy = -dy;
        }
    }

    public void render(GraphicsContext gc) {
        gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public void reverseY() {
        dy = -dy;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}

class Brick {
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean visible;

    public Brick(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
}
