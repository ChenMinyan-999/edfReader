import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class OxygenPanel extends JPanel {
    // 颜色配置
    private static final Color BACKGROUND_COLOR = Color.WHITE;                  // 背景颜色
    private static final Color BORDER_COLOR = Color.LIGHT_GRAY;                 // 边框颜色
    private static final Color GRID_COLOR = new Color(230, 230, 230);           // 网格线颜色
    private static final Color TEXT_COLOR = Color.BLACK;                        // 文本颜色
    private static final Color OXYGEN_LINE_COLOR = Color.GREEN;                 // 血氧曲线颜色
    private static final Color THRESHOLD_LINE_COLOR = Color.RED;                // 阈值线颜色
    private static final Color EMPTY_STATE_TEXT_COLOR = Color.GRAY;             // 空状态文本颜色

    // 线条样式
    private static final float OXYGEN_LINE_THICKNESS = 1.5f;                    // 血氧曲线粗细
    private static final float THRESHOLD_LINE_THICKNESS = 1.0f;                 // 阈值线粗细

    // 字体大小
    private static final int CHART_TITLE_FONT_SIZE = 16;                        // 图表标题字体大小
    private static final int LEGEND_FONT_SIZE = 12;                              // 图例字体大小
    private static final int GRID_LABEL_FONT_SIZE = 12;                          // 网格标签字体大小
    private static final int TIME_LABEL_FONT_SIZE = 12;                          // 时间标签字体大小

    // 时间刻度间隔
    private static final int MINUTE_GRID_INTERVAL = 5;                           // 分钟网格间隔(分钟)
    private static final int MINUTE_LABEL_INTERVAL = 15;                         // 分钟标签间隔(分钟)
    private static final int FINE_MINUTE_LABEL_INTERVAL = 5;                     // 精细分钟标签间隔(分钟)

    // 缩放参数
    private static final double ZOOM_INCREMENT = 1.1;                             // 缩放增量
    private static final double MIN_ZOOM = 1.0;                                   // 最小缩放比例
    private static final double MAX_ZOOM = 100.0;                                 // 最大缩放比例

    // 图表数据范围
    private static final int TOTAL_DISPLAY_HOURS = 8;                             // 总显示小时数
    private static final int THRESHOLD_SATURATION = 90;                           // 饱和度阈值(%)

    // 布局边距
    private static final int X_AXIS_MARGIN = 5;                                   // X轴边距
    private static final int Y_AXIS_MARGIN = 5;                                   // Y轴边距
    private static final int TITLE_MARGIN = 25;                                   // 标题边距
    private static final int LEGEND_WIDTH = 100;                                  // 图例宽度
    private static final int LEGEND_HEIGHT = 20;                                  // 图例高度

    // 缩放阈值
    private static final double MINUTES_SCALE_THRESHOLD = 4.0;                   // 分钟刻度显示阈值
    private static final double FINE_MINUTES_SCALE_THRESHOLD = 8.0;              // 精细分钟刻度显示阈值

    // 成员变量
    private List<DataPoint> data;
    private double zoomFactor = 1.0;
    private int offsetX = 0;
    private Point dragStart;

    public OxygenPanel() {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        // 添加鼠标滚轮缩放功能
        addMouseWheelListener(e -> {
            double oldZoom = zoomFactor;
            if (e.getWheelRotation() < 0) {
                zoomFactor *= ZOOM_INCREMENT;
            } else {
                zoomFactor /= ZOOM_INCREMENT;
            }
            zoomFactor = Math.max(MIN_ZOOM, Math.min(zoomFactor, data != null ? data.size() : MAX_ZOOM));

            double mouseX = e.getX();
            offsetX = (int) (mouseX - (mouseX - offsetX) * (zoomFactor / oldZoom));
            repaint();
        });

        // 添加鼠标拖动功能
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

    public void setData(List<DataPoint> data) {
        this.data = data;
        this.zoomFactor = MIN_ZOOM;
        this.offsetX = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) {
            drawEmptyState(g);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();
        int dataSize = data.size();

        offsetX = Math.min(0, Math.max(offsetX, width - (int)(width * zoomFactor)));

        // 绘制网格背景
        drawGrid(g2d, width, height);

        // 绘制血氧饱和度曲线
        g2d.setColor(OXYGEN_LINE_COLOR);
        g2d.setStroke(new BasicStroke(OXYGEN_LINE_THICKNESS));

        for (int i = 1; i < dataSize; i++) {
            if (i >= data.size()) break;

            int x1 = (int)((i - 1) * width / (double)dataSize * zoomFactor + offsetX);
            int y1 = height - (data.get(i - 1).oxygenSaturation * height / 100);
            int x2 = (int)(i * width / (double)dataSize * zoomFactor + offsetX);
            int y2 = height - (data.get(i).oxygenSaturation * height / 100);

            if ((x1 >= 0 || x2 >= 0) && (x1 <= width || x2 <= width)) {
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        // 绘制90%饱和度参考线
        g2d.setColor(THRESHOLD_LINE_COLOR);
        g2d.setStroke(new BasicStroke(THRESHOLD_LINE_THICKNESS, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                new float[]{5.0f, 5.0f}, 0.0f));
        int yThreshold = height - (THRESHOLD_SATURATION * height / 100);
        g2d.drawLine(0, yThreshold, width, yThreshold);

        // 添加参考线标签
        g2d.setColor(THRESHOLD_LINE_COLOR);
        g2d.setFont(new Font("SimHei", Font.PLAIN, GRID_LABEL_FONT_SIZE));
        g2d.drawString(THRESHOLD_SATURATION + "% 阈值", width - LEGEND_WIDTH, yThreshold - Y_AXIS_MARGIN);

        // 绘制时间刻度
        drawTimeScale(g2d, width, height);

        // 绘制标题和图例
        drawTitleAndLegend(g2d, width);
    }

    private void drawEmptyState(Graphics g) {
        g.setColor(EMPTY_STATE_TEXT_COLOR);
        g.setFont(new Font("SimHei", Font.PLAIN, CHART_TITLE_FONT_SIZE));
        String message = "未加载数据";
        int x = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }

    private void drawGrid(Graphics2D g2d, int width, int height) {
        g2d.setColor(GRID_COLOR);

        // 水平网格线（对应血氧饱和度）
        for (int i = 1; i < 5; i++) {
            int saturation = i * 20;
            int y = height - (saturation * height / 100);
            g2d.drawLine(0, y, width, y);
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(saturation + "%", Y_AXIS_MARGIN, y - Y_AXIS_MARGIN);
            g2d.setColor(GRID_COLOR);
        }

        // 垂直网格线（基于时间刻度）
        if (zoomFactor < MINUTES_SCALE_THRESHOLD) {
            // 低缩放时显示小时刻度
            drawHourGrid(g2d, width, height);
        } else if (zoomFactor < FINE_MINUTES_SCALE_THRESHOLD) {
            // 中等缩放时显示15分钟刻度
            drawMinuteGrid(g2d, width, height);
        } else {
            // 高缩放时显示1分钟刻度
            drawFineMinuteGrid(g2d, width, height);
        }
    }

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

    private void drawMinuteGrid(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        // 计算起始和结束分钟，每5分钟绘制一个网格线
        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_GRID_INTERVAL * MINUTE_GRID_INTERVAL);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 10);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_GRID_INTERVAL) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, 0, x, height);
            }
        }
    }

    private void drawFineMinuteGrid(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        // 计算起始和结束分钟，每分钟绘制一个网格线
        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 2);

        for (int minute = startMinute; minute <= endMinute; minute++) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, 0, x, height);
            }
        }
    }

    private void drawTimeScale(Graphics2D g2d, int width, int height) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("SimHei", Font.PLAIN, TIME_LABEL_FONT_SIZE));

        if (zoomFactor < MINUTES_SCALE_THRESHOLD) {
            // 低缩放时显示小时刻度
            drawHourScale(g2d, width, height);
        } else if (zoomFactor < FINE_MINUTES_SCALE_THRESHOLD) {
            // 中等缩放时显示15分钟刻度
            drawMinuteScale(g2d, width, height);
        } else {
            // 高缩放时显示1分钟刻度
            drawFineMinuteScale(g2d, width, height);
        }
    }

    private void drawHourScale(Graphics2D g2d, int width, int height) {
        int pixelsPerHour = (int)(width * zoomFactor / TOTAL_DISPLAY_HOURS);

        int startHour = Math.max(0, (-offsetX) / pixelsPerHour);
        int endHour = Math.min(TOTAL_DISPLAY_HOURS, startHour + width / pixelsPerHour + 2);

        for (int hour = startHour; hour <= endHour; hour++) {
            int x = (int)(hour * pixelsPerHour + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, height - X_AXIS_MARGIN, x, height);
                g2d.drawString(hour + "h", x - 10, height - X_AXIS_MARGIN - 5);
            }
        }
    }

    private void drawMinuteScale(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        // 计算起始和结束分钟，每15分钟绘制一个刻度
        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / MINUTE_LABEL_INTERVAL * MINUTE_LABEL_INTERVAL);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 30);

        for (int minute = startMinute; minute <= endMinute; minute += MINUTE_LABEL_INTERVAL) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                g2d.drawLine(x, height - X_AXIS_MARGIN, x, height);
                int hour = minute / 60;
                int min = minute % 60;
                g2d.drawString(String.format("%d:%02d", hour, min), x - 15, height - X_AXIS_MARGIN - 5);
            }
        }
    }

    private void drawFineMinuteScale(Graphics2D g2d, int width, int height) {
        int totalMinutes = TOTAL_DISPLAY_HOURS * 60;
        int pixelsPerMinute = (int)(width * zoomFactor / totalMinutes);

        // 计算起始和结束分钟，每5分钟绘制一个刻度，每分钟绘制一个小刻度
        int startMinute = Math.max(0, (-offsetX) / pixelsPerMinute / FINE_MINUTE_LABEL_INTERVAL * FINE_MINUTE_LABEL_INTERVAL);
        int endMinute = Math.min(totalMinutes, startMinute + width / pixelsPerMinute + 10);

        for (int minute = startMinute; minute <= endMinute; minute++) {
            int x = (int)(minute * pixelsPerMinute + offsetX);
            if (x >= 0 && x <= width) {
                // 每分钟绘制一个小刻度
                g2d.drawLine(x, height - 3, x, height);

                // 每5分钟绘制一个大刻度和时间标签
                if (minute % FINE_MINUTE_LABEL_INTERVAL == 0) {
                    g2d.drawLine(x, height - X_AXIS_MARGIN, x, height);
                    int hour = minute / 60;
                    int min = minute % 60;
                    g2d.drawString(String.format("%d:%02d", hour, min), x - 15, height - X_AXIS_MARGIN - 5);
                }
            }
        }
    }

    private void drawTitleAndLegend(Graphics2D g2d, int width) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("SimHei", Font.BOLD, CHART_TITLE_FONT_SIZE));
        g2d.drawString("血氧饱和度 (SpO2)", Y_AXIS_MARGIN, TITLE_MARGIN);

        // 绘制图例
        g2d.setFont(new Font("SimHei", Font.PLAIN, LEGEND_FONT_SIZE));

        // 血氧饱和度曲线图例
        g2d.setColor(OXYGEN_LINE_COLOR);
        g2d.drawLine(width - LEGEND_WIDTH, TITLE_MARGIN - LEGEND_HEIGHT, width - LEGEND_WIDTH + LEGEND_WIDTH/2, TITLE_MARGIN - LEGEND_HEIGHT);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("血氧饱和度", width - LEGEND_WIDTH/2, TITLE_MARGIN - LEGEND_HEIGHT + 4);

        // 阈值线图例
        g2d.setColor(THRESHOLD_LINE_COLOR);
        g2d.setStroke(new BasicStroke(THRESHOLD_LINE_THICKNESS, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f,
                new float[]{5.0f, 5.0f}, 0.0f));
        g2d.drawLine(width - LEGEND_WIDTH, TITLE_MARGIN, width - LEGEND_WIDTH + LEGEND_WIDTH/2, TITLE_MARGIN);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString(THRESHOLD_SATURATION + "% 阈值", width - LEGEND_WIDTH/2, TITLE_MARGIN + 4);
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public int getOffsetX() {
        return offsetX;
    }
}