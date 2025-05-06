package sports_tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public Login() {
        setTitle("Login - Sports Tournament Tracker");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false);

        GradientPanel backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titleLabel = new JLabel("🏆 Sports Tournament Tracker", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createVerticalStrut(25));
        backgroundPanel.add(createLabeledField("Username", usernameField));
        backgroundPanel.add(Box.createVerticalStrut(15));
        backgroundPanel.add(createLabeledField("Password", passwordField));
        backgroundPanel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        JButton loginBtn = createStyledButton("Login");
        JButton registerBtn = createStyledButton("Register");

        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> {
            dispose();
            new Register().setVisible(true);
        });

        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        backgroundPanel.add(btnPanel);

        setContentPane(backgroundPanel);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setOpaque(false);
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 255, 255));
        button.setForeground(new Color(34, 34, 34));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try {
            Connect db = new Connect();
            PreparedStatement stmt = db.c.prepareStatement(
                "SELECT role FROM users WHERE username = ? AND password = ?"
            );
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                dispose();
                if (role.equalsIgnoreCase("admin")) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new HomePage().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error logging in.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

    // Gradient background panel
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(58, 123, 213),
                0, getHeight(), new Color(58, 213, 151)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
