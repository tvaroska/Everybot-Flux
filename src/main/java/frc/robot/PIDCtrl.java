package frc.robot;

public class PIDCtrl {
    public static double DefaultDifTime = 0.002;  //sec

    private double kP;
    private double kD;
    private double kI;
    private double dTime;

    private double error1;
    private double error2;

    public PIDCtrl(double kp, double kd, double ki, double dtime) {
        error1 = error2 = 0.0;
        kP = kp;
        kD = kd;
        kI = ki;
        dTime = dtime;
        if (dTime == 0)
            dTime = DefaultDifTime;
    }

    public void reset() {
        error1 = error2 = 0.0;
    }

    public void pid(double kp, double kd, double ki, double dtime) {
        dTime = dtime;
        if (dTime == 0)
            dTime = DefaultDifTime;
    }

    public void delta(double dtime) {
        dTime = dtime;
        if (dTime == 0)
            dTime = DefaultDifTime;
    }

    public double calculateDif(double value, double target) {
        double err = target - value;
//        double res = (kP + kI * dTime + kD / dTime) * err - (kP + 2 * kD / dTime) * error1 + kD / dTime * error2;
        double res = (kP + kI * dTime) * err - kP * error1 + (err + 2 * error1 + error2) * kD / dTime;
        error2 = error1;
        error1 = err;
//        output += res;
        return res;
    }

    public double calculateDif2(double value, double target) {
        double err = target - value;
        error2 += err * dTime;  // error2 keeps integral part
        double res = kP * err + kI * error2 + kD * (err - error1) / dTime;
        error1 = err;
//        output += res;
        return res;
    }

    static public double limitRange(double x, double xMax) {
        if (xMax < 0)
            xMax = -xMax;
        if (x > xMax)
            x = xMax;
        if (x < -xMax)
            x = -xMax;
        return x;
    }
}
