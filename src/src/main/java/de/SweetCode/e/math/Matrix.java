package de.SweetCode.e.math;

import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

public class Matrix {

    private final int n;
    private final int m;

    private double[][] matrix;

    /**
     * This creates a n*m matrix.
     * @param n The amount of vertical elements.
     * @param m The amount of horizontal elements.
     */
    public Matrix(int n, int m) {

        Assert.assertTrue("n must be greater than 0.", n > 0);
        Assert.assertTrue("m must be greater than 0.", m > 0);

        this.n = n;
        this.m = m;
        this.matrix = new double[n][m];

    }

    /**
     * The amount of m.
     * @return
     */
    public int getN() {
        return this.n;
    }

    /**
     * The amount of n.
     * @return
     */
    public int getM() {
        return this.m;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix(double[][] matrix) {

        boolean sameDimension = (matrix.length == this.getN());

        if(sameDimension) {
            for (int i = 0; i < matrix.length; i++) {
                if(!(this.matrix[i].length == matrix[i].length)) {
                    sameDimension = false;
                    break;
                }
            }
        }

        Assert.assertTrue(String.format("The matrix doesn't fit the matrix dimensions (%dx%d).", this.getN(), this.getM()), sameDimension);

        this.matrix = matrix;
    }

    public Matrix multiply(Matrix b) {

        Assert.assertTrue("The matrix a (this) needs the same amount of m as the amount of b n.", this.getM() == b.getN());

        Matrix result = new Matrix(this.getN(), b.getM());

        for(int i = 0; i < result.getN(); i++) {
            for(int j = 0; j < result.getM(); j++) {
                for(int k = 0; k < this.getM(); k++) {
                    result.set(i, j, result.get(i, j) + this.get(k, j) * b.get(i, k));
                }
            }
        }

        return result;

    }

    public Matrix add(Matrix b) {

        Assert.assertTrue(
                String.format("The matrices need the same dimensions (%dx%d).", this.getN(), this.getM()),
                (this.getN() == (b.getN()) && this .getM() == b.getM())
        );

        Matrix result = new Matrix(this.getN(), this.getM());

        for(int x = 0; x < this.getM(); x++) {
            for(int y = 0; y < this.getN(); y++) {
                result.set(x, y, this.get(x, y) + b.get(x, y));
            }
        }

        return result;

    }

    public Matrix sub(Matrix b) {

        Assert.assertTrue(
                String.format("The matrices need the same dimensions (%dx%d).", this.getN(), this.getM()),
                (this.getN() == (b.getN()) && this .getM() == b.getM())
        );

        Matrix result = new Matrix(this.getN(), this.getM());

        for(int x = 0; x < this.getM(); x++) {
            for(int y = 0; y < this.getN(); y++) {
                result.set(x, y, this.get(x, y) - b.get(x, y));
            }
        }

        return result;

    }

    public void set(int x, int y, double value) {

        Assert.assertTrue(String.format("x cannot be greater or equal to %d.", this.getM()), x < this.getM());
        Assert.assertTrue(String.format("y cannot be greater or equal to %d.", this.getN()), y < this.getN());

        this.matrix[y][x] = value;

    }

    public double get(int x, int y) {

        Assert.assertTrue(String.format("x cannot be greater or equal to %d.", this.getM()), x < this.getM());
        Assert.assertTrue(String.format("y cannot be greater or equal to %d.", this.getN()), y < this.getN());

        try {
            return this.matrix[y][x];
        } catch (Exception ex) {
            System.out.println("Exception at " + (x + " - " + y));
        }

        return -1;

    }

    @Override
    public String toString() {

        ToStringBuilder builder = ToStringBuilder.create(this)
                .append("n", this.getN())
                .append("m", this.getM());

        int i = 1;
        for(double[] m : this.getMatrix()) {
            builder.append(String.format("%d.", i), m);
            i++;
        }

        return builder.build();

    }
}
