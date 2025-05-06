package sports_tracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleBox;

    public Register() {
        setTitle("Register - Sports Tournament Tracker");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(255, 175, 189); // light pink
                Color color2 = new Color(255, 195, 160); // peach
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setPreferredSize(new Dimension(350, 280));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Create Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(titleLabel);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleBox = new JComboBox<>(new String[]{"user", "admin"});

        formPanel.add(labeledPanel("Username:", usernameField));
        formPanel.add(labeledPanel("Password:", passwordField));
        formPanel.add(labeledPanel("Role:", roleBox));

        JButton registerBtn = createStyledButton("Register");
        registerBtn.addActionListener(e -> registerUser());

        JButton loginBtn = createStyledButton("Already have an account? Login");
        loginBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        formPanel.add(registerBtn);
        formPanel.add(loginBtn);

        background.add(formPanel);
    }

    private JPanel labeledPanel(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 94, 98));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 120, 120));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 94, 98));
            }
        });

        return button;
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Connect db = new Connect();
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = db.c.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            } else {
                String insertQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = db.c.prepareStatement(insertQuery);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, role);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account created! You can now log in.");
                dispose();
                new Login().setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during registration.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}
