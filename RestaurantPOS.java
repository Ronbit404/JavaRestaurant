package midtermT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RestaurantPOS extends JFrame {
    private static final Color YELLOW_COLOR = new Color(255, 204, 0);
    private static final Color CONTROL_PANEL_COLOR = new Color(220, 220, 220);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font CONTROL_BUTTON_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Dimension BUTTON_SIZE = new Dimension(150, 100);
    private static final Dimension CONTROL_BUTTON_SIZE = new Dimension(200, 60);
    
    private JPanel cartPanel, controlPanel;
    private JTextArea cartArea;
    private JTabbedPane menuTabbedPane;
    private JRadioButton dineInRadio, takeOutRadio;
    private ButtonGroup orderTypeGroup;
    private double total = 0.0;
    private HashMap<String, Double> menuPrices = new HashMap<>();
    private HashMap<String, Integer> orderQuantities = new HashMap<>();

    public RestaurantPOS() {
        setTitle("Restaurant POS System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeMenu();
        createUIComponents();
        addEscapeKeyListener();
    }

    private void initializeMenu() {
        menuPrices.put("Cheeseburger", 99.00);
        menuPrices.put("Chicken Sandwich", 129.00);
        menuPrices.put("Veggie Burger", 89.00);
        menuPrices.put("French Fries", 49.00);
        menuPrices.put("Onion Rings", 59.00);
        menuPrices.put("Mozzarella Sticks", 69.00);
        menuPrices.put("Soft Drink", 39.00);
        menuPrices.put("Milkshake", 79.00);
        menuPrices.put("Iced Tea", 49.00);
        menuPrices.put("Ice Cream", 59.00);
        menuPrices.put("Chocolate Cake", 89.00);
    }

    private void createUIComponents() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout(10, 10));
        container.setBackground(new Color(240, 240, 240));

        container.add(createOrderTypePanel(), BorderLayout.NORTH);
        container.add(createMenuPanel(), BorderLayout.CENTER);
        container.add(createCartPanel(), BorderLayout.EAST);
        container.add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createOrderTypePanel() {
        JPanel orderTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        orderTypePanel.setBorder(BorderFactory.createTitledBorder("Order Type"));
        orderTypePanel.setBackground(Color.WHITE);

        dineInRadio = new JRadioButton("Dine-in");
        takeOutRadio = new JRadioButton("Take-out");
        orderTypeGroup = new ButtonGroup();
        orderTypeGroup.add(dineInRadio);
        orderTypeGroup.add(takeOutRadio);

        dineInRadio.setFont(CONTROL_BUTTON_FONT);
        takeOutRadio.setFont(CONTROL_BUTTON_FONT);
        dineInRadio.setSelected(true);

        orderTypePanel.add(dineInRadio);
        orderTypePanel.add(takeOutRadio);
        return orderTypePanel;
    }

    private JTabbedPane createMenuPanel() {
        menuTabbedPane = new JTabbedPane();
        menuTabbedPane.addTab("Burgers", createCategoryPanel("Burgers", new String[]{"Cheeseburger", "Chicken Sandwich", "Veggie Burger"}));
        menuTabbedPane.addTab("Sides", createCategoryPanel("Sides", new String[]{"French Fries", "Onion Rings", "Mozzarella Sticks"}));
        menuTabbedPane.addTab("Drinks", createCategoryPanel("Drinks", new String[]{"Soft Drink", "Milkshake", "Iced Tea"}));
        menuTabbedPane.addTab("Desserts", createCategoryPanel("Desserts", new String[]{"Ice Cream", "Chocolate Cake"}));
        return menuTabbedPane;
    }

    private JPanel createCategoryPanel(String categoryName, String[] items) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(categoryName));
        panel.setBackground(Color.WHITE);

        for (String item : items) {
            panel.add(createMenuButton(item));
        }
        return panel;
    }

    private JPanel createMenuButton(String itemName) {
        ImageIcon icon = loadImage(itemName);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(icon);

        JButton btn = new JButton("<html><center>" + itemName + "<br>₱" + menuPrices.get(itemName) + "</center></html>");
        btn.setFont(BUTTON_FONT);
        btn.setBackground(YELLOW_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(BUTTON_SIZE);

        buttonPanel.add(imageLabel, BorderLayout.NORTH);
        buttonPanel.add(btn, BorderLayout.SOUTH);
        
        btn.addActionListener(e -> addToCart(itemName));

        return buttonPanel;
    }

    private ImageIcon loadImage(String itemName) {
        String imagePath = itemName.toLowerCase().replace(" ", "_") + ".png";
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getIconWidth() == -1) {
            icon = new ImageIcon(); // Placeholder or default image
        }
        return icon;
    }

    private JPanel createCartPanel() {
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Your Order"));
        cartPanel.setPreferredSize(new Dimension(400, 0));

        cartArea = new JTextArea();
        cartArea.setEditable(false);
        cartArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        cartArea.setMargin(new Insets(10, 10, 10, 10));

        cartPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);
        return cartPanel;
    }

    private JPanel createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(CONTROL_PANEL_COLOR);

        JButton confirmButton = createControlButton("Confirm Order", Color.GREEN);
        JButton clearButton = createControlButton("Clear Order", Color.ORANGE);
        JButton exitButton = createControlButton("Exit", Color.RED);
        JButton printReceiptButton = createControlButton("Print Receipt", Color.BLUE);

        confirmButton.addActionListener(e -> confirmOrder());
        clearButton.addActionListener(e -> clearOrder());
        exitButton.addActionListener(e -> System.exit(0));
        printReceiptButton.addActionListener(e -> printReceipt());

        controlPanel.add(confirmButton);
        controlPanel.add(clearButton);
        controlPanel.add(printReceiptButton);
        controlPanel.add(exitButton);
        return controlPanel;
    }

    private JButton createControlButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(CONTROL_BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(CONTROL_BUTTON_SIZE);
        return btn;
    }

    private void addToCart(String item) {
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity for " + item + ":");
        if (quantityStr != null && !quantityStr.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0) {
                    double price = menuPrices.get(item);
                    total += price * quantity;
                    orderQuantities.put(item, orderQuantities.getOrDefault(item, 0) + quantity);
                    updateCart();
                } else {
                    showError("Quantity must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        }
    }

    private void updateCart() {
        cartArea.setText("");
        orderQuantities.forEach((item, quantity) -> {
            double price = menuPrices.get(item);
            cartArea.append(String.format("%-20s x%d ₱%.2f\n", item, quantity, price * quantity));
        });
        updateCartTotal();
    }

    private void updateCartTotal() {
        String orderType = dineInRadio.isSelected() ? "Dine-in" : "Take-out";
        cartArea.append(String.format("\n%-20s ₱%.2f\n", "TOTAL (" + orderType + "):", total));
    }

    private void clearOrder() {
        cartArea.setText("");
        total = 0.0;
        orderQuantities.clear();
        dineInRadio.setSelected(true);
    }

    private void confirmOrder() {
        if (total == 0) {
            showError("Please select items first!");
            return;
        }

        String orderType = dineInRadio.isSelected() ? "Dine-in" : "Take-out";
        int choice = JOptionPane.showConfirmDialog(this, 
            "Confirm " + orderType + " order for ₱" + total + "?", 
            "Confirm Order", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Order submitted!\nThank you!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearOrder();
        }
    }

    private void printReceipt() {
        if (total == 0) {
            showError("No items in the cart to print a receipt!");
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt\n");
        receipt.append("================================\n");
        orderQuantities.forEach((item, quantity) -> {
            double price = menuPrices.get(item);
            receipt.append(String.format("%-20s x%d ₱%.2f\n", item, quantity, price * quantity));
        });
        receipt.append("================================\n");
        String orderType = dineInRadio.isSelected() ? "Dine-in" : "Take-out";
        receipt.append(String.format("%-20s ₱%.2f\n", "TOTAL (" + orderType + "):", total));
        receipt.append("Thank you for your order!\n");

        JTextArea receiptArea = new JTextArea(receipt.toString());
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JOptionPane.showMessageDialog(this, new JScrollPane(receiptArea), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addEscapeKeyListener() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestaurantPOS pos = new RestaurantPOS();
            pos.setVisible(true);
        });
    }
}