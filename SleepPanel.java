import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * 睡眠监测面板，用于显示和交互脑电信号数据
 */
class SleepPanel extends JPanel {
    // 数据和视图状态
    protected List<DataPoint> data;
    protected double zoomFactor = 1.0;
    protected int offsetX = 0;
    private Point dragStart;

    // 缩放阈值常量
    private static final double MINUTES_SCALE_THRESHOLD = 4.0; // 分钟刻度显示阈值
    private static final double FINE_MINUTES_SCALE_THRESHOLD = 8.0; // 精细分钟刻度显示阈值

    // 界面常量
    private static final int GRID_HORIZONTAL_DIVISIONS = 5; // 水平网格线数量
    private static final int TOTAL_DISPLAY_HOURS = 8; // 总显示小时数
    private static final int OXYGEN_THRESHOLD = 90; // 低血氧阈值
    private static final int SIGNAL_LINE_WIDTH = 1; // 正常信号线条宽度
    private static final int LOW_OXYGEN_LINE_WIDTH = 2; // 低血氧信号线条宽度

    // 颜色常量
    private static final Color GRID_COLOR = new Color(230, 230, 230);
    private static final Color SIGNAL_COLOR = Color.BLUE;
    private static final Color LOW_OXYGEN_COLOR = Color.RED;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color EMPTY_STATE_TEXT_COLOR = Color.GRAY;

    // 字体常量
    private static final Font TITLE_FONT = new Font("SimHei", Font.BOLD, 16);
    private static final Font LEGEND_FONT = new Font("SimHei", Font.PLAIN, 12);
    private static final Font EMPTY_STATE_FONT = new Font("SimHei", Font.PLAIN, 16);

    // 时间刻度常量
    private static final int MINUTE_INTERVAL_COARSE = 15; // 中等缩放时间隔
    private static final int MINUTE_INTERVAL_FINE = 5; // 高缩放时间隔

    public void setData(List<DataPoint> data) {
        this.data = data;
        this.zoomFactor = 1.0;
        this.offsetX = 0;
        repaint();
    }

    public SleepPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // 注册鼠标滚轮事件用于缩放
        addMouseWheelListener(e -> {
            double oldZoom = zoomFactor;
            if (e.getWheelRotation() < 0) {
                zoomFactor *= 1.1;
            } else {
                zoomFactor /= 1.1;
            }
            // 限制缩放范围
            zoomFactor = Math.max(1, Math.min(zoomFactor, data != null ? data.size() : 100));

            // 保持鼠标位置不变的缩放逻辑
            double mouseX = e.getX();
            offsetX = (int) (mouseX - (mouseX - offsetX) * (zoomFactor / oldZoom));
            repaint();
        });

        // 注册鼠标拖动事件用于平移
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    offsetX += dx;
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            drawEmptyState(g);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        // 设置抗锯齿和高质量渲染
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();
        int maxEegSignal = data.stream().mapToInt(dp -> dp.eegSignal).max().orElse(1);
        int dataSize = data.size();

        // 限制平移范围
        offsetX = Math.min(0, Math.max(offsetX, width - (int)(width * zoomFactor)));

        // 绘制背景和内容
        drawGrid(g2d, width, height);
        drawEegSignal(g2d, width, height, maxEegSignal, dataSize);
        drawTimeScale(g2d, width, height);
        drawTitleAndLegend(g2d, width);
    }

    /**
     * 绘制空状态提示
     */
    private void drawEmptyState(Graphics g) {
        g.setColor(EMPTY_STATE_TEXT_COLOR);
        g.setFont(EMPTY_STATE_FONT);
        String message = "未加载数据，请打开文件或生成测试数据";
        int x = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }

    /**
     * 绘制网格背景
     */
    private void drawGrid(Graphics2D g2d, int width, int height) {
        g2d.setColor(GRID_COLOR);

        // 绘制水平网格线
        for (int i = 1; i < GRID_HORIZONTAL_DIVISIONS; i++) {
            int y = i * height / GRID_HORIZONTAL_DIVISIONS;
            g2d.drawLine(0, y, width, y);
        }

        // 根据缩放级别绘制不同精度的垂直网格线
        if (zoomFactor < MINUTES_SCALE_THRESHOLD) {
            drawHourGrid(g2d, width, height);
        } else if (zoomFactor < FINE_MINUTES_SCALE_THRESHOLD) {
            drawMinuteGrid(g2d, width, height);
        } else {
            drawFineMinuteGrid(g2d, width, height);
        }
    }

    /**
     * 绘制小时网格线
     */
    private void drawHourGrid(Graphics2D g2d, int width, int height) {
        int pixelsPerHour = (int)(width * zoomFactor / TOTAL_DISPLAY_HOURS);

        int startHour = Math.max(0, (-offsetX) / pixelsPerHour);
        int endHour = Math.min(TOTAL_DISPLAY_HOURS, startHour + width / pixelsPerHour + 2);

        for (int hour = startHour; hour <= endHour; hour++) {
            int x = (int)(hour * pixelsPerHour + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, 0, x, height);
            }
        }
    }

    /**
     * 绘制15分钟间隔网格线
     */
    private void drawMinuteGrid(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_INTERVAL_COARSE * MINUTE_INTERVAL_COARSE);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 10);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_INTERVAL_COARSE) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, 0, x, height);
            }
        }
    }

    /**
     * 绘制精细分钟网格线（5分钟间隔）
     */
    private void drawFineMinuteGrid(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_INTERVAL_FINE * MINUTE_INTERVAL_FINE);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 10);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_INTERVAL_FINE) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, 0, x, height);
            }
        }
    }

    /**
     * 绘制脑电信号曲线
     */
    private void drawEegSignal(Graphics2D g2d, int width, int height, int maxEegSignal, int dataSize) {
        Stroke normalStroke = new BasicStroke(SIGNAL_LINE_WIDTH);
        Stroke lowOxygenStroke = new BasicStroke(LOW_OXYGEN_LINE_WIDTH);

        for (int i = 1; i < dataSize; i++) {
            if (i >= data.size()) break;

            int x1 = (int)((i - 1) * width / (double)dataSize * zoomFactor + offsetX);
            int y1 = height - (data.get(i - 1).eegSignal * height / maxEegSignal);
            int x2 = (int)(i * width / (double)dataSize * zoomFactor + offsetX);
            int y2 = height - (data.get(i).eegSignal * height / maxEegSignal);

            if ((x1 >= 0 || x2 >= 0) && (x1 <= width || x2 <= width)) {
                boolean isLowOxygen = data.get(i-1).oxygenSaturation < OXYGEN_THRESHOLD || data.get(i).oxygenSaturation < OXYGEN_THRESHOLD;

                g2d.setColor(isLowOxygen ? LOW_OXYGEN_COLOR : SIGNAL_COLOR);
                g2d.setStroke(isLowOxygen ? lowOxygenStroke : normalStroke);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

    /**
     * 绘制时间刻度
     */
    private void drawTimeScale(Graphics2D g2d, int width, int height) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(LEGEND_FONT);

        if (zoomFactor < MINUTES_SCALE_THRESHOLD) {
            drawHourScale(g2d, width, height);
        } else if (zoomFactor < FINE_MINUTES_SCALE_THRESHOLD) {
            drawMinuteScale(g2d, width, height);
        } else {
            drawFineMinuteScale(g2d, width, height);
        }
    }

    /**
     * 绘制小时刻度
     */
    private void drawHourScale(Graphics2D g2d, int width, int height) {
        int pixelsPerHour = (int)(width * zoomFactor / TOTAL_DISPLAY_HOURS);

        int startHour = Math.max(0, (-offsetX) / pixelsPerHour);
        int endHour = Math.min(TOTAL_DISPLAY_HOURS, startHour + width / pixelsPerHour + 2);

        for (int hour = startHour; hour <= endHour; hour++) {
            int x = (int)(hour * pixelsPerHour + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, height - 5, x, height);
                g2d.drawString(hour + "h", x - 10, height - 10);
            }
        }
    }

    /**
     * 绘制15分钟间隔刻度
     */
    private void drawMinuteScale(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_INTERVAL_COARSE * MINUTE_INTERVAL_COARSE);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 30);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_INTERVAL_COARSE) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, height - 5, x, height);
                int hour = minute / 60;
                int min = minute % 60;
                g2d.drawString(String.format("%d:%02d", hour, min), x - 15, height - 10);
            }
        }
    }

    /**
     * 绘制精细分钟刻度（5分钟间隔）
     */
    private void drawFineMinuteScale(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_INTERVAL_FINE * MINUTE_INTERVAL_FINE);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 10);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_INTERVAL_FINE) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, height - 5, x, height);
                int hour = minute / 60;
                int min = minute % 60;
                g2d.drawString(String.format("%d:%02d", hour, min), x - 15, height - 10);
            }
        }
    }

    /**
     * 绘制标题和图例
     */
    private void drawTitleAndLegend(Graphics2D g2d, int width) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(TITLE_FONT);
        g2d.drawString("脑电信号 (EEG)", 15, 25);

        // 绘制图例
        g2d.setFont(LEGEND_FONT);

        // 正常血氧图例
        g2d.setColor(SIGNAL_COLOR);
        g2d.drawLine(width - 120, 20, width - 100, 20);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("正常血氧", width - 90, 24);

        // 低血氧图例
        g2d.setColor(LOW_OXYGEN_COLOR);
        g2d.drawLine(width - 120, 40, width - 100, 40);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("低血氧 (<90%)", width - 90, 44);
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public int getOffsetX() {
        return offsetX;
    }
}    