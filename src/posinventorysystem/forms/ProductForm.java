/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package posinventorysystem.forms;

/**
 *
 * @author Tlpczdqp
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import java.awt.FileDialog;
import java.awt.Image;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import posinventorysystem.db.DBConnection;

public class ProductForm extends javax.swing.JFrame {
    private String selectedImagePath = "";
    private int selectedProductId = 0;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProductForm.class.getName());
    
    /**
     * Creates new form ProductForm
     */
    public ProductForm() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        loadProducts();
    }
    
    
    private void addProduct() {
        try {
            String productName = txtProductName.getText().trim();
            String category = txtCategory.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            String savedImagePath = "";

            if (!selectedImagePath.isEmpty()) {
                savedImagePath = copyImageToProjectFolder(selectedImagePath);
            }

            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO products(product_name, category, price, quantity, image_path, status) VALUES (?, ?, ?, ?, ?, ?)";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            pst.setString(2, category);
            pst.setDouble(3, price);
            pst.setInt(4, quantity);
            pst.setString(5, savedImagePath);
            
            String status = quantity > 0 ? "Available" : "Not Available";
            pst.setString(6, status);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product added successfully.");
            loadProducts();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add product error: " + e.getMessage());
        }
    }
    private void loadProducts() {
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("image_path"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void updateProduct() {
        if (selectedProductId == 0) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }

        try {
            String productName = txtProductName.getText().trim();
            String category = txtCategory.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            String finalImagePath = txtImagePath.getText().trim();

            if (!selectedImagePath.isEmpty()) {
                finalImagePath = copyImageToProjectFolder(selectedImagePath);
            }

            // auto-set status based on quantity
            String status = quantity > 0 ? "Available" : "Not Available";

            Connection con = DBConnection.getConnection();
            String sql = "UPDATE products SET product_name=?, category=?, price=?, quantity=?, image_path=?, status=? WHERE product_id=?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, productName);
            pst.setString(2, category);
            pst.setDouble(3, price);
            pst.setInt(4, quantity);
            pst.setString(5, finalImagePath);
            pst.setString(6, status);
            pst.setInt(7, selectedProductId);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Product updated successfully.");
            loadProducts();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update product error: " + e.getMessage());
        }
    }
    
    private void deleteProduct() {
        if (selectedProductId == 0) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "DELETE FROM products WHERE product_id=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, selectedProductId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product Deleted");
                loadProducts();
                clearFields();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private void clearFields() {
        txtProductName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtImagePath.setText("");
        lblImage.setIcon(null);
        lblImage.setText("No Image");

        selectedImagePath = "";
        selectedProductId = 0;
    }
    
    private void addStock() {
        if (selectedProductId == 0) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Enter stock to add:");
        if (input != null && !input.isEmpty()) {
            int addQty = Integer.parseInt(input);

            try {
                Connection con = DBConnection.getConnection();
                String sql = "UPDATE products SET quantity = quantity + ?, status = CASE WHEN quantity + ? > 0 THEN 'Available' ELSE 'Not Available' END WHERE product_id=?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, addQty);
                pst.setInt(2, addQty);
                pst.setInt(3, selectedProductId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Stock updated successfully");
                loadProducts();
                clearFields();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private String copyImageToProjectFolder(String sourceImagePath) {
        try {
            File sourceFile = new File(sourceImagePath);

            if (!sourceFile.exists()) {
                JOptionPane.showMessageDialog(this, "Selected image file does not exist.");
                return "";
            }

            String projectPath = System.getProperty("user.dir");
            File imageDir = new File(projectPath + File.separator + "product_images");

            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + sourceFile.getName();
            File destinationFile = new File(imageDir, fileName);

            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return "product_images" + File.separator + fileName;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Image copy error: " + e.getMessage());
            return "";
        }
    }
    
    private ImageIcon resizeImage(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(path);
        Image originalImage = originalIcon.getImage();

        int originalWidth = originalIcon.getIconWidth();
        int originalHeight = originalIcon.getIconHeight();

        if (originalWidth <= 0 || originalHeight <= 0) {
            return null;
        }

        double widthRatio = (double) width / originalWidth;
        double heightRatio = (double) height / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        navbarPanel = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        btnProducts = new javax.swing.JButton();
        btnPOS = new javax.swing.JButton();
        btnSalesHistory = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txtProductName = new javax.swing.JTextField();
        txtCategory = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        btnBrowseImage = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnAddStock = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtImagePath = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        navbarPanel.setBackground(new java.awt.Color(255, 255, 255));

        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(this::btnHomeActionPerformed);

        btnProducts.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnProducts.setText("Products");
        btnProducts.setEnabled(false);
        btnProducts.addActionListener(this::btnProductsActionPerformed);

        btnPOS.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPOS.setText("POS");
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
                .addComponent(btnSalesHistory))
        );
        navbarPanelLayout.setVerticalGroup(
            navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPOS)
                        .addComponent(btnSalesHistory))
                    .addGroup(navbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnHome)
                        .addComponent(btnProducts)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane1.setResizeWeight(0.3);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txtProductName.setText("ProductName");

        txtCategory.setText("Category");
        txtCategory.addActionListener(this::txtCategoryActionPerformed);

        txtPrice.setText("Price");

        txtQuantity.setText("Quantity");

        btnBrowseImage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBrowseImage.setText("BrowseImage");
        btnBrowseImage.addActionListener(this::btnBrowseImageActionPerformed);

        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAdd.setText("Add Product");
        btnAdd.setToolTipText("");
        btnAdd.addActionListener(this::btnAddActionPerformed);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);

        btnAddStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddStock.setText("Add Stock");
        btnAddStock.setToolTipText("");
        btnAddStock.addActionListener(this::btnAddStockActionPerformed);

        jLabel1.setText("Product Name:");

        jLabel2.setText("Category:");

        jLabel3.setText("Price:");

        jLabel4.setText("Quantity:");

        lblImage.setBackground(new java.awt.Color(255, 255, 255));
        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblImage.setMaximumSize(new java.awt.Dimension(150, 150));
        lblImage.setMinimumSize(new java.awt.Dimension(150, 150));

        txtImagePath.setEditable(false);
        txtImagePath.setText("ImagePath");
        txtImagePath.addActionListener(this::txtImagePathActionPerformed);
        jScrollPane3.setViewportView(txtImagePath);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCategory)
                                    .addComponent(txtPrice)
                                    .addComponent(txtQuantity)
                                    .addComponent(txtProductName)))
                            .addComponent(jScrollPane3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBrowseImage))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(59, 59, 59))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(29, 29, 29)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBrowseImage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnAddStock))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Category", "Price", "Qty", "Image path", "status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducts);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(navbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(navbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductsMouseClicked
        // TODO add your handling code here:
        int row = tblProducts.getSelectedRow();

        selectedProductId = Integer.parseInt(tblProducts.getValueAt(row, 0).toString());
        txtProductName.setText(tblProducts.getValueAt(row, 1).toString());
        txtCategory.setText(tblProducts.getValueAt(row, 2).toString());
        txtPrice.setText(tblProducts.getValueAt(row, 3).toString());
        txtQuantity.setText(tblProducts.getValueAt(row, 4).toString());
        txtImagePath.setText(tblProducts.getValueAt(row, 5).toString());

        String imagePath = tblProducts.getValueAt(row, 5).toString();

        if (imagePath != null && !imagePath.isEmpty()) {
            lblImage.setText("");
            lblImage.setIcon(resizeImage(imagePath, 150, 150));
        } else {
            lblImage.setIcon(null);
            lblImage.setText("No Image");
        }

        selectedImagePath = "";
    }//GEN-LAST:event_tblProductsMouseClicked

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

    private void btnAddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStockActionPerformed
        // TODO add your handling code here:
        addStock();
    }//GEN-LAST:event_btnAddStockActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        deleteProduct();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateProduct();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        addProduct();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnBrowseImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseImageActionPerformed
        // TODO add your handling code here:
        FileDialog fd = new FileDialog(this, "Select Product Image", FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.home") + "\\Pictures");

        fd.setFilenameFilter((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg")
            || lower.endsWith(".jpeg") || lower.endsWith(".gif");
        });

        fd.setVisible(true);

        String directory = fd.getDirectory();
        String filename = fd.getFile();

        if (directory != null && filename != null) {
            selectedImagePath = directory + filename;
            txtImagePath.setText(selectedImagePath);

            //            ImageIcon icon = new ImageIcon(selectedImagePath);
            //            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            //            lblImage.setIcon(new ImageIcon(img));
            lblImage.setText("");
            lblImage.setIcon(resizeImage(selectedImagePath, 150, 150));
        }

    }//GEN-LAST:event_btnBrowseImageActionPerformed

    private void txtImagePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImagePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtImagePathActionPerformed

    private void txtCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCategoryActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCategoryActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

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
        
        java.awt.EventQueue.invokeLater(() -> new ProductForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnBrowseImage;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPOS;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnSalesHistory;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JPanel navbarPanel;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtImagePath;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtQuantity;
    // End of variables declaration//GEN-END:variables
}
