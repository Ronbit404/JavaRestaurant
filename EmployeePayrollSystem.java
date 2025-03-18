package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeePayrollSystem extends JFrame {
    
    private JTextField txtName;
    private JTextField txtHourlyRate;
    private JTextField txtHoursWorked;
    private JRadioButton rbtnFullTime;
    private JRadioButton rbtnPartTime;
    private JCheckBox chkTax;
    private JCheckBox chkInsurance;
    private JButton btnCalculate;
    
    public EmployeePayrollSystem() {
        super("Employee Payroll System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        
        // Use a GridBagLayout for a neat, flexible layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ========== ROW 0: Employee Name ==========
        JLabel lblName = new JLabel("Employee Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblName, gbc);
        
        txtName = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(txtName, gbc);
        
        // ========== ROW 1: Hourly Rate ==========
        JLabel lblHourlyRate = new JLabel("Hourly Rate:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblHourlyRate, gbc);
        
        txtHourlyRate = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(txtHourlyRate, gbc);
        
        // ========== ROW 2: Hours Worked ==========
        JLabel lblHoursWorked = new JLabel("Hours Worked:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblHoursWorked, gbc);
        
        txtHoursWorked = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtHoursWorked, gbc);
        
        // ========== ROW 3: Employment Type (Radio Buttons) ==========
        JLabel lblEmploymentType = new JLabel("Employment Type:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblEmploymentType, gbc);
        
        JPanel pnlRadioButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rbtnFullTime = new JRadioButton("Full-Time");
        rbtnPartTime = new JRadioButton("Part-Time");
        
        // Group them so only one can be selected at a time
        ButtonGroup group = new ButtonGroup();
        group.add(rbtnFullTime);
        group.add(rbtnPartTime);
        
        // Set a default selection
        rbtnFullTime.setSelected(true);
        
        pnlRadioButtons.add(rbtnFullTime);
        pnlRadioButtons.add(rbtnPartTime);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(pnlRadioButtons, gbc);
        
        // ========== ROW 4: Deductions (CheckBoxes) ==========
        JLabel lblDeductions = new JLabel("Deductions:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblDeductions, gbc);
        
        JPanel pnlCheckBoxes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkTax = new JCheckBox("Tax");
        chkInsurance = new JCheckBox("Insurance");
        pnlCheckBoxes.add(chkTax);
        pnlCheckBoxes.add(chkInsurance);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(pnlCheckBoxes, gbc);
        
        // ========== ROW 5: Calculate Button ==========
        btnCalculate = new JButton("Calculate & Generate Payslip");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(btnCalculate, gbc);
        
        // Add ActionListener for the Calculate button
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePayslip();
            }
        });
    }
    
    private void calculatePayslip() {
        // Retrieve input values
        String name = txtName.getText().trim();
        String hourlyRateStr = txtHourlyRate.getText().trim();
        String hoursWorkedStr = txtHoursWorked.getText().trim();
        
        // Basic validation
        if (name.isEmpty() || hourlyRateStr.isEmpty() || hoursWorkedStr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Please fill in all fields.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        double hourlyRate, hoursWorked;
        try {
            hourlyRate = Double.parseDouble(hourlyRateStr);
            hoursWorked = Double.parseDouble(hoursWorkedStr);
            
            if (hourlyRate < 0 || hoursWorked < 0) {
                throw new NumberFormatException("Negative values are not allowed.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, 
                    "Please enter valid numeric values for rate and hours.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Determine employment type
        String employmentType = rbtnFullTime.isSelected() ? "Full-Time" : "Part-Time";
        
        // Calculate gross salary
        double grossSalary = hourlyRate * hoursWorked;
        
        // Apply deductions if selected
        double netSalary = grossSalary;
        
        if (chkTax.isSelected()) {
            // Example: 10% tax
            netSalary -= (netSalary * 0.10);
        }
        
        if (chkInsurance.isSelected()) {
            // Example: 5% insurance
            netSalary -= (netSalary * 0.05);
        }
        
        // Build the payslip message
        StringBuilder payslip = new StringBuilder();
        payslip.append("Employee Name: ").append(name).append("\n");
        payslip.append("Employment Type: ").append(employmentType).append("\n");
        payslip.append("Hourly Rate: $").append(String.format("%.2f", hourlyRate)).append("\n");
        payslip.append("Hours Worked: ").append(String.format("%.2f", hoursWorked)).append("\n");
        payslip.append("Gross Salary: $").append(String.format("%.2f", grossSalary)).append("\n");
        payslip.append("Deductions:\n");
        payslip.append("  - Tax: ").append(chkTax.isSelected() ? "Yes" : "No").append("\n");
        payslip.append("  - Insurance: ").append(chkInsurance.isSelected() ? "Yes" : "No").append("\n");
        payslip.append("Net Salary: $").append(String.format("%.2f", netSalary)).append("\n");
        
        // Display payslip
        JOptionPane.showMessageDialog(
                this, 
                payslip.toString(), 
                "Payslip", 
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeePayrollSystem frame = new EmployeePayrollSystem();
            frame.setVisible(true);
        });
    }
}
