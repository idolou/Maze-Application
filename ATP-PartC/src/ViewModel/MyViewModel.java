package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int rowPlayer;
    private int colPlayer;
    private int rowEnd;
    private int colEnd;
    private int startRow;
    private int startCol;
    private boolean isSolved;
    int[][] mazeGrid;

    Maze maze;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
    }

    public void update(Observable o, Object arg) {
        if (arg == "mazeFromFile"){
            rowPlayer = model.getRowUser();
            colPlayer = model.getColUser();
            rowEnd = model.getRowEnd();
            colEnd = model.getColEnd();
            isSolved = model.isReachedEnd();
            maze = model.getMaze();
            setChanged();
            notifyObservers("mazeFromFile");
            //*********get maze from file*********
        }
        else if(o instanceof IModel){
            if(model.getMaze() == null){
                this.mazeGrid = model.getGrid();
            }
            else{
                Maze maze = model.getMaze();
//                int[][] grid = model.getGrid();
                if(maze == this.maze){
                    int rowPlayer = model.getRowUser();
                    int colPlayer = model.getColUser();
                    int rowEnd = model.getRowEnd();
                    int colEnd = model.getColEnd();
                    boolean Solved = model.isReachedEnd();
                    if(this.colPlayer == colPlayer && this.rowPlayer == rowPlayer){
                        getSolution();
                    }
                    else {
                        this.rowPlayer = rowPlayer;
                        this.colPlayer = colPlayer;
                        this.isSolved = Solved;

                    }
                }
                else{
                    this.maze = maze;
                    startRow = model.getMaze().getStartPosition().getRowIndex();
                    startCol = model.getMaze().getStartPosition().getColumnIndex();
                    rowPlayer = model.getRowUser();
                    colPlayer = model.getColUser();
                    isSolved = model.isReachedEnd();
                }
            }
        }
        setChanged();
        notifyObservers(arg);
    }

    public Maze getmaze() {
        return maze;
    }

    public int[][] getMaze() {
        return model.getGrid();
    }

    public int getPlayerRow(){
        return model.getRowUser();
    }

    public int getEndRow(){
        return model.getRowEnd();
    }

    public int getEndCol(){
        return model.getColEnd();
    }

    public int getPlayerCol(){ return model.getColUser();}

    public Solution getSolution(){
        return this.model.getSolution();
    }

    public void generateMaze(int row, int col) {
        this.model.generateMaze(row, col);
    }


    public void movePlayer(KeyEvent event) {
        int direction;
        switch (event.getCode()){
            case NUMPAD8 -> direction = 1; //UP
            case UP -> direction = 1;
            case NUMPAD2 -> direction = 2; //DOWN
            case DOWN -> direction = 2;
            case NUMPAD6 -> direction = 3; //RIGHT;
            case RIGHT -> direction = 3;
            case NUMPAD4 -> direction = 4; //LEFT
            case LEFT -> direction = 4;
            case NUMPAD9 -> direction = 5; //UP_RIGHT
            case NUMPAD7 -> direction = 6; //UP_LEFT
            case NUMPAD3 -> direction = 7; //DOWN_RIGHT
            case NUMPAD1 -> direction = 8; //DOWN_LEFT

            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerPositionKey(direction);
    }



    public void movePlayer(KeyCode kc){
        int direction;
        switch (kc){
            case NUMPAD8 -> direction = 1; //UP
            case UP -> direction = 1;
            case NUMPAD2 -> direction = 2; //DOWN
            case DOWN -> direction = 2;
            case NUMPAD6 -> direction = 3; //RIGHT;
            case RIGHT -> direction = 3;
            case NUMPAD4 -> direction = 4; //LEFT
            case LEFT -> direction = 4;
            case NUMPAD9 -> direction = 5; //UP_RIGHT
            case NUMPAD7 -> direction = 6; //UP_LEFT
            case NUMPAD3 -> direction = 7; //DOWN_RIGHT
            case NUMPAD1 -> direction = 8; //DOWN_LEFT

            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerPositionKey(direction);
    }



    public void movePlayer(double mouseX, double mouseY){
        if ( mouseX == colPlayer && mouseY < rowPlayer )
            movePlayer(KeyCode.NUMPAD8);
        else if (mouseY == rowPlayer && mouseX > colPlayer)
            movePlayer(KeyCode.NUMPAD6);
        else if ( mouseY == rowPlayer && mouseX < colPlayer )
            movePlayer(KeyCode.NUMPAD4);
        else if (mouseX == colPlayer && mouseY > rowPlayer )
            movePlayer(KeyCode.NUMPAD2); }


    //to check
    public void solveMaze(int currRow, int currCol){
        this.model.solveMaze(model.getRowUser(),model.getColUser());
    }

    public boolean isSolved(){
        return this.model.isReachedEnd();
    }

    public  void Exit(){
        this.model.exit();
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void saveMaze(File fileName){
        this.model.saveMaze(fileName);
    }

    public void loadMaze(File fileName){
        this.model.loadMaze(fileName);
    }

    public void refreshProperties() {
        model.refreshStrategies();
    }

}
