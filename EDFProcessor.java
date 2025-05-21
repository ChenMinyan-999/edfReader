import java.io.*;
import java.util.*;
import java.util.Calendar;

// 假设这些类在EDF库中定义
//import EDFreader;
//import EDFwriter;
//import EDFException;
//import EDFAnnotationStruct;

class EDFProcessor {
    // 常量定义区域
    private static final int MIN_REQUIRED_SIGNALS = 2;                 // 最低需要的信号通道数
    private static final int EEG_CHANNEL_INDEX = 0;                    // EEG信号通道索引
    private static final int SPO2_CHANNEL_INDEX = 1;                   // 血氧饱和度信号通道索引
    private static final int EEG_SAMPLES_PER_SECOND = 60;              // EEG每秒采样数
    private static final int SPO2_SAMPLES_PER_SECOND = 1;              // 血氧饱和度每秒采样数
    private static final int MAX_EEG_VALUE = 255;                       // EEG信号最大值
    private static final int MIN_EEG_VALUE = 0;                         // EEG信号最小值
    private static final int MAX_SPO2_VALUE = 100;                      // 血氧饱和度最大值
    private static final int MIN_SPO2_VALUE = 80;                       // 血氧饱和度最小值
    private static final int FULL_MINUTES_IN_HOUR = 60;                 // 每小时的分钟数
    private static final int FULL_SECONDS_IN_MINUTE = 60;               // 每分钟的秒数
    private static final int RECORDING_DURATION_HOURS = 8;              // 模拟记录的总小时数
    private static final int RECORDING_DURATION_MINUTES = RECORDING_DURATION_HOURS * FULL_MINUTES_IN_HOUR; // 模拟记录的总分钟数
    private static final int INITIAL_AWAKE_DURATION_MINUTES = 30;       // 初始清醒状态持续时间（分钟）
    private static final int DEEP_SLEEP_CYCLE_DURATION_MINUTES = 90;    // 深度睡眠周期持续时间（分钟）
    private static final int DEEP_SLEEP_PHASE_DURATION_MINUTES = 60;    // 深度睡眠阶段持续时间（分钟）
    private static final int FINAL_AWAKE_DURATION_MINUTES = 30;          // 最终清醒状态持续时间（分钟）
    private static final int MIN_APNEA_DURATION_SECONDS = 10;            // 最小呼吸暂停持续时间（秒）
    private static final int MAX_APNEA_DURATION_SECONDS = 60;            // 最大呼吸暂停持续时间（秒）
    private static final int MAX_APNEA_EFFECT = 25;                        // 呼吸暂停对血氧的最大影响百分比
    private static final double APNEA_RECOVERY_FACTOR = 0.7;              // 呼吸暂停恢复因子
    private static final double OXYGEN_BASE_AWAKE = 96;                    // 清醒状态下的基础血氧饱和度
    private static final double OXYGEN_BASE_DEEP_SLEEP = 94;               // 深度睡眠状态下的基础血氧饱和度
    private static final double HIGH_FREQ_NOISE_AMPLITUDE = 10;           // EEG高频噪声振幅
    private static final double LOW_FREQ_DELTA_WAVE_AMPLITUDE = 30;       // 深度睡眠时的低频delta波振幅
    private static final double LOW_FREQ_REM_WAVE_AMPLITUDE = 20;         // REM睡眠时的低频波振幅
    private static final double NATURAL_OXYGEN_FLUCTUATION = 0.5;         // 血氧自然波动幅度

    // 睡眠阶段常量
    private static final int AWAKE_STAGE = 0;                              // 清醒状态
    private static final int LIGHT_SLEEP_STAGE = 1;                        // 浅睡眠状态
    private static final int DEEP_SLEEP_STAGE = 2;                        // 深度睡眠状态
    private static final int REM_SLEEP_STAGE = 3;                          // REM睡眠状态

    public static List<DataPoint> readSleepDataEDFFile(String filePath) throws IOException, EDFException {
        EDFreader edfReader = new EDFreader(filePath);
        List<DataPoint> data = new ArrayList<>();

        // 验证是否至少有2个信号通道(EEG和血氧)
        int numSignals = edfReader.getNumSignals();
        if (numSignals < MIN_REQUIRED_SIGNALS) {
            throw new IOException("EDF文件必须包含至少2个信号通道 (脑电和血氧)");
        }

        // 获取每个数据记录中的样本数
        int eegSamplesPerRecord = edfReader.getSampelsPerDataRecord(EEG_CHANNEL_INDEX); // EEG信号
        int oxygenSamplesPerRecord = edfReader.getSampelsPerDataRecord(SPO2_CHANNEL_INDEX); // 血氧饱和度信号
        long numDataRecords = edfReader.getNumDataRecords();

        // 读取所有数据记录
        for (long i = 0; i < numDataRecords; i++) {
            // 读取EEG样本
            double[] eegBuffer = new double[eegSamplesPerRecord];
            edfReader.readPhysicalSamples(EEG_CHANNEL_INDEX, eegBuffer);

            // 读取血氧饱和度样本
            double[] oxygenBuffer = new double[oxygenSamplesPerRecord];
            edfReader.readPhysicalSamples(SPO2_CHANNEL_INDEX, oxygenBuffer);

            // 添加数据点(使用每个记录的相同样本数)
            int minSamples = Math.min(eegSamplesPerRecord, oxygenSamplesPerRecord);
            for (int j = 0; j < minSamples; j++) {
                // 将物理值转换为适当的范围(0-255)
                int eegSignal = (int)Math.round(eegBuffer[j]);
                int oxygenSaturation = (int)Math.round(oxygenBuffer[j]);

                // 确保值在范围内
                eegSignal = Math.max(MIN_EEG_VALUE, Math.min(MAX_EEG_VALUE, eegSignal));
                oxygenSaturation = Math.max(MIN_SPO2_VALUE, Math.min(MAX_SPO2_VALUE, oxygenSaturation));

                data.add(new DataPoint(eegSignal, oxygenSaturation));
            }
        }

        // 关闭EDF读取器
        edfReader.close();

        return data;
    }

    public static void createTestEDFDataFile(String filePath, int totalMinutes) throws IOException, EDFException {
        // 创建具有2个信号的EDF写入器: EEG和血氧饱和度
        EDFwriter edfWriter = new EDFwriter(filePath, EDFwriter.EDFLIB_FILETYPE_EDFPLUS, MIN_REQUIRED_SIGNALS);

        // 设置采样频率
        edfWriter.setSampleFrequency(EEG_CHANNEL_INDEX, EEG_SAMPLES_PER_SECOND); // EEG
        edfWriter.setSampleFrequency(SPO2_CHANNEL_INDEX, SPO2_SAMPLES_PER_SECOND); // 血氧饱和度

        // 设置信号标签
        edfWriter.setSignalLabel(EEG_CHANNEL_INDEX, "EEG");
        edfWriter.setSignalLabel(SPO2_CHANNEL_INDEX, "SpO2");

        // 设置物理维度
        edfWriter.setPhysicalDimension(EEG_CHANNEL_INDEX, "uV");
        edfWriter.setPhysicalDimension(SPO2_CHANNEL_INDEX, "%");

        // 设置物理最小值/最大值
        edfWriter.setPhysicalMinimum(EEG_CHANNEL_INDEX, MIN_EEG_VALUE);
        edfWriter.setPhysicalMaximum(EEG_CHANNEL_INDEX, MAX_EEG_VALUE);
        edfWriter.setPhysicalMinimum(SPO2_CHANNEL_INDEX, MIN_SPO2_VALUE);
        edfWriter.setPhysicalMaximum(SPO2_CHANNEL_INDEX, MAX_SPO2_VALUE);

        // 设置数字最小值/最大值
        edfWriter.setDigitalMinimum(EEG_CHANNEL_INDEX, MIN_EEG_VALUE);
        edfWriter.setDigitalMaximum(EEG_CHANNEL_INDEX, MAX_EEG_VALUE);
        edfWriter.setDigitalMinimum(SPO2_CHANNEL_INDEX, MIN_SPO2_VALUE);
        edfWriter.setDigitalMaximum(SPO2_CHANNEL_INDEX, MAX_SPO2_VALUE);

        // 设置传感器类型
        edfWriter.setTransducer(EEG_CHANNEL_INDEX, "AgAgCl电极");
        edfWriter.setTransducer(SPO2_CHANNEL_INDEX, "脉搏血氧仪");

        // 设置预滤波器
        edfWriter.setPreFilter(EEG_CHANNEL_INDEX, "HP:0.1Hz LP:75Hz");
        edfWriter.setPreFilter(SPO2_CHANNEL_INDEX, "无");

        // 设置患者信息
        edfWriter.setPatientName("测试患者");
        edfWriter.setPatientCode("PAT001");

        // 设置记录信息
        edfWriter.setAdministrationCode("ADMIN001");
        edfWriter.setTechnician("技术人员");
        edfWriter.setEquipment("睡眠监测仪");

        // 设置记录开始日期和时间
        Calendar cal = Calendar.getInstance();
        edfWriter.setStartDateTime(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1, // Calendar月份从0开始
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                0
        );

        Random random = new Random();

        // 模拟睡眠阶段(清醒、浅睡眠、深睡眠、REM)
        List<Integer> sleepStages = new ArrayList<>();
        // 前30分钟可能处于清醒状态
        for (int i = 0; i < INITIAL_AWAKE_DURATION_MINUTES; i++) {
            sleepStages.add(random.nextInt(LIGHT_SLEEP_STAGE + 1)); // 0:清醒, 1:浅睡眠
        }
        // 中间6小时交替出现浅睡眠、深睡眠和REM
        for (int i = 0; i < RECORDING_DURATION_MINUTES - INITIAL_AWAKE_DURATION_MINUTES - FINAL_AWAKE_DURATION_MINUTES; i++) {
            if (i % DEEP_SLEEP_CYCLE_DURATION_MINUTES < DEEP_SLEEP_PHASE_DURATION_MINUTES) {
                sleepStages.add(random.nextInt(DEEP_SLEEP_STAGE) + LIGHT_SLEEP_STAGE); // 1:浅睡眠, 2:深睡眠
            } else {
                sleepStages.add(REM_SLEEP_STAGE); // 3:REM
            }
        }
        // 最后30分钟逐渐清醒
        for (int i = 0; i < FINAL_AWAKE_DURATION_MINUTES; i++) {
            sleepStages.add(Math.min(AWAKE_STAGE, random.nextInt(DEEP_SLEEP_STAGE))); // 0:清醒, 1:浅睡眠
        }

        // 添加一些随机的呼吸暂停事件(每小时1-3次)
        List<Integer> apneaEvents = new ArrayList<>();
        for (int hour = 0; hour < RECORDING_DURATION_HOURS; hour++) {
            int numApneas = 1 + random.nextInt(5);
            for (int i = 0; i < numApneas; i++) {
                int minute = hour * FULL_MINUTES_IN_HOUR + random.nextInt(FULL_MINUTES_IN_HOUR);
                int duration = MIN_APNEA_DURATION_SECONDS + random.nextInt(MAX_APNEA_DURATION_SECONDS - MIN_APNEA_DURATION_SECONDS); // 10-40秒
                apneaEvents.add(minute);
                apneaEvents.add(duration);
            }
        }

        // 为指定持续时间的每秒写入数据
        for (int minute = 0; minute < totalMinutes; minute++) {
            // 当前睡眠阶段
            int sleepStage = sleepStages.get(Math.min(minute, sleepStages.size() - 1));

            // 检查是否有呼吸暂停事件
            boolean isApnea = false;
            int apneaDuration = 0;
            for (int i = 0; i < apneaEvents.size(); i += 2) {
                int eventMinute = apneaEvents.get(i);
                int eventDuration = apneaEvents.get(i + 1);
                if (minute >= eventMinute && minute < eventMinute + eventDuration / FULL_MINUTES_IN_HOUR) {
                    isApnea = true;
                    apneaDuration = eventDuration;
                    break;
                }
            }

            for (int second = 0; second < FULL_SECONDS_IN_MINUTE; second++) {
                // 计算呼吸暂停影响
                double apneaEffect = 0;
                if (isApnea) {
                    int secondsIntoApnea = (minute - apneaEvents.get(apneaEvents.indexOf(apneaDuration) - 1)) * FULL_SECONDS_IN_MINUTE + second;
                    double progress = (double)secondsIntoApnea / apneaDuration;
                    if (progress < APNEA_RECOVERY_FACTOR) {
                        apneaEffect = progress * MAX_APNEA_EFFECT; // 最大下降25%
                    } else {
                        apneaEffect = MAX_APNEA_EFFECT - (progress - APNEA_RECOVERY_FACTOR) * MAX_APNEA_EFFECT * 2; // 快速恢复
                    }
                }

                // 计算时间因子(用于EEG模拟)
                double timeFactor = Math.sin((minute * FULL_SECONDS_IN_MINUTE + second) * 2 * Math.PI / (totalMinutes * FULL_SECONDS_IN_MINUTE / 4));

                // 基于睡眠阶段的EEG基础值
                double eegBase = 0;
                switch (sleepStage) {
                    case AWAKE_STAGE: // 清醒
                        eegBase = 150 + random.nextInt(30);
                        break;
                    case LIGHT_SLEEP_STAGE: // 浅睡眠
                        eegBase = 120 + random.nextInt(20);
                        break;
                    case DEEP_SLEEP_STAGE: // 深睡眠
                        eegBase = 80 + random.nextInt(40);
                        break;
                    case REM_SLEEP_STAGE: // REM
                        eegBase = 140 + random.nextInt(40);
                        break;
                }

                // 写入一秒的EEG数据(60个样本)
                double[] eegBuffer = new double[EEG_SAMPLES_PER_SECOND];
                for (int i = 0; i < EEG_SAMPLES_PER_SECOND; i++) {
                    // 模拟EEG信号，添加高频波动和睡眠阶段特征
                    double highFreqNoise = random.nextGaussian() * HIGH_FREQ_NOISE_AMPLITUDE;
                    double signal = eegBase + highFreqNoise;

                    // 添加低频波动(取决于睡眠阶段)
                    if (sleepStage == DEEP_SLEEP_STAGE) { // 深睡眠有明显的低频delta波
                        signal += LOW_FREQ_DELTA_WAVE_AMPLITUDE * Math.sin(i * 2 * Math.PI / EEG_SAMPLES_PER_SECOND * 1); // 1Hz
                    } else if (sleepStage == REM_SLEEP_STAGE) { // REM睡眠有快速眼动
                        signal += LOW_FREQ_REM_WAVE_AMPLITUDE * Math.sin(i * 2 * Math.PI / EEG_SAMPLES_PER_SECOND * 5); // 5Hz
                    }

                    eegBuffer[i] = Math.max(MIN_EEG_VALUE, Math.min(MAX_EEG_VALUE, signal));
                }
                edfWriter.writePhysicalSamples(eegBuffer);

                // 写入一秒的血氧饱和度数据(1个样本)
                double[] oxygenBuffer = new double[SPO2_SAMPLES_PER_SECOND];

                // 基于睡眠阶段的血氧饱和度基础值
                double oxygenBase = (sleepStage == DEEP_SLEEP_STAGE || sleepStage == REM_SLEEP_STAGE) ?
                        OXYGEN_BASE_DEEP_SLEEP : OXYGEN_BASE_AWAKE;

                // 添加呼吸暂停影响
                if (isApnea) {
                    oxygenBase -= apneaEffect;
                }

                // 添加自然波动
                oxygenBase += random.nextGaussian() * NATURAL_OXYGEN_FLUCTUATION;

                oxygenBuffer[0] = Math.min(MAX_SPO2_VALUE, oxygenBase);

                edfWriter.writePhysicalSamples(oxygenBuffer);
            }
        }

        // 添加注释
        edfWriter.writeAnnotation(0, -1, "记录开始");
        edfWriter.writeAnnotation(totalMinutes * FULL_SECONDS_IN_MINUTE * EDFwriter.EDFLIB_TIME_DIMENSION, -1, "记录结束");

        // 关闭文件
        edfWriter.close();
    }
}