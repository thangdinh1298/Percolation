import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] threshold;
    private double mean;
    private double dev;
    private int trials;
    public PercolationStats(int n, int trials){
        if(n <=0 || trials <=0)
        {
            throw new IllegalArgumentException("n and trials has to be larger than 0");
        }
        this.trials = trials;
        threshold = new double[trials];
        int count = 0;
        while(trials != 0){
            Percolation perc = new Percolation(n);
            while(!perc.percolates()){
                int row = StdRandom.uniform(1,n+1);
                int col = StdRandom.uniform(1,n+1);
                perc.open(row, col);
            }
            threshold[count++] =(float) perc.numberOfOpenSites()/(n*n);
            trials--;
        }
        mean = StdStats.mean(threshold);
        dev = StdStats.stddev(threshold);
    }    // perform trials independent experiments on an n-by-n grid
    public double mean(){
        return mean;
    }                          // sample mean of percolation threshold
    public double stddev(){
        return dev;
    }                        // sample standard deviation of percolation threshold
    public double confidenceLo(){
        return mean - (1.96*dev)/Math.sqrt(trials);
    }                  // low  endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean + (1.96*dev)/Math.sqrt(trials);
    }                  // high endpoint of 95% confidence interval

    public static void main(String[] args){
        int trials = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[0]);
        PercolationStats percStat = new PercolationStats(n,trials);
        System.out.println(percStat.mean);
        System.out.println(percStat.dev);
        System.out.println("["+percStat.confidenceLo()+", "+percStat.confidenceHi()+"]");
    }        // test client (described below)
}
