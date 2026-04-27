/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posinventorysystem.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ThemeUtil {

    public static final Color BLUE = new Color(0, 51, 153);
    public static final Color RED = new Color(204, 0, 0);
    public static final Color WHITE = Color.WHITE;
    public static final Color DARK_TEXT = new Color(20, 20, 20);

    private ThemeUtil() {
        // prevent object creation
    }

    public static void applyFrameTheme(JFrame frame) {
        frame.getContentPane().setBackground(WHITE);
    }

    public static void styleMainPanel(JPanel panel) {
        panel.setBackground(WHITE);
    }

    public static void styleHeaderPanel(JPanel panel) {
        panel.setBackground(BLUE);
    }

    public static void styleHeaderLabel(JLabel label) {
        label.setForeground(WHITE);
    }

    public static void styleNormalLabel(JLabel label) {
        label.setForeground(DARK_TEXT);
    }

    public static void stylePrimaryButton(JButton button) {
        styleButton(button, BLUE, RED, WHITE);
    }

    public static void styleDangerButton(JButton button) {
        styleButton(button, RED, BLUE, WHITE);
    }

    public static void styleButton(JButton button, Color normalColor, Color hoverColor, Color textColor) {
        button.setBackground(normalColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    public static void setRedBorder(JPanel panel) {
        panel.setBorder(new LineBorder(RED, 3));
    }

    public static void styleWhitePanelWithRedBorder(JPanel panel) {
        panel.setBackground(WHITE);
        panel.setBorder(new LineBorder(RED, 2));
    }

    public static void styleAllPanelsWhite(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel panel) {
                panel.setBackground(WHITE);
                styleAllPanelsWhite(panel);
            } else if (comp instanceof Container childContainer) {
                styleAllPanelsWhite(childContainer);
            }
        }
    }

    public static void styleAllLabelsDark(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(DARK_TEXT);
            } else if (comp instanceof Container childContainer) {
                styleAllLabelsDark(childContainer);
            }
        }
    }
    
    public static void styleTextField(javax.swing.JTextField field) {
        field.setBackground(WHITE);
        field.setForeground(DARK_TEXT);
        field.setCaretColor(DARK_TEXT);
        field.setBorder(new javax.swing.border.LineBorder(BLUE, 2));
    }
    
    public static void styleTable(javax.swing.JTable table) {
        table.setBackground(WHITE);
        table.setForeground(DARK_TEXT);
        table.setSelectionBackground(BLUE);
        table.setSelectionForeground(WHITE);
        table.setGridColor(RED);

        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(BLUE);
        header.setForeground(WHITE);
    }
}