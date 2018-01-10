import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private char[][] grid;
    private WeightedQuickUnionUF WQU;
    private WeightedQuickUnionUF WQUsub;
    private int size;
    private int count;
    //create n-by-n grid, with all sites blocked
    public Percolation(int n)       {
        if(n <=0){
            throw new IllegalArgumentException("n can't be smaller than 1");
        }
        else{
            count = 0;
            size = n;
            grid = new char[n+1][n+1];
            for(int i = 1; i <= n; i++ ){
                for(int j = 1; j <= n; j++){
                    grid[i][j] = 'b';
                }
            }
            WQU = new WeightedQuickUnionUF(n*n+2);//includes 2 pseudo-object
            WQUsub = new WeightedQuickUnionUF(n*n+1);//only includes 1 pseudo-object at the top of the grid. Used to check full sites.
        }
    }
    // open site (row, col) if it is not open already
    public    void open(int row, int col)  {
        if((row <= 0 || row > size) || (col <= 0 || col > size )) throw new IllegalArgumentException(" column and row indices must be between 1 and n");
        if(grid[row][col] == 'o') return;
        else if(grid[row][col] == 'b'){
            grid[row][col] = 'o';
            count++;
        }
        connectSites(row,col);
        if(row == 1){
            WQU.union(size*size,col-1);//connect to pseudo-object n^2
            WQUsub.union(size*size,col-1);
            return;
        }
        if(row == size){
            WQU.union(size*size + 1, (row-1)* size + col-1);//connect to pseudo-object n^2+1
        }
    }
    private void connectSites(int row, int col){
        if(row - 1 >= 1 && grid[row-1][col] == 'o'){
            WQU.union((row-1) * size + col-1, (row-2)*size + col-1);
            WQUsub.union((row-1) * size + col-1, (row-2)*size + col-1);
        }
        if(row + 1 <= size && grid[row+1][col] == 'o'){
            WQU.union((row-1) * size + col-1, row*size + col-1);
            WQUsub.union((row-1) * size + col-1, row*size + col-1);
        }
        if(col - 1 >= 1 && grid[row][col-1] == 'o'){
            WQU.union((row-1) * size + col-1, (row-1)*size + col - 2 );
            WQUsub.union((row-1) * size + col-1, (row-1)*size + col - 2 );
        }
        if(col + 1 <= size && grid[row][col+1] == 'o'){
            WQU.union((row-1) * size + col-1,(row-1)*size + col );
            WQUsub.union((row-1) * size + col-1,(row-1)*size + col );
        }
    }
    public boolean isOpen(int row, int col){
        if((row <= 0 || row > size) || (col <= 0 || col > size )) throw new IllegalArgumentException(" column and row indices must be between 1 and n");
        return grid[row][col] == 'o';
    }  // is site (row, col) open?
    public boolean isFull(int row, int col){
        if((row <= 0 || row > size) || (col <= 0 || col > size )) throw new IllegalArgumentException(" column and row indices must be between 1 and n");
        return (WQUsub.connected((row-1)* size + col-1, size*size));
    }  // is site (row, col) full?
    public     int numberOfOpenSites(){
        return count;
    }       // number of open sites
    public boolean percolates(){
        return WQU.connected(size*size + 1, size*size);
    }        // does the system percolate?

    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        Percolation perc = new Percolation(n);
        while(!perc.percolates()){
            int row = StdRandom.uniform(1,perc.size+1);
            int col = StdRandom.uniform(1,perc.size+1);
            perc.open(row, col);
        }
    }
}   // test client (optional)
