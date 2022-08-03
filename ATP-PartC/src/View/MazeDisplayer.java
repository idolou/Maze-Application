package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    private Solution solution;

    private Maze maze1;

    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    int row_goal, col_goal;
    boolean endGame = false;
    private boolean roundFirst = true;


    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNameFinish = new SimpleStringProperty();
    StringProperty imageFileNamePath = new SimpleStringProperty();


    public MazeDisplayer() {

        heightProperty().addListener(evt -> {
            draw();
        });

        widthProperty().addListener(evt -> {
            draw();
        });


//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newValue) {
//                heightProperty().set(newValue.doubleValue());
////                setHeight((double) newValue);
//            }
//        });

//        heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newValue) {
////                setWidth((double) newValue);
//                widthProperty().set(newValue.doubleValue());
//            }
//        });
    }

    public String getImageFileNamePath() {
        return imageFileNamePath.get();
    }

    public void setImageFileNamePath(String imageFileNamePath) {
        this.imageFileNamePath.set(imageFileNamePath);
    }

    public String getImageFileNameFinish() {
        return imageFileNameFinish.get();
    }

    public void setImageFileNameFinish(String imageFileNameFinish) {
        this.imageFileNameFinish.set(imageFileNameFinish);
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }


    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setGoalPosition(int row, int col) {
        this.row_goal = row;
        this.col_goal = col;
    }


    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }


    public void resetFirstRound(boolean first) {
        this.roundFirst = first;
    }


    public void drawMaze(Maze maze) {
        this.maze1 = maze;
        if (this.roundFirst) {
            this.roundFirst = false;
            setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());
//            setPlayerPosition(controller.getMyViewModel().getStartRow(), controller.getMyViewModel().getStartCol());
        }
        this.endGame = false;
        setGoalPosition(maze.getGoalPosition().getRowIndex(), maze.getGoalPosition().getColumnIndex());
//        setGoalPosition(controller.getMyViewModel().getEndRow(), controller.getMyViewModel().getEndCol());
        draw();
    }


    private void draw() {
        if (maze1 != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze1.getMaze().length;
            int cols = maze1.getMaze()[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            ArrayList<AState> sol = null;
            if (solution != null) {
                sol = solution.getSolutionPath();
            }

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);


            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);

//                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols, sol);

            Image finishImage = null;
            try {
                finishImage = new Image(new FileInputStream(getImageFileNameFinish()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (endGame && finishImage != null) {
                double wCanvas = graphicsContext.getCanvas().getWidth();
                double hCanvas = graphicsContext.getCanvas().getHeight();
                double hImage = finishImage.getHeight();
                double wImage = finishImage.getWidth();

                graphicsContext.drawImage(finishImage, getWidth() / 8, getHeight() / 8, graphicsContext.getCanvas().getHeight(), graphicsContext.getCanvas().getHeight());
                System.out.println(getHeight());
                System.out.println(getWidth());


            }
            if (solution != null)
                solution = null;

            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawGoal(graphicsContext, cellHeight, cellWidth);
        }
    }


    public void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols, ArrayList<AState> path) {
        // need to be implemented
        Image SolPath = null;
        try {
            SolPath = new Image(new FileInputStream(getImageFileNamePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (path != null)
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    AState state = new MazeState(0, new Position(i, j));
                    if (path.contains(state)) {
                        graphicsContext.drawImage(SolPath, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                }
            }

    }


    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);
        int[][] maze = maze1.getMaze();

        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 1) {
                    //if it is a wall:
                    double w = j * cellWidth;
                    double h = i * cellHeight;
                    if (wallImage == null)
                        graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                }
            }
        }
    }


    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if (playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }


    private void drawGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = col_goal * cellWidth;
        double y = row_goal * cellHeight;
        graphicsContext.setFill(Color.BLUE);

        Image goalImage = null;
        try {
            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no goal image file");
        }
        if (goalImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(goalImage, x, y, cellWidth, cellHeight);
    }

    public void drawFinished(boolean finished) {
        this.endGame = finished;
        draw();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public void setRoundFirst(boolean roundFirst) {
        this.roundFirst = roundFirst;
    }


}
