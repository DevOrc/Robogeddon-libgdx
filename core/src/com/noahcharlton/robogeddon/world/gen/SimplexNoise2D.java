package com.noahcharlton.robogeddon.world.gen;

import java.util.Random;

/**
 * An impementation of Simplex Noise for 2 dimensions.
 * <p>
 * Based on this paper: <a href="http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf">
 * http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf</a>
 */
public class SimplexNoise2D {

    private static int gradients[][] = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1},
            {1, 0}, {-1, 0}, {1, 0}, {-1, 0},
            {0, 1}, {0, -1}, {0, 1}, {0, -1}
    };

    private final int[] permutations = new int[512];

    public SimplexNoise2D(Random random) {
        for(int i = 0; i < 256; i++) {
            permutations[i] = permutations[i + 256] = random.nextInt(256);
        }
    }

    public double noise(double xIn, double yIn) {
        double n0, n1, n2; // Noise contributions from the three corners

        // Skew the input space to determine which simplex cell we're in
        double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        double s = (xIn + yIn) * F2; // Hairy factor for 2D
        int i = floor(xIn + s);
        int j = floor(yIn + s);
        double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
        double t = (i + j) * G2;
        double X0 = i - t; // Unskew the cell origin back to (x,y) space
        double Y0 = j - t;
        double x0 = xIn - X0; // The x,y distances from the cell origin
        double y0 = yIn - Y0;

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        int i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords
        if(x0 > y0) {
            i1 = 1;
            j1 = 0;
        } // lower triangle, XY order: (0,0)->(1,0)->(1,1)
        else {
            i1 = 0;
            j1 = 1;
        } // upper triangle, YX order: (0,0)->(0,1)->(1,1)

        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords
        double y2 = y0 - 1.0 + 2.0 * G2;
        // Work out the hashed gradient indices of the three simplex corners
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = permutations[ii + permutations[jj]] % 12;
        int gi1 = permutations[ii + i1 + permutations[jj + j1]] % 12;
        int gi2 = permutations[ii + 1 + permutations[jj + 1]] % 12;

        // Calculate the contribution from the three corners
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if(t0 < 0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * dotProduct(gradients[gi0], x0, y0); // (x,y) of grad3 used for 2D gradient
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if(t1 < 0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * dotProduct(gradients[gi1], x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if(t2 < 0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * dotProduct(gradients[gi2], x2, y2);
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2);
    }

    private int floor(double x) {
        return x > 0 ? (int) x : (int) x - 1;
    }

    private double dotProduct(int[] gradient, double x, double y) {
        return gradient[0] * x + gradient[1] * y;
    }

}
