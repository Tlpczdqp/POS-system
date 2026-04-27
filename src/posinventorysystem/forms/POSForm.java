/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package posinventorysystem.forms;

import java.awt.*;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import posinventorysystem.db.DBConnection;
import posinventorysystem.forms.UserSession;

/**
 *
 * @author Tlpczdqp
 */
public class POSForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(POSForm.class.getName());

    /**
     * Creates new form POSForm
     */
    public POSForm() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        productsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        loadProductCards();
        
//        applyTheme();
    }
    
//    private void applyTheme() {
//        ThemeUtil.applyFrameTheme(this);
//
//        // Main backgrounds
//        ThemeUtil.styleMainPanel(navbarPanel);
//        ThemeUtil.styleMainPanel(leftPanel);
//        ThemeUtil.styleMainPanel(rightPanel);
//        ThemeUtil.styleMainPanel(jPanel1);
//        ThemeUtil.styleMainPanel(productsPanel);
//
//        // Borders
//        ThemeUtil.setRedBorder(leftPanel);
//        ThemeUtil.setRedBorder(rightPanel);
//
//        // Navbar buttons
//        ThemeUtil.stylePrimaryButton(btnHome);
//        ThemeUtil.stylePrimaryButton(btnProducts);
//        ThemeUtil.stylePrimaryButton(btnSalesHistory);
//
//        // Current page button
//        btnPOS.setBackground(ThemeUtil.RED);
//        btnPOS.setForeground(ThemeUtil.WHITE);
//        btnPOS.setFocusPainted(false);
//        btnPOS.setBorderPainted(false);
//        btnPOS.setOpaque(true);
//        btnPOS.setContentAreaFilled(true);
//
//        // Action buttons
//        ThemeUtil.stylePrimaryButton(btnSearch);
//        ThemeUtil.stylePrimaryButton(btnCompute); // Compute
//        ThemeUtil.stylePrimaryButton(btnPay);
//        ThemeUtil.styleDangerButton(btnRemoveItem);
//        ThemeUtil.styleDangerButton(btnNewSale);
//
//        // Labels
//        ThemeUtil.styleNormalLabel(jLabel1);
//        ThemeUtil.styleNormalLabel(jLabel2);
//        ThemeUtil.styleNormalLabel(jLabel3);
//        ThemeUtil.styleNormalLabel(jLabel4);
//        ThemeUtil.styleNormalLabel(jLabel5);
//        ThemeUtil.styleNormalLabel(jLabel6);
//        ThemeUtil.styleNormalLabel(jLabel7);
//
//        // Text fields
//        ThemeUtil.styleTextField(txtSearch);
//        ThemeUtil.styleTextField(txtCustomerName);
//        ThemeUtil.styleTextField(txtPayment);
//        ThemeUtil.styleTextField(txtTotal);
//        ThemeUtil.styleTextField(txtBalance);
//
//        // Table
//        ThemeUtil.styleTable(tblCart);
//
//        // Split pane background
//        jSplitPane1.setBackground(ThemeUtil.WHITE);
//
//        // Scroll panes
//        jScrollPane1.getViewport().setBackground(ThemeUtil.WHITE);
//        jScrollPane3.getViewport().setBackground(ThemeUtil.WHITE);
//    }
//    
    private void loadProductCards() {
        productsPanel.removeAll();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products WHERE quantity > 0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("quantity");
                String imagePath = rs.getString("image_path");

                JPanel card = createProductCard(productId, productName, price, stock, imagePath);
                productsPanel.add(card);
            }

            productsPanel.revalidate();
            productsPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load products error: " + e.getMessage());
        }
    }
    
    private JPanel createProductCard(int productId, String productName, double price, int stock, String imagePath) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(180, 250));
        card.setBackground(ThemeUtil.WHITE);
        card.setBorder(BorderFactory.createLineBorder(ThemeUtil.BLUE, 2));
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));

        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(160, 110));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        try {
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                String fullPath = System.getProperty("user.dir") + File.separator + imagePath;
                ImageIcon icon = new ImageIcon(fullPath);
                Image img = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                lblImage.setText("No Image");
            }
        } catch (Exception e) {
            lblImage.setText("No Image");
        }

        JLabel lblName = new JLabel("<html><center>" + productName + "</center></html>");
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(ThemeUtil.BLUE);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel lblPrice = new JLabel("Price: " + price);
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPrice.setForeground(ThemeUtil.DARK_TEXT);
        lblPrice.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel lblStock = new JLabel("Stock: " + stock);
        lblStock.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStock.setForeground(ThemeUtil.RED);
        lblStock.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton btnAdd = new JButton("Add to Cart");
        btnAdd.setAlignmentX(JButton.CENTER_ALIGNMENT);
        ThemeUtil.stylePrimaryButton(btnAdd);

        btnAdd.addActionListener(e -> {
            addProductToCart(productId, productName, price, stock);
        });

        card.add(lblImage);
        card.add(new JLabel(" "));
        card.add(lblName);
        card.add(new JLabel(" "));
        card.add(lblPrice);
        card.add(lblStock);
        card.add(new JLabel(" "));
        card.add(btnAdd);

        return card;
    }
    private void addProductToCart(int productId, String productName, double price, int stock) {
        String qtyInput = JOptionPane.showInputDialog(this, "Enter quantity for " + productName + ":");

        if (qtyInput == null || qtyInput.trim().isEmpty()) {
            return;
        }

        try {
            int qty = Integer.parseInt(qtyInput);

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0");
                return;
            }

            if (qty > stock) {
                JOptionPane.showMessageDialog(this, "Insufficient stock");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
            boolean found = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                Object value = model.getValueAt(i, 0);

                // Skip empty/null rows
                if (value == null) {
                    continue;
                }

                int existingProductId = Integer.parseInt(value.toString());

                if (existingProductId == productId) {
                    Object qtyValue = model.getValueAt(i, 3);

                    int existingQty = 0;
                    if (qtyValue != null) {
                        existingQty = Integer.parseInt(qtyValue.toString());
                    }

                    int newQty = existingQty + qty;

                    if (newQty > stock) {
                        JOptionPane.showMessageDialog(this, "Total quantity exceeds stock");
                        return;
                    }

                    double newSubtotal = newQty * price;
                    model.setValueAt(newQty, i, 3);
                    model.setValueAt(newSubtotal, i, 4);
                    found = true;
                    break;
                }
            }

            if (!found) {
                double subtotal = qty * price;

                model.addRow(new Object[]{
                    productId,
                    productName,
                    price,
                    qty,
                    subtotal
                });
            }

            calculateTotal();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
        }
    }
    private void calculateTotal() {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 4);

            if (value != null) {
                total += Double.parseDouble(value.toString());
            }
        }

        txtTotal.setText(String.valueOf(total));
    }
    
    private void saveSale() {
        DefaultTableModel cartModel = (DefaultTableModel) tblCart.getModel();
        
        String customerName = txtCustomerName.getText().trim();
        //Check customer
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name");
            return;
        }
        
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty");
            return;
        }

        if (txtPayment.getText().trim().isEmpty() || txtBalance.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please compute payment first");
            return;
        }

        double total = Double.parseDouble(txtTotal.getText());
        double payment = Double.parseDouble(txtPayment.getText());
        double balance = Double.parseDouble(txtBalance.getText());
        String invoiceNo = generateInvoiceNo();

        try {
            Connection con = DBConnection.getConnection();
            con.setAutoCommit(false);

            String saleSql = "INSERT INTO sales(invoice_no, customer_name, total, payment, balance, cashier) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement salePst = con.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS);
            salePst.setString(1, invoiceNo);
            salePst.setString(2, customerName);
            salePst.setDouble(3, total);
            salePst.setDouble(4, payment);
            salePst.setDouble(5, balance);
            salePst.setString(6, UserSession.fullName);
            salePst.executeUpdate();

            ResultSet generatedKeys = salePst.getGeneratedKeys();
            int saleId = 0;

            if (generatedKeys.next()) {
                saleId = generatedKeys.getInt(1);
            }

            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (cartModel.getValueAt(i, 0) == null) {
                    continue;
                }

                int productId = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                String productName = cartModel.getValueAt(i, 1).toString();
                double price = Double.parseDouble(cartModel.getValueAt(i, 2).toString());
                int qty = Integer.parseInt(cartModel.getValueAt(i, 3).toString());
                double subtotal = Double.parseDouble(cartModel.getValueAt(i, 4).toString());

                String itemSql = "INSERT INTO sale_items(sale_id, product_id, product_name, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement itemPst = con.prepareStatement(itemSql);
                itemPst.setInt(1, saleId);
                itemPst.setInt(2, productId);
                itemPst.setString(3, productName);
                itemPst.setDouble(4, price);
                itemPst.setInt(5, qty);
                itemPst.setDouble(6, subtotal);
                itemPst.executeUpdate();

                String updateStockSql = "UPDATE products SET quantity = quantity - ?, status = CASE WHEN quantity - ? > 0 THEN 'Available' ELSE 'Not Available' END WHERE product_id=?";
                PreparedStatement stockPst = con.prepareStatement(updateStockSql);
                stockPst.setInt(1, qty);
                stockPst.setInt(2, qty);
                stockPst.setInt(3, productId);
                stockPst.executeUpdate();
            }

            con.commit();

            String saleDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());

            String receiptText = buildReceipt(invoiceNo, customerName, saleDate, total, payment, balance);

            ReceiptForm receiptForm = new ReceiptForm();
            receiptForm.setReceiptText(receiptText);
            receiptForm.setVisible(true);

            JOptionPane.showMessageDialog(this, "Sale completed successfully\nInvoice No: " + invoiceNo);

            clearPOS();
            loadProductCards();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Save sale error: " + e.getMessage());
        }
    }
    private String generateInvoiceNo() {
        return "INV-" + System.currentTimeMillis();
    }
    
    private void clearPOS() {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        model.setRowCount(0);

        txtCustomerName.setText("");
        txtTotal.setText("");
        txtPayment.setText("");
        txtBalance.setText("");
    }
    
    private void loadProductCards(String keyword) {
        productsPanel.removeAll();

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products WHERE quantity > 0 AND product_name LIKE '%" + keyword + "%'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("quantity");
                String imagePath = rs.getString("image_path");

                JPanel card = createProductCard(productId, productName, price, stock, imagePath);
                productsPanel.add(card);
            }

            productsPanel.revalidate();
            productsPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }
    
    private String buildReceipt(String invoiceNo, String customerName, String saleDate, double total, double payment, double balance) {
        StringBuilder sb = new StringBuilder();

        sb.append("====================================\n");
        sb.append("           MY POS SYSTEM            \n");
        sb.append("====================================\n");
        sb.append("Invoice No : ").append(invoiceNo).append("\n");
        sb.append("Customer   : ").append(customerName).append("\n");
        sb.append("Date       : ").append(saleDate).append("\n");
        sb.append("Cashier    : ").append(UserSession.fullName).append("\n");
        sb.append("====================================\n");
        sb.append(String.format("%-15s %5s %7s %8s\n", "Item", "Qty", "Price", "Total"));
        sb.append("------------------------------------\n");

        DefaultTableModel cartModel = (DefaultTableModel) tblCart.getModel();

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String productName = cartModel.getValueAt(i, 1).toString();
            String price = cartModel.getValueAt(i, 2).toString();
            String qty = cartModel.getValueAt(i, 3).toString();
            String subtotal = cartModel.getValueAt(i, 4).toString();

            if (productName.length() > 15) {
                productName = productName.substring(0, 15);
            }

            sb.append(String.format("%-15s %5s %7s %8s\n", productName, qty, price, subtotal));
        }

        sb.append("====================================\n");
        sb.append(String.format("Total   : %.2f\n", total));
        sb.append(String.format("Payment : %.2f\n", payment));
        sb.append(String.format("Balance : %.2f\n", balance));
        sb.append("====================================\n");
        sb.append("      Thank you for your purchase!  \n");
        sb.append("====================================\n");

        return sb.toString();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navbarPanel = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnProducts = new javax.swing.JButton();
        btnPOS = new javax.swing.JButton();
        btnSalesHistory = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productsPanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCart = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        txtPayment = new javax.swing.JTextField();
        txtBalance = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnPay = new javax.swing.JButton();
        btnCompute = new javax.swing.JButton();
        btnRemoveItem = new javax.swing.JButton();
        btnNewSale = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(this::btnHomeActionPerformed);

        btnProducts.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnProducts.setText("Products");
        btnProducts.addActionListener(this::btnProductsActionPerformed);

        btnPOS.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPOS.setText("POS");
        btnPOS.setEnabled(false);
        btnPOS.addActionListener(this::btnPOSActionPerformed);

        btnSalesHistory.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSalesHistory.setText("Sales History");
        btnSalesHistory.addActionListener(this::btnSalesHistoryActionPerformed);

        javax.swing.GroupLayout navbarPanelLayout = new javax.swing.GroupLayout(navbarPanel);
        navbarPanel.setLayout(navbarPanelLayout);
        navbarPanelLayout.setHorizontalGroup(
            navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarPanelLayout.createSequentialGroup()
                .addComponent(btnHome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPOS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalesHistory)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        navbarPanelLayout.setVerticalGroup(
            navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHome)
                    .addComponent(btnProducts)
                    .addComponent(btnPOS)
                    .addComponent(btnSalesHistory))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setResizeWeight(0.5);

        leftPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Product List");

        javax.swing.GroupLayout productsPanelLayout = new javax.swing.GroupLayout(productsPanel);
        productsPanel.setLayout(productsPanelLayout);
        productsPanelLayout.setHorizontalGroup(
            productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        productsPanelLayout.setVerticalGroup(
            productsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(productsPanel);

        txtSearch.setToolTipText("");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        jLabel5.setText("⌕");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSearch)))
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(leftPanel);

        rightPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Price", "Qty", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblCart);

        jLabel6.setText("Customer Name:");

        jLabel2.setText("Total:");

        jLabel3.setText("Payment:");

        txtTotal.setEditable(false);

        txtBalance.setEditable(false);
        txtBalance.addActionListener(this::txtBalanceActionPerformed);

        jLabel7.setText("Balance:");

        btnPay.setText("Pay");
        btnPay.addActionListener(this::btnPayActionPerformed);

        btnCompute.setText("Compute");
        btnCompute.addActionListener(this::btnComputeActionPerformed);

        btnRemoveItem.setText("Remove Item");
        btnRemoveItem.addActionListener(this::btnRemoveItemActionPerformed);

        btnNewSale.setText("New Sale");
        btnNewSale.addActionListener(this::btnNewSaleActionPerformed);

        jLabel8.setText("Php");

        jLabel9.setText("Php");

        jLabel10.setText("Php");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTotal)
                    .addComponent(txtPayment)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRemoveItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCompute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNewSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(btnCompute))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(btnPay))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(btnRemoveItem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNewSale)
                                .addContainerGap(28, Short.MAX_VALUE))))))
        );

        jLabel4.setText("Cart Items");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jSplitPane1.setRightComponent(rightPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navbarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(navbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBalanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceActionPerformed

    private void btnComputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComputeActionPerformed
        try {
            double total = Double.parseDouble(txtTotal.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (payment < total) {
                JOptionPane.showMessageDialog(this, "Insufficient payment");
                return;
            }

            double balance = payment - total;
            txtBalance.setText(String.valueOf(balance));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid payment input");
        }
    }//GEN-LAST:event_btnComputeActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        try {
            double total = Double.parseDouble(txtTotal.getText());
            double payment = Double.parseDouble(txtPayment.getText());

            if (payment < total) {
                JOptionPane.showMessageDialog(this, "Insufficient payment");
                return;
            }

            double balance = payment - total;
            txtBalance.setText(String.valueOf(balance));
            saveSale();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid payment input");
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveItemActionPerformed
        int row = tblCart.getSelectedRow();

        if (row >= 0) {
            DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
            model.removeRow(row);
            calculateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to remove");
        }
    }//GEN-LAST:event_btnRemoveItemActionPerformed

    private void btnNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSaleActionPerformed
        clearPOS();
    }//GEN-LAST:event_btnNewSaleActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        loadProductCards(txtSearch.getText().trim());
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        new MainForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnPOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPOSActionPerformed
        new POSForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnPOSActionPerformed

    private void btnProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsActionPerformed
        new ProductForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnProductsActionPerformed

    private void btnSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesHistoryActionPerformed
        new SalesHistoryForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSalesHistoryActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new POSForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompute;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnNewSale;
    private javax.swing.JButton btnPOS;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JButton btnSalesHistory;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel navbarPanel;
    private javax.swing.JPanel productsPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTable tblCart;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtPayment;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
