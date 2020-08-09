import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    // 能耗
    private double[] energy;
    private double aveEnergy;
    private double sdEnergy;
    private String aveEnergyPath;
    private String energyPath;

    // PM的CPU和内存利用率
    private double[] aveCpuMemUtil;
    private double[] sdCpuMemUtil;
    private String aveCpuMemUtilPath;

    // 使用的PM数量
    private double aveNumOfPm;
    private double sdNumOfPm;
    private String aveNumOfPmPath;

    // 使用的vm的平均数量
    private double aveNumOfVm;
    private double sdNumOfVm;
    private String aveNumOfVmPath;

    // 收敛曲线是每一代的平均适应度在所有运行
    private double[] convergenceCurve;
    private String convergenceCurvePath;

    // 执行时间的平均值和Sd(每次运行)
    private double aveTime;
    private double sdTime;
    private String aveTimePath;



    private WriteFile(Builder builder){
        energy = builder.energy;
        aveEnergy = builder.aveEnergy;
        sdEnergy = builder.sdEnergy;
        aveEnergyPath = builder.aveEnergyPath;
        energyPath = builder.energyPath;

        aveCpuMemUtil = builder.aveCpuMemUtil;
        sdCpuMemUtil = builder.sdCpuMemUtil;
        aveCpuMemUtilPath = builder.aveCpuMemUtilPath;

        aveNumOfPm = builder.aveNumOfPm;
        sdNumOfPm = builder.sdNumOfPm;
        aveNumOfPmPath = builder.aveNumOfPmPath;

        aveNumOfVm = builder.aveNumOfVm;
        sdNumOfVm = builder.sdNumOfVm;
        aveNumOfVmPath = builder.aveNumOfVmPath;

        convergenceCurve = builder.convergenceCurve;
        convergenceCurvePath = builder.convergenceCurvePath;

        aveTime = builder.aveTime;
        sdTime = builder.sdTime;
        aveTimePath = builder.aveTimePath;
    }


    public WriteFile writeAveSdEnergy() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(aveEnergyPath));
        writer.write(String.valueOf(aveEnergy) + "," + String.valueOf(sdEnergy) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeEnergy() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(energyPath));
        for(int i = 0; i < energy.length; i++){
            writer.write(String.valueOf(energy[i]) + "\n");
        }
        writer.close();
        return this;
    }

    public WriteFile writeAveSdCpuMemUtil() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(aveCpuMemUtilPath));
        writer.write(String.valueOf(aveCpuMemUtil[0]) + "," + String.valueOf(sdCpuMemUtil[0]) + "\n");
        writer.write(String.valueOf(aveCpuMemUtil[1]) + "," + String.valueOf(sdCpuMemUtil[1]) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeAveSdNumOfPm() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(aveNumOfPmPath));
        writer.write(String.valueOf(aveNumOfPm) + "," + String.valueOf(sdNumOfPm) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeAveSdNumOfVm() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(aveNumOfVmPath));
        writer.write(String.valueOf(aveNumOfVm) + "," + String.valueOf(sdNumOfVm) + "\n");
        writer.close();
        return this;
    }

    public WriteFile writeConvergenceCurve() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(convergenceCurvePath));
        writer.write(String.valueOf(convergenceCurve[0]) + "\n");
        for(int i = 1; i < convergenceCurve.length; i++){
            writer.append(String.valueOf(convergenceCurve[i]) + "\n");
        }
        writer.close();
        return this;
    }

    public WriteFile writeAveSdTime() throws  IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(aveTimePath));
        writer.write(String.valueOf(aveTime) + "," + String.valueOf(sdTime) + "\n");
        writer.close();
        return this;
    }


    public static class Builder {
        private double[] energy;
        private double aveEnergy; // required
        private double sdEnergy;
        private String aveEnergyPath; // required
        private String energyPath;

        // PM cpu和 内存 利用率
        private double[] aveCpuMemUtil;
        private double[] sdCpuMemUtil;
        private String aveCpuMemUtilPath;

        // average number of used PMs
        private double aveNumOfPm;
        private double sdNumOfPm;
        private String aveNumOfPmPath;

        // 使用的vm的平均数量
        private double aveNumOfVm;
        private double sdNumOfVm;
        private String aveNumOfVmPath;

        // 收敛曲线是每一代的平均适应度在所有运行
        private double[] convergenceCurve;
        private String convergenceCurvePath;

        // 平均和sd时间(每次运行)
        private double aveTime;
        private double sdTime;
        private String aveTimePath;

        // 惟一必需的字段包括在构造函数中
        public Builder(
                        double[] energy,
                        String energyPath,
                        double aveEnergy,
                        double sdEnergy,
                        String aveEnergyPath
                        ){
            this.energy = energy;
            this.energyPath = energyPath;
            this.aveEnergy = aveEnergy;
            this.sdEnergy = sdEnergy;
            this.aveEnergyPath = aveEnergyPath;
        }

        public Builder setAveCpuMemUtil(double[] aveCpuMemUtil,
                                        double[] sdCpuMemUtil,
                                        String aveCpuMemUtilPath){
            this.aveCpuMemUtil = aveCpuMemUtil;
            this.sdCpuMemUtil = sdCpuMemUtil;
            this.aveCpuMemUtilPath = aveCpuMemUtilPath;
            return this;
        }

        public Builder setAveNumOfPm(double aveNumOfPm,
                                     double sdNumOfPm,
                                     String aveNumOfPmPath){
            this.aveNumOfPm = aveNumOfPm;
            this.sdNumOfPm = sdNumOfPm;
            this.aveNumOfPmPath = aveNumOfPmPath;
            return this;
        }

        public Builder setAveNumOfVm(double aveNumOfVm,
                                     double sdNumOfVm,
                                     String aveNumOfVmPath){
            this.aveNumOfVm = aveNumOfVm;
            this.sdNumOfVm = sdNumOfVm;
            this.aveNumOfVmPath = aveNumOfVmPath;
            return this;
        }

        public Builder setConvergenceCurve(double[] convergenceCurve,
                                           String convergenceCurvePath){
            this.convergenceCurve = convergenceCurve;
            this.convergenceCurvePath = convergenceCurvePath;
            return this;
        }

        public Builder setAveTime(double aveTime,
                                  double sdTime,
                                  String aveTimePath){
            this.aveTime = aveTime;
            this.sdTime = sdTime;
            this.aveTimePath = aveTimePath;
            return this;
        }

        //  build method
        public WriteFile build(){
            return new WriteFile(this);
        }

    }
}
