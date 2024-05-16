package maHoa;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class scurity extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private Connection conn;

    public scurity() {
        super("Login System");

        // Initialize components
        JLabel titleLabel = new JLabel("Login System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Set layout
        setLayout(new GridLayout(4, 1));
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel1.add(titleLabel);
        panel2.add(new JLabel("Username:"));
        panel2.add(usernameField);
        panel3.add(new JLabel("Password:"));
        panel3.add(passwordField);
        panel4.add(loginButton);
        panel4.add(registerButton);
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        // Add action listeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Database connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/[dbo].[Table_1]", "sa", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(scurity::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            login();
        } else if (e.getActionCommand().equals("Register")) {
            register();
        }
    }

    public void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String hashedPassword = hashPassword(password);

        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO scurity (username, password) VALUES (?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại!");
        }
    }

    public void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String hashedPassword = hashPassword(password);

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM scurity WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + username);
            } else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thất bại! Tên người dùng hoặc mật khẩu không đúng.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đăng nhập thất bại!");
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}