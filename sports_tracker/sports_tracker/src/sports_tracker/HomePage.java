package sports_tracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HomePage extends JFrame {

    JTabbedPane tabbedPane;

    public HomePage() {
        setTitle("🏆 Sports Tracker");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(135, 206, 250), getWidth(), getHeight(),
                        new Color(255, 182, 193)); // sky blue to light pink
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setOpaque(false);

        addSportTab("Cricket");
        addSportTab("Football");
        addSportTab("Basketball");
        addSportTab("Badminton");
        addSportTab("Tennis");

        backgroundPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private void addSportTab(String sport) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel("Live " + sport + " Matches", JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(new Color(30, 30, 30));
        panel.add(label, BorderLayout.NORTH);

        JTable table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(22);
        updateTableData(table, sport);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        JTextField searchField = new JTextField(15);
        JButton findButton = new JButton("🔍 Find Event");
        styleButton(findButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(findButton);

        findButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                updateTableDataWithSearch(table, sport, keyword);
            } else {
                updateTableData(table, sport);
            }
        });

        panel.add(searchPanel, BorderLayout.SOUTH);
        tabbedPane.addTab(sport, panel);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // steel blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
    }

    private void updateTableData(JTable table, String sportType) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Match", "Time", "Date", "Score"}, 0);

        try {
            Connect db = new Connect();
            PreparedStatement stmt = db.c.prepareStatement(
                "SELECT match_name, match_time, match_date, score FROM matches WHERE sport_type = ?"
            );
            stmt.setString(1, sportType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("match_name"),
                    rs.getString("match_time"),
                    rs.getString("match_date"),
                    rs.getString("score")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setModel(model);
    }

    private void updateTableDataWithSearch(JTable table, String sportType, String keyword) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Match", "Time", "Date", "Score"}, 0);

        try {
            Connect db = new Connect();
            PreparedStatement stmt = db.c.prepareStatement(
                "SELECT match_name, match_time, match_date, score FROM matches WHERE sport_type = ? AND match_name LIKE ?"
            );
            stmt.setString(1, sportType);
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("match_name"),
                    rs.getString("match_time"),
                    rs.getString("match_date"),
                    rs.getString("score")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }
}
