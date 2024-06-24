import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginSignUpApp {
    private static final String USER_CSV = "user.csv";
    private static final String EMPLOYEE_DATA_CSV = "EmployeeData.csv";

    private JFrame mainFrame;
    private JPanel loginPanel;
    private JPanel signUpPanel;
    private JPanel welcomePanel;
    private JPanel employeeDataPanel;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JTextField signUpUsernameField;
    private JPasswordField signUpPasswordField;

    private DataTableModel tableModel;
    private int nextEmployeeNumber = 1;  // Counter for generating employee numbers

    public LoginSignUpApp() {
        mainFrame = new JFrame("Login and Sign-up Application");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        showLoginFrame();
    }

    private void showLoginFrame() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome! Please Log In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        loginUsernameField = new JTextField(15);
        loginPanel.add(loginUsernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        loginPasswordField = new JPasswordField(15);
        loginPanel.add(loginPasswordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(loginButton, gbc);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        gbc.gridx = 1;
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(40, 167, 69));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(signUpButton, gbc);
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSignUpPage();
            }
        });

        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainFrame.setContentPane(loginPanel);
        mainFrame.setVisible(true);
    }

    private void login() {
        String username = loginUsernameField.getText();
        char[] password = loginPasswordField.getPassword();

        if (authenticateUser(username, new String(password))) {
            showWelcomePage();
        } else {
            showMessage("Invalid username or password.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openSignUpPage() {
        signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        signUpPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        signUpPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        signUpUsernameField = new JTextField(15);
        signUpPanel.add(signUpUsernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        signUpPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        signUpPasswordField = new JPasswordField(15);
        signUpPanel.add(signUpPasswordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(40, 167, 69));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(signUpButton, gbc);
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        gbc.gridx = 1;
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(220, 53, 69));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(backButton, gbc);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backToLoginPage();
            }
        });

        signUpPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainFrame.setContentPane(signUpPanel);
        mainFrame.setVisible(true);
    }

    private void signUp() {
        String username = signUpUsernameField.getText();
        char[] password = signUpPasswordField.getPassword();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_CSV, true))) {
            bw.write(username + "," + new String(password));
            bw.newLine();
            showMessage("Sign-up successful! You can now log in.");
            backToLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Sign-up failed. Please try again.");
        }
    }

    private void backToLoginPage() {
        mainFrame.setContentPane(loginPanel);
        mainFrame.setVisible(true);
    }

    private void showWelcomePage() {
        welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(70, 130, 180));

        JLabel welcomeLabel = new JLabel("Welcome to the Employee Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(70, 130, 180));

        JButton viewEmployeeDataButton = new JButton("View Employee Data");
        viewEmployeeDataButton.setBackground(new Color(0, 123, 255));
        viewEmployeeDataButton.setForeground(Color.WHITE);
        viewEmployeeDataButton.setFont(new Font("Arial", Font.BOLD, 12));
        buttonPanel.add(viewEmployeeDataButton);
        viewEmployeeDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEmployeeData();
            }
        });

        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.setContentPane(welcomePanel);
        mainFrame.setVisible(true);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void showEmployeeData() {
        JFrame employeeDataFrame = new JFrame("Employee Data");
        employeeDataFrame.setSize(800, 600);
        employeeDataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        employeeDataFrame.setLocationRelativeTo(null);

        employeeDataPanel = new JPanel();
        employeeDataPanel.setLayout(new BorderLayout());

        tableModel = new DataTableModel();
        JTable employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        employeeDataPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton updateEmployeeButton = new JButton("Update Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        employeeDataPanel.add(buttonPanel, BorderLayout.SOUTH);

        addEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        updateEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    updateEmployee(selectedRow);
                } else {
                    showMessage("Please select an employee to update.");
                }
            }
        });

        deleteEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    deleteEmployee(selectedRow);
                } else {
                    showMessage("Please select an employee to delete.");
                }
            }
        });

        employeeDataFrame.add(employeeDataPanel);
        employeeDataFrame.setVisible(true);
    }

    private void addEmployee() {
                JPanel inputPanel = new JPanel(new GridLayout(20, 2));
        JTextField[] fields = new JTextField[19];
        String[] labels = {"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS", "Philhealth",
                "TIN", "Pag-IBIG", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"};

        fields[0] = new JTextField(String.valueOf(nextEmployeeNumber));
        fields[0].setEditable(false);  // Make the employee number field non-editable

        for (int i = 1; i < labels.length; i++) {
            inputPanel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField();
            inputPanel.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = fields[2].getText().trim(); // Get and trim first name
            if (!isValidFirstName(firstName)) {
                showMessage("Invalid first name. Please enter only letters and spaces.");
                return; // Exit method if first name is invalid
            }

            String[] employeeData = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                employeeData[i] = fields[i].getText();
            }
            tableModel.addEmployeeData(employeeData);
            saveEmployeeData();

            // Increment the employee number counter for the next employee
            nextEmployeeNumber++;
        }
    }


    private void updateEmployee(int rowIndex) {
        String[] currentData = tableModel.getEmployeeData(rowIndex);

        JPanel inputPanel = new JPanel(new GridLayout(20, 2));
        JTextField[] fields = new JTextField[19];
        String[] labels = {"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS", "Philhealth",
                "TIN", "Pag-IBIG", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"};

        fields[0] = new JTextField(currentData[0]); // Employee number remains the same for update
        fields[0].setEditable(false);  // Make the employee number field non-editable

        for (int i = 1; i < labels.length; i++) {
            inputPanel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField(currentData[i]);
            inputPanel.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String firstName = fields[2].getText().trim(); // Get and trim first name
            if (!isValidFirstName(firstName)) {
                showMessage("Invalid first name. Please enter only letters and spaces.");
                return; // Exit method if first name is invalid
            }

            String[] updatedData = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                updatedData[i] = fields[i].getText();
            }
            tableModel.updateEmployeeData(rowIndex, updatedData);
            saveEmployeeData();
        }
    }

    private void deleteEmployee(int rowIndex) {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            tableModel.removeEmployeeData(rowIndex);
            saveEmployeeData();
        }
    }

    private void saveEmployeeData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DATA_CSV))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String[] data = tableModel.getEmployeeData(i);
                bw.write(String.join(",", data));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginSignUpApp();
    }

    // Custom table model for managing employee data
    class DataTableModel extends AbstractTableModel {
        private List<String[]> employeeData;
        private String[] columnNames = {"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS", "Philhealth",
                "TIN", "Pag-IBIG", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"};

        public DataTableModel() {
            employeeData = new ArrayList<>();
            loadEmployeeData();
        }

        private void loadEmployeeData() {
            try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_CSV))) {
                String line;
                boolean headerSkipped = false;
                while ((line = br.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue; // Skip the header line
                    }
                    employeeData.add(line.split(","));
                    // Update the next employee number based on existing data
                    int employeeNumber = Integer.parseInt(employeeData.get(employeeData.size() - 1)[0]);
                    if (employeeNumber >= nextEmployeeNumber) {
                        nextEmployeeNumber = employeeNumber + 1;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        public void addEmployeeData(String[] data) {
            employeeData.add(data);
            fireTableRowsInserted(employeeData.size() - 1, employeeData.size() - 1);
        }

        public void updateEmployeeData(int rowIndex, String[] data) {
            employeeData.set(rowIndex, data);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }

        public void removeEmployeeData(int rowIndex) {
            employeeData.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }

        public String[] getEmployeeData(int rowIndex) {
            return employeeData.get(rowIndex);
        }

        @Override
        public int getRowCount() {
            return employeeData.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return employeeData.get(rowIndex)[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    private boolean isValidFirstName(String firstName) {
        return firstName.matches("[a-zA-Z ]+");
    }
}


