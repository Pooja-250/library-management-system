package library.ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import library.manager.LibraryManager;
import library.model.Book;

public class BooksPanel extends JPanel {
    private LibraryManager manager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private Runnable onUpdate;

    private static final String[] COLUMNS = {
        "ID", "Title", "Author", "Genre", "Year", "Status", "Issued To", "Issue Date"
    };

    public BooksPanel(LibraryManager manager, Runnable onUpdate) {
        this.manager = manager;
        this.onUpdate = onUpdate;
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        refreshTable(manager.getAllBooks());
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);

        JLabel title = new JLabel("📚 Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 30, 60));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 34));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search title, author, genre...");
        searchField.addActionListener(e -> doSearch());

        JButton searchBtn = makeButton("Search", new Color(79, 142, 247));
        searchBtn.addActionListener(e -> doSearch());

        JButton addBtn = makeButton("+ Add Book", new Color(45, 147, 72));
        addBtn.addActionListener(e -> showAddDialog());

        JButton deleteBtn = makeButton("Delete", new Color(192, 57, 43));
        deleteBtn.addActionListener(e -> deleteSelected());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(searchField);
        right.add(searchBtn);
        right.add(addBtn);
        right.add(deleteBtn);

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 241, 248));
        table.getTableHeader().setForeground(new Color(80, 80, 110));
        table.setSelectionBackground(new Color(210, 228, 255));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // color rows by status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    Object status = t.getValueAt(r, 5);
                    if ("Issued".equals(status)) {
                        comp.setBackground(new Color(255, 245, 245));
                    } else {
                        comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(249, 250, 253));
                    }
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 230), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void doSearch() {
        String q = searchField.getText().trim();
        if (q.isEmpty()) refreshTable(manager.getAllBooks());
        else refreshTable(manager.searchBooks(q));
    }

    public void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) tableModel.addRow(b.toTableRow());
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Book Add Karo", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));

        JTextField titleF = new JTextField(); titleF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField authorF = new JTextField(); authorF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField genreF = new JTextField(); genreF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField yearF = new JTextField(); yearF.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] labels = {"Title:", "Author:", "Genre:", "Year:"};
        JTextField[] fields = {titleF, authorF, genreF, yearF};
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            form.add(lbl);
            form.add(fields[i]);
        }
        form.add(new JLabel(""));

        JButton save = makeButton("Save", new Color(45, 147, 72));
        save.addActionListener(e -> {
            try {
                String t = titleF.getText().trim();
                String a = authorF.getText().trim();
                String g = genreF.getText().trim();
                int y = Integer.parseInt(yearF.getText().trim());
                if (t.isEmpty() || a.isEmpty() || g.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Sab fields bharo!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                manager.addBook(t, a, g, y);
                refreshTable(manager.getAllBooks());
                onUpdate.run();
                dialog.dispose();
                showMsg("Book added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid year", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(save);

        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) { showErr("Please select a book first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 5);
        if ("Issued".equals(status)) { showErr("Issued books cannot be deleted."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure? This book will be permanently deleted.", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteBook(id);
            refreshTable(manager.getAllBooks());
            onUpdate.run();
            showMsg("Book deleted!");
        }
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(text.length() * 9 + 20, 34));
        return btn;
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void refresh() { refreshTable(manager.getAllBooks()); }
}
