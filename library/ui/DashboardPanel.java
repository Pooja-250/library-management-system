package library.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import library.manager.LibraryManager;

public class DashboardPanel extends JPanel {
    private LibraryManager manager;
    private JLabel totalBooksLabel, availableLabel, issuedLabel, membersLabel;

    public DashboardPanel(LibraryManager manager) {
        this.manager = manager;
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildStatsGrid(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 30, 60));
        JLabel sub = new JLabel("Library Management System Overview");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(120, 120, 150));
        JPanel left = new JPanel(new GridLayout(2,1,0,2));
        left.setOpaque(false);
        left.add(title); left.add(sub);
        p.add(left, BorderLayout.WEST);
        return p;
    }

    private JPanel buildStatsGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);

        totalBooksLabel  = addStatCard(grid, "Total Books",     String.valueOf(manager.getTotalBooks()),    new Color(79, 142, 247),  "📚");
        availableLabel   = addStatCard(grid, "Available Books", String.valueOf(manager.getAvailableBooks()), new Color(45, 147, 72),  "✅");
        issuedLabel      = addStatCard(grid, "Issued Books",    String.valueOf(manager.getIssuedBooks()),   new Color(192, 57, 43),   "📤");
        membersLabel     = addStatCard(grid, "Total Members",   String.valueOf(manager.getTotalMembers()),  new Color(142, 68, 173),  "👥");

        return grid;
    }

    private JLabel addStatCard(JPanel parent, String label, String value, Color accent, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon + "  " + label);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        iconLabel.setForeground(new Color(120, 120, 150));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(accent);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        parent.add(card);
        return valueLabel;
    }

    public void refresh() {
        totalBooksLabel.setText(String.valueOf(manager.getTotalBooks()));
        availableLabel.setText(String.valueOf(manager.getAvailableBooks()));
        issuedLabel.setText(String.valueOf(manager.getIssuedBooks()));
        membersLabel.setText(String.valueOf(manager.getTotalMembers()));
    }
}
