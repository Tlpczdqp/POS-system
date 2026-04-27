/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package posinventorysystem.forms;

/**
 *
 * @author Tlpczdqp
 */

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import posinventorysystem.db.DBConnection;

public class SalesHistoryForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SalesHistoryForm.class.getName());
    
    private int selectedSaleId = 0;
    private String selectedInvoiceNo = "";
    /**
     * Creates new form SalesHistoryForm
     */
    public SalesHistoryForm() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        loadSales();
    }
    
    private void loadSales() {
        DefaultTableModel model = (DefaultTableModel) tblSales.getModel();
        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM sales ORDER BY sale_id DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("sale_id"),
                    rs.getString("invoice_no"),
                    rs.getString("customer_name"),
                    rs.getString("sale_date"),
                    rs.getDouble("total"),
                    rs.getDouble("payment"),
                    rs.getDouble("balance"),
                    rs.getString("cashier")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load sales error: " + e.getMessage());
        }
    }

    private void searchSales() {
        DefaultTableModel model = (DefaultTableModel) tblSales.getModel();
        model.setRowCount(0);

        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM sales WHERE invoice_no LIKE ? ORDER BY sale_id DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + txtSearchInvoice.getText().trim() + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("sale_id"),
                    rs.getString("invoice_no"),
                    rs.getString("customer_name"),
                    rs.getString("sale_date"),
                    rs.getDouble("total"),
                    rs.getDouble("payment"),
                    rs.getDouble("balance"),
                    rs.getString("cashier")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    private String buildReceiptFromDatabase(int saleId) {
        StringBuilder sb = new StringBuilder();

        try {
            Connection con = DBConnection.getConnection();

            String saleSql = "SELECT * FROM sales WHERE sale_id=?";
            PreparedStatement salePst = con.prepareStatement(saleSql);
            salePst.setInt(1, saleId);
            ResultSet saleRs = salePst.executeQuery();

            if (saleRs.next()) {
                sb.append("========================================\n");
                sb.append("           MY POS SYSTEM            \n");
                sb.append("========================================\n");
                sb.append("Invoice No : ").append(saleRs.getString("invoice_no")).append("\n");
                sb.append("Customer   : ").append(saleRs.getString("customer_name")).append("\n");
                sb.append("Date       : ").append(saleRs.getString("sale_date")).append("\n");
                sb.append("Cashier    : ").append(saleRs.getString("cashier")).append("\n");
                sb.append("========================================\n");
                sb.append(String.format("%-15s %5s %7s %8s\n", "Item", "Qty", "Price", "Total"));
                sb.append("----------------------------------------\n");

                String itemSql = "SELECT * FROM sale_items WHERE sale_id=?";
                PreparedStatement itemPst = con.prepareStatement(itemSql);
                itemPst.setInt(1, saleId);
                ResultSet itemRs = itemPst.executeQuery();

                while (itemRs.next()) {
                    String productName = itemRs.getString("product_name");
                    String qty = itemRs.getString("quantity");
                    String price = itemRs.getString("price");
                    String subtotal = itemRs.getString("subtotal");

                    if (productName.length() > 15) {
                        productName = productName.substring(0, 15);
                    }

                    sb.append(String.format("%-15s %5s %7s %8s\n", productName, qty, price, subtotal));
                }

                sb.append("========================================\n");
                sb.append(String.format("Total   : %.2f\n", saleRs.getDouble("total")));
                sb.append(String.format("Payment : %.2f\n", saleRs.getDouble("payment")));
                sb.append(String.format("Balance : %.2f\n", saleRs.getDouble("balance")));
                sb.append("========================================\n");
                sb.append("      Thank you for your purchase!  \n");
                sb.append("========================================\n");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Receipt load error: " + e.getMessage());
        }

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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSales = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtSearchInvoice = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnViewItems = new javax.swing.JButton();
        btnPrintReceipt = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(this::btnHomeActionPerformed);

        btnProducts.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnProducts.setText("Products");
        btnProducts.addActionListener(this::btnProductsActionPerformed);

        btnPOS.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPOS.setText("POS");
        btnPOS.addActionListener(this::btnPOSActionPerformed);

        btnSalesHistory.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSalesHistory.setText("Sales History");
        btnSalesHistory.setEnabled(false);
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
                .addGap(0, 128, Short.MAX_VALUE))
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

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales ID", "Invoice No", "Customer Name", "Sales Date", "Total", "Payment", "Balance", "Cashier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSales);

        jLabel1.setText("Sales History");

        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        btnViewItems.setText("View Items");
        btnViewItems.addActionListener(this::btnViewItemsActionPerformed);

        btnPrintReceipt.setText("Print Receipt");
        btnPrintReceipt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPrintReceipt.addActionListener(this::btnPrintReceiptActionPerformed);

        btnClose.setText("Close");
        btnClose.addActionListener(this::btnCloseActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnViewItems)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrintReceipt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClose))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtSearchInvoice)
                        .addGap(32, 32, 32)
                        .addComponent(btnSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRefresh)))
                .addGap(12, 12, 12))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewItems)
                    .addComponent(btnClose)
                    .addComponent(btnPrintReceipt))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(navbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(navbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(474, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(52, 52, 52)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchSales();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalesMouseClicked
        int row = tblSales.getSelectedRow();

        if (row >= 0) {
            selectedSaleId = Integer.parseInt(tblSales.getValueAt(row, 0).toString());
            selectedInvoiceNo = tblSales.getValueAt(row, 1).toString();
        }
    }//GEN-LAST:event_tblSalesMouseClicked

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        txtSearchInvoice.setText("");
        loadSales();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnPrintReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintReceiptActionPerformed
        if (selectedSaleId == 0) {
            JOptionPane.showMessageDialog(this, "Select a sale first");
            return;
        }

        String receiptText = buildReceiptFromDatabase(selectedSaleId);

        ReceiptForm receiptForm = new ReceiptForm();
        receiptForm.setReceiptText(receiptText);
        receiptForm.setVisible(true);
    }//GEN-LAST:event_btnPrintReceiptActionPerformed

    private void btnViewItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewItemsActionPerformed
        if (selectedSaleId == 0) {
            JOptionPane.showMessageDialog(this, "Select a sale first");
            return;
        }

        new SaleItemsForm(selectedSaleId, selectedInvoiceNo).setVisible(true);
    }//GEN-LAST:event_btnViewItemsActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        new MainForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new SalesHistoryForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPOS;
    private javax.swing.JButton btnPrintReceipt;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSalesHistory;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewItems;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel navbarPanel;
    private javax.swing.JTable tblSales;
    private javax.swing.JTextField txtSearchInvoice;
    // End of variables declaration//GEN-END:variables
}
