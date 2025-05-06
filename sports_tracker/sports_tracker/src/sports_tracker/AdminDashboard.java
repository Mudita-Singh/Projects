package sports_tracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    JTable matchTable;
    DefaultTableModel tableModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Sports Tournament Tracker");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(72, 219, 251), getWidth(), getHeight(),
                        new Color(253, 203, 110)); // teal to peach
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("📋 All Matches (Admin View)", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 30, 30));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Sport", "Match", "Time", "Date", "Score"};
        tableModel = new DefaultTableModel(columns, 0);
        matchTable = new JTable(tableModel);
        matchTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        matchTable.setRowHeight(22);
        matchTable.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(matchTable);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        loadMatches();

        JButton addButton = createStyledButton("➕ Add Match");
        JButton editButton = createStyledButton("✏️ Edit Selected Match");
        JButton deleteButton = createStyledButton("🗑️ Delete Selected Match");
        JButton refreshButton = createStyledButton("🔄 Refresh");

        addButton.addActionListener(e -> addMatchDialog());
        editButton.addActionListener(e -> editSelectedMatch());
        deleteButton.addActionListener(e -> deleteSelectedMatch());
        refreshButton.addActionListener(e -> loadMatches());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(72, 149, 239));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadMatches() {
        tableModel.setRowCount(0);
        try {
            Connect db = new Connect();
            String query = "SELECT id, sport_type, match_name, match_time, match_date, score FROM matches";
            ResultSet rs = db.s.executeQuery(query);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("sport_type"),
                    rs.getString("match_name"),
                    rs.getString("match_time"),
                    rs.getString("match_date"),
                    rs.getString("score")
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading matches.");
        }
    }

    private void addMatchDialog() {
        JTextField sportField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField scoreField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Sport:")); panel.add(sportField);
        panel.add(new JLabel("Match Name:")); panel.add(nameField);
        panel.add(new JLabel("Time:")); panel.add(timeField);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Score:")); panel.add(scoreField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Match", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connect db = new Connect();
                String sql = "INSERT INTO matches (sport_type, match_name, match_time, match_date, score) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = db.c.prepareStatement(sql);
                stmt.setString(1, sportField.getText());
                stmt.setString(2, nameField.getText());
                stmt.setString(3, timeField.getText());
                stmt.setString(4, dateField.getText());
                stmt.setString(5, scoreField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Match added successfully!");
                loadMatches();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding match.");
            }
        }
    }

    private void editSelectedMatch() {
        int row = matchTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a match to edit.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String sport = (String) tableModel.getValueAt(row, 1);
        String match = (String) tableModel.getValueAt(row, 2);
        String time = (String) tableModel.getValueAt(row, 3);
        String date = (String) tableModel.getValueAt(row, 4);
        String score = (String) tableModel.getValueAt(row, 5);

        JTextField sportField = new JTextField(sport);
        JTextField nameField = new JTextField(match);
        JTextField timeField = new JTextField(time);
        JTextField dateField = new JTextField(date);
        JTextField scoreField = new JTextField(score);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Sport:")); panel.add(sportField);
        panel.add(new JLabel("Match Name:")); panel.add(nameField);
        panel.add(new JLabel("Time:")); panel.add(timeField);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Score:")); panel.add(scoreField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Match", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connect db = new Connect();
                String sql = "UPDATE matches SET sport_type=?, match_name=?, match_time=?, match_date=?, score=? WHERE id=?";
                PreparedStatement stmt = db.c.prepareStatement(sql);
                stmt.setString(1, sportField.getText());
                stmt.setString(2, nameField.getText());
                stmt.setString(3, timeField.getText());
                stmt.setString(4, dateField.getText());
                stmt.setString(5, scoreField.getText());
                stmt.setInt(6, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Match updated successfully!");
                loadMatches();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating match.");
            }
        }
    }

    private void deleteSelectedMatch() {
        int row = matchTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a match to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this match?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connect db = new Connect();
                String sql = "DELETE FROM matches WHERE id=?";
                PreparedStatement stmt = db.c.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Match deleted.");
                loadMatches();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting match.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
