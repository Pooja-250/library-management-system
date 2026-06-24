package library.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import library.manager.LibraryManager;

public class MainWindow extends JFrame {
    private LibraryManager manager;
    private DashboardPanel dashboardPanel;
    private BooksPanel booksPanel;
    private MembersPanel membersPanel;
    private IssueReturnPanel issueReturnPanel;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private JButton activeBtn = null;

    public MainWindow() {
        manager = new LibraryManager();

        setTitle("Library Management System");
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(20, 20, 50));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo area
        JPanel logoArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        logoArea.setBackground(new Color(20, 20, 50));
        logoArea.setMaximumSize(new Dimension(200, 70));
        logoArea.setPreferredSize(new Dimension(200, 70));
        JLabel logo = new JLabel("📖 LibraryMS");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(Color.WHITE);
        logoArea.add(logo);
        sidebar.add(logoArea);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 50, 90));
        sep.setMaximumSize(new Dimension(200, 1));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        // Nav buttons
        String[][] navItems = {
            {"dashboard", "🏠  Dashboard"},
            {"books",     "📚  Books"},
            {"members",   "👥  Members"},
            {"issue",     "📤  Issue / Return"}
        };

        for (String[] item : navItems) {
            JButton btn = makeNavButton(item[1]);
            btn.addActionListener(e -> {
                showPanel(item[0]);
                setActiveButton(btn);
            });
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(4));
            if (item[0].equals("dashboard")) {
                activeBtn = btn;
                btn.setBackground(new Color(50, 50, 100));
                btn.setForeground(Color.WHITE);
            }
        }
        sidebar.add(Box.createVerticalGlue());

        // Footer in sidebar
        JLabel footer = new JLabel("  v1.0 - Java Swing");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(100, 100, 140));
        footer.setBorder(new EmptyBorder(0, 14, 14, 0));
        sidebar.add(footer);

        // Content area
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(245, 246, 250));

        Runnable refresh = this::refreshAll;

        dashboardPanel  = new DashboardPanel(manager);
        booksPanel      = new BooksPanel(manager, refresh);
        membersPanel    = new MembersPanel(manager, refresh);
        issueReturnPanel = new IssueReturnPanel(manager, refresh);

        contentArea.add(dashboardPanel, "dashboard");
        contentArea.add(booksPanel, "books");
        contentArea.add(membersPanel, "members");
        contentArea.add(issueReturnPanel, "issue");

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    private void showPanel(String name) {

    cardLayout.show(contentArea, name);

    dashboardPanel.refresh();
    booksPanel.refresh();
    membersPanel.refresh();

}

    private void refreshAll() {
        dashboardPanel.refresh();
        booksPanel.refresh();
        membersPanel.refresh();
    }

    private void setActiveButton(JButton btn) {
        if (activeBtn != null) {
            activeBtn.setBackground(new Color(20, 20, 50));
            activeBtn.setForeground(new Color(170, 180, 210));
        }
        btn.setBackground(new Color(50, 50, 100));
        btn.setForeground(Color.WHITE);
        activeBtn = btn;
    }

    private JButton makeNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(170, 180, 210));
        btn.setBackground(new Color(20, 20, 50));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(new Color(35, 35, 75));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeBtn) btn.setBackground(new Color(20, 20, 50));
            }
        });
        return btn;
    }
}
