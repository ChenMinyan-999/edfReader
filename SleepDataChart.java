import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SleepDataChart {
    // 常量定义，避免硬编码数字
    // 窗口相关常量
    private static final int FRAME_WIDTH = 1000;           // 主窗口宽度
    private static final int FRAME_HEIGHT = 800;          // 主窗口高度
    private static final int SPLIT_PANE_INITIAL_DIVIDER = 400;  // 分割面板初始分割位置
    private static final double SPLIT_PANE_RESIZE_WEIGHT = 0.5; // 分割面板调整权重

    // 数据相关常量
    private static final int TEST_DATA_DURATION_MINUTES = 8 * 60; // 测试数据时长(8小时)
    private static final int OXYGEN_THRESHOLD = 90;              // 血氧饱和度阈值(低于此值标记为异常)

    // 文件相关常量
    private static final String DEFAULT_FILE_NAME_PREFIX = "sleep_data_"; // 测试文件默认前缀
    private static final String FILE_EXTENSION = ".edf";                   // 文件扩展名
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";           // 时间戳格式

    private JFrame frame;
    private SleepPanel chartPanel;
    private OxygenPanel oxygenPanel;
    private JFileChooser fileChooser;
    private String currentFilePath;

    public SleepDataChart() {
        // 初始化主窗口
        frame = new JFrame("睡眠数据可视化工具");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);

        // 初始化文件选择器
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return "EDF 文件 (*" + FILE_EXTENSION + ")";
            }
        });

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建顶部控制面板
        JPanel controlPanel = createControlPanel();

        // 使用垂直分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        chartPanel = new SleepPanel();
        oxygenPanel = new OxygenPanel();

        splitPane.setTopComponent(chartPanel);
        splitPane.setBottomComponent(oxygenPanel);
        splitPane.setDividerLocation(SPLIT_PANE_INITIAL_DIVIDER);
        splitPane.setResizeWeight(SPLIT_PANE_RESIZE_WEIGHT);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createControlPanel() {
        // 创建控制面板，设置背景色和边距
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 文件操作按钮
        JButton openButton = new JButton("打开文件");
        JButton generateButton = new JButton("生成测试数据");
        JButton helpButton = new JButton("帮助");

        // 当前文件标签
        JLabel fileLabel = new JLabel("当前文件: 未选择文件");

        // 状态标签
        JLabel statusLabel = new JLabel("就绪");

        // 添加按钮事件
        openButton.addActionListener(e -> openFile());
        generateButton.addActionListener(e -> generateTestData());
        helpButton.addActionListener(e -> showHelpDialog());

        // 添加组件到面板
        panel.add(openButton);
        panel.add(generateButton);
        panel.add(helpButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(fileLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(statusLabel);

        return panel;
    }

    private void openFile() {
        // 打开文件选择对话框
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                currentFilePath = selectedFile.getAbsolutePath();
                List<DataPoint> data = EDFProcessor.readSleepDataEDFFile(currentFilePath);
                chartPanel.setData(data);
                oxygenPanel.setData(data);
                showStatus("已加载文件: " + selectedFile.getName());
            } catch (Exception ex) {
                showErrorDialog("读取文件时出错: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void generateTestData() {
        try {
            // 创建带有时间戳的文件名
            String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            String filePath = DEFAULT_FILE_NAME_PREFIX + timestamp + FILE_EXTENSION;

            // 调用数据生成方法，生成指定时长的测试数据
            EDFProcessor.createTestEDFDataFile(filePath, TEST_DATA_DURATION_MINUTES);

            // 加载生成的文件
            List<DataPoint> data = EDFProcessor.readSleepDataEDFFile(filePath);
            chartPanel.setData(data);
            oxygenPanel.setData(data);
            currentFilePath = filePath;
            showStatus("已生成并加载测试数据: " + filePath);
        } catch (Exception ex) {
            showErrorDialog("生成测试数据时出错: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showHelpDialog() {
        // 构建帮助信息字符串
        String helpMessage =
                "睡眠数据可视化工具帮助\n\n" +
                        "1. 打开文件: 点击'打开文件'按钮选择已有的EDF睡眠数据文件\n" +
                        "2. 生成测试数据: 点击'生成测试数据'按钮创建模拟EDF数据文件\n" +
                        "3. 图表交互:\n" +
                        "   - 使用鼠标滚轮缩放图表\n" +
                        "   - 按住鼠标左键拖动图表\n" +
                        "4. 数据说明:\n" +
                        "   - 上方图表显示脑电信号，血氧饱和度低于" + OXYGEN_THRESHOLD + "%的部分用红色标记\n" +
                        "   - 下方图表显示血氧饱和度，绿色曲线表示血氧水平，红色横线为" + OXYGEN_THRESHOLD + "%参考线\n" +
                        "5. 文件格式:\n" +
                        "   - 使用标准EDF格式存储数据\n" +
                        "   - 包含两个通道：脑电数据和血氧饱和度";

        JOptionPane.showMessageDialog(frame, helpMessage, "帮助", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStatus(String message) {
        // 在实际应用中可以更新状态栏
        System.out.println(message);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SleepDataChart());
    }
}