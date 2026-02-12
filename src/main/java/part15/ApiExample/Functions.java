package part15.ApiExample;

public class Functions {

    // f(x): 비선형 미분방정식(로렌츠 시스템) RK4 적분 -> int로 해시
    public static int f(int x) {
        // Lorenz parameters (classic)
        final double sigma = 10.0;
        final double rho   = 28.0;
        final double beta  = 8.0 / 3.0;

        // initial condition derived from x (deterministic)
        double X = (x % 97) * 0.01 + 1.0;
        double Y = ((x / 97) % 97) * 0.01 + 1.0;
        double Z = ((x / (97 * 97)) % 97) * 0.01 + 1.0;

        // step size and number of steps (heavy)
        final double dt = 0.0025;
        final int steps = 200_000; // 꽤 무겁게

        long acc = 0x9E3779B97F4A7C15L; // mixing accumulator

        for (int i = 0; i < steps; i++) {
            // Lorenz derivatives
            // dX = sigma*(Y - X)
            // dY = X*(rho - Z) - Y
            // dZ = X*Y - beta*Z

            // RK4
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

            X += (dt / 6.0) * (k1x + 2.0 * k2x + 2.0 * k3x + k4x);
            Y += (dt / 6.0) * (k1y + 2.0 * k2y + 2.0 * k3y + k4y);
            Z += (dt / 6.0) * (k1z + 2.0 * k2z + 2.0 * k3z + k4z);

            // occasional mixing (avoid NaN drift; keep deterministic)
            if ((i & 1023) == 0) {
                long hx = Double.doubleToLongBits(X);
                long hy = Double.doubleToLongBits(Y);
                long hz = Double.doubleToLongBits(Z);
                long m = hx ^ (hy * 0xBF58476D1CE4E5B9L) ^ (hz * 0x94D049BB133111EBL) ^ i;
                acc ^= m;
                acc *= 0x9E3779B97F4A7C15L;
                acc ^= (acc >>> 27);
            }
        }

        // fold to int
        acc ^= (acc >>> 33);
        acc *= 0xC2B2AE3D27D4EB4FL;
        acc ^= (acc >>> 29);
        return (int) (acc ^ (acc >>> 32));
    }

    // g(x): seed로 행렬 생성 -> power iteration으로 주고유값 근사 -> int로 해시
    public static int g(int x) {
        // matrix size (moderate but heavy)
        // 48~64 정도면 꽤 빡셈. (너무 키우면 진짜 오래 걸려서 이 정도로)
        final int n = 56 + (Math.abs(x) % 9); // 56~64

        // pseudo-random generator state from x (deterministic)
        long state = (x * 0x9E3779B9L) ^ 0xD1B54A32D192ED03L;

        // build a dense matrix A (stored row-major)
        double[] A = new double[n * n];
        for (int i = 0; i < n * n; i++) {
            // xorshift64*
            state ^= (state >>> 12);
            state ^= (state << 25);
            state ^= (state >>> 27);
            long r = state * 0x2545F4914F6CDD1DL;

            // map to (-1, 1) and bias diagonal to make spectral radius stable
            double v = ((r >>> 11) * (1.0 / (1L << 53))) * 2.0 - 1.0;
            A[i] = v * 0.15;
        }
        for (int i = 0; i < n; i++) {
            A[i * n + i] += 1.25; // diagonal dominance
        }

        // power iteration: v <- A v / ||A v||
        double[] v = new double[n];
        double[] w = new double[n];

        // init v from x
        for (int i = 0; i < n; i++) {
            state ^= (state >>> 12);
            state ^= (state << 25);
            state ^= (state >>> 27);
            long r = state * 0x2545F4914F6CDD1DL;
            v[i] = (((r >>> 11) * (1.0 / (1L << 53))) * 2.0 - 1.0);
        }

        // normalize v
        double norm = 0.0;
        for (int i = 0; i < n; i++) norm += v[i] * v[i];
        norm = Math.sqrt(norm) + 1e-12;
        for (int i = 0; i < n; i++) v[i] /= norm;

        final int iters = 6_000; // 꽤 무겁게
        double lambda = 0.0;

        for (int it = 0; it < iters; it++) {
            // w = A v
            for (int i = 0; i < n; i++) {
                double sum = 0.0;
                int row = i * n;
                for (int j = 0; j < n; j++) {
                    sum += A[row + j] * v[j];
                }
                w[i] = sum;
            }

            // Rayleigh quotient approx: lambda = v^T w
            double num = 0.0;
            double den = 0.0;
            for (int i = 0; i < n; i++) {
                num += v[i] * w[i];
                den += v[i] * v[i];
            }
            lambda = num / (den + 1e-12);

            // normalize w -> v
            double wn = 0.0;
            for (int i = 0; i < n; i++) wn += w[i] * w[i];
            wn = Math.sqrt(wn) + 1e-12;
            for (int i = 0; i < n; i++) v[i] = w[i] / wn;

            // tiny perturb to avoid stagnation on some seeds
            if ((it & 255) == 0) {
                v[it % n] += 1e-9;
            }
        }

        long bits = Double.doubleToLongBits(lambda);
        long mix = bits ^ (bits >>> 33);
        mix *= 0xFF51AFD7ED558CCDL;
        mix ^= (mix >>> 33);
        mix *= 0xC4CEB9FE1A85EC53L;
        mix ^= (mix >>> 33);

        return (int) (mix ^ (mix >>> 32));
    }
}