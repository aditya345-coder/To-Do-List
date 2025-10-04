package app;

import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToDoListApp extends JFrame {
    private static final String DB_URL = "jdbc:h2:./db/todo";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private JTextField taskInputField;
    private DefaultListModel<Task> listModel;
    private JList<Task> taskJList;

    // Static block to load H2 JDBC driver
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    public ToDoListApp() {
        setTitle("To-Do List Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeDatabase();
        initializeUi();
        refreshTaskList();
    }

    private void initializeUi() {
        taskInputField = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton completeButton = new JButton("Mark as Completed");

        listModel = new DefaultListModel<>();
        taskJList = new JList<>(listModel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(taskInputField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(removeButton);
        bottomPanel.add(completeButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskJList), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::onAddTask);
        removeButton.addActionListener(this::onRemoveTask);
        completeButton.addActionListener(this::onCompleteTask);
    }

    private void onAddTask(ActionEvent e) {
        String text = taskInputField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task description.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        addTaskToDb(text);
        taskInputField.setText("");
        refreshTaskList();
    }

    private void onRemoveTask(ActionEvent e) {
        Task selected = taskJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a task to remove.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        deleteTaskFromDb(selected.getId());
        refreshTaskList();
    }

    private void onCompleteTask(ActionEvent e) {
        Task selected = taskJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        markTaskCompletedInDb(selected.getId());
        refreshTaskList();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "description TEXT NOT NULL, " +
                    "isCompleted INTEGER NOT NULL DEFAULT 0" +
                    ")";
            stmt.execute(createTableSql);
        } catch (SQLException ex) {
            showError("Database initialization failed: " + ex.getMessage());
        }
    }

    private void addTaskToDb(String description) {
        String sql = "INSERT INTO tasks(description, isCompleted) VALUES(?, 0)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            ps.executeUpdate();
        } catch (SQLException ex) {
            showError("Failed to add task: " + ex.getMessage());
        }
    }

    private void deleteTaskFromDb(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            showError("Failed to remove task: " + ex.getMessage());
        }
    }

    private void markTaskCompletedInDb(int id) {
        String sql = "UPDATE tasks SET isCompleted = 1 WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            showError("Failed to mark task as completed: " + ex.getMessage());
        }
    }

    private List<Task> fetchAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, description, isCompleted FROM tasks ORDER BY id DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                boolean isCompleted = rs.getInt("isCompleted") == 1;
                tasks.add(new Task(id, description, isCompleted));
            }
        } catch (SQLException ex) {
            showError("Failed to load tasks: " + ex.getMessage());
        }
        return tasks;
    }

    private void refreshTaskList() {
        listModel.clear();
        for (Task task : fetchAllTasks()) {
            listModel.addElement(task);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().setVisible(true));
    }
}


