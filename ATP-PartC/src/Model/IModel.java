package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import java.io.File;
import java.util.Observer;

public interface IModel {
    public void generateMaze(int rows, int cols);
    public void solveMaze(int row_User,int col_User);
    public void updatePlayerPositionKey(int direction);
    void setPlayerPosition(Position startPosition);
    void setGoalPosition(Position endPosition);
    public void assignObserver(Observer o);

//    public void stopServers();
//    public void reStartServers();

    public void saveMaze(File saveFile);
    public void loadMaze(File file);
    public void exit();
    public void restart();

    public Solution getSolution();
    public boolean isReachedEnd();
    public Maze getMaze();
    public int[][] getGrid();

    int getRowUser();
    int getColUser();

    int getRowEnd();
    int getColEnd();
    public void refreshStrategies();
}
