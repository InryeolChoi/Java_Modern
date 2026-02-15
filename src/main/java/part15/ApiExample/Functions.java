package part15.ApiExample;

import java.util.function.IntConsumer;

public class Functions {

    /* ============================================================
       1️⃣ 동기 버전 f (Lorenz + RK4 + hash)
    ============================================================ */
    public static int f(int x) {
        final double sigma = 10.0;
        final double rho   = 28.0;
        final double beta  = 8.0 / 3.0;

        double X = (x % 97) * 0.01 + 1.0;
        double Y = ((x / 97) % 97) * 0.01 + 1.0;
        double Z = ((x / (97 * 97)) % 97) * 0.01 + 1.0;

        final double dt = 0.0025;
        final int steps = 200_000;

        long acc = 0x9E3779B97F4A7C15L;

        for (int i = 0; i < steps; i++) {

            double k1x = sigma * (Y - X);
            double k1y = X * (rho - Z) - Y;
            double k1z = X * Y - beta * Z;

            double x2 = X + 0.5 * dt * k1x;
            double y2 = Y + 0.5 * dt * k1y;
            double z2 = Z + 0.5 * dt * k1z;

            double k2x = sigma * (y2 - x2);
            double k2y = x2 * (rho - z2) - y2;
            double k2z = x2 * y2 - beta * z2;

            double x3 = X + 0.5 * dt * k2x;
            double y3 = Y + 0.5 * dt * k2y;
            double z3 = Z + 0.5 * dt * k2z;

            double k3x = sigma * (y3 - x3);
            double k3y = x3 * (rho - z3) - y3;
            double k3z = x3 * y3 - beta * z3;

            double x4 = X + dt * k3x;
            double y4 = Y + dt * k3y;
            double z4 = Z + dt * k3z;

            double k4x = sigma * (y4 - x4);
            double k4y = x4 * (rho - z4) - y4;
            double k4z = x4 * y4 - beta * z4;

            X += (dt / 6.0) * (k1x + 2*k2x + 2*k3x + k4x);
            Y += (dt / 6.0) * (k1y + 2*k2y + 2*k3y + k4y);
            Z += (dt / 6.0) * (k1z + 2*k2z + 2*k3z + k4z);

            if ((i & 1023) == 0) {
                long hx = Double.doubleToLongBits(X);
                long hy = Double.doubleToLongBits(Y);
                long hz = Double.doubleToLongBits(Z);
                long m = hx ^ hy ^ hz ^ i;
                acc ^= m;
                acc *= 0x9E3779B97F4A7C15L;
                acc ^= (acc >>> 27);
            }
        }

        acc ^= (acc >>> 33);
        return (int)(acc ^ (acc >>> 32));
    }

    /* ============================================================
       2️⃣ 콜백 버전 f
    ============================================================ */
    public static void f(int x, IntConsumer callback) {
        new Thread(() -> {
            int result = f(x);   // 동일 계산
            callback.accept(result);
        }).start();
    }


    /* ============================================================
       3️⃣ 동기 버전 g (Matrix + Power Iteration + hash)
    ============================================================ */
    public static int g(int x) {

        final int n = 56 + (Math.abs(x) % 9);
        long state = (x * 0x9E3779B9L) ^ 0xD1B54A32D192ED03L;

        double[] A = new double[n * n];

        for (int i = 0; i < A.length; i++) {
            state ^= (state >>> 12);
            state ^= (state << 25);
            state ^= (state >>> 27);
            long r = state * 0x2545F4914F6CDD1DL;
            A[i] = (((r >>> 11) * (1.0 / (1L << 53))) * 2.0 - 1.0) * 0.15;
        }

        for (int i = 0; i < n; i++) {
            A[i*n + i] += 1.25;
        }

        double[] v = new double[n];
        double[] w = new double[n];

        for (int i = 0; i < n; i++) {
            v[i] = 1.0 / n;
        }

        double lambda = 0.0;

        for (int it = 0; it < 6000; it++) {

            for (int i = 0; i < n; i++) {
                double sum = 0.0;
                for (int j = 0; j < n; j++) {
                    sum += A[i*n + j] * v[j];
                }
                w[i] = sum;
            }

            double num = 0.0, den = 0.0;
            for (int i = 0; i < n; i++) {
                num += v[i] * w[i];
                den += v[i] * v[i];
            }

            lambda = num / (den + 1e-12);

            double norm = 0.0;
            for (int i = 0; i < n; i++) norm += w[i]*w[i];
            norm = Math.sqrt(norm) + 1e-12;

            for (int i = 0; i < n; i++) v[i] = w[i] / norm;
        }

        long bits = Double.doubleToLongBits(lambda);
        return (int)(bits ^ (bits >>> 32));
    }

    /* ============================================================
       4️⃣ 콜백 버전 g
    ============================================================ */
    public static void g(int x, IntConsumer callback) {
        new Thread(() -> {
            int result = g(x);   // 동일 계산
            callback.accept(result);
        }).start();
    }
}