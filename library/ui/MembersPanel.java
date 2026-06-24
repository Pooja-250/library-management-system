package library.ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import library.manager.LibraryManager;
import library.model.Member;

public class MembersPanel extends JPanel {
    private LibraryManager manager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private Runnable onUpdate;

    private static final String[] COLUMNS = {"ID", "Name", "Phone", "Email", "Join Date", "Books Issued"};

    public MembersPanel(LibraryManager manager, Runnable onUpdate) {
        this.manager = manager;
        this.onUpdate = onUpdate;
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);
        refreshTable(manager.getAllMembers());
    }

    private JPanel buildTopBar() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);

        JLabel title = new JLabel("👥 Members");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 30, 60));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 34));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        searchField.addActionListener(e -> doSearch());

        JButton searchBtn = makeButton("Search", new Color(79, 142, 247));
        searchBtn.addActionListener(e -> doSearch());

        JButton addBtn = makeButton("+ Add Member", new Color(142, 68, 173));
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
        table.setSelectionBackground(new Color(230, 210, 255));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(249, 250, 253));
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
        if (q.isEmpty()) refreshTable(manager.getAllMembers());
        else refreshTable(manager.searchMembers(q));
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add a new member", true);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));

        JTextField nameF  = new JTextField(); nameF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField phoneF = new JTextField(); phoneF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField emailF = new JTextField(); emailF.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String[] lbls = {"Name:", "Phone:", "Email:"};
        JTextField[] flds = {nameF, phoneF, emailF};
        for (int i = 0; i < lbls.length; i++) {
            JLabel lbl = new JLabel(lbls[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            form.add(lbl);
            form.add(flds[i]);
        }
        form.add(new JLabel(""));

        JButton save = makeButton("Save", new Color(142, 68, 173));
        save.addActionListener(e -> {
            String n = nameF.getText().trim();
            String ph = phoneF.getText().trim();
            String em = emailF.getText().trim();
            if (n.isEmpty() || ph.isEmpty() || em.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, " please fill all fileds !", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            manager.addMember(n, ph, em, today);
            refreshTable(manager.getAllMembers());
            onUpdate.run();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Member added successfully !", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        form.add(save);

        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) { showErr("please select a member!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int issued = (int) tableModel.getValueAt(row, 5);
        if (issued > 0) { showErr("\"This member has issued books. Return them first.."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to delete this member?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            manager.deleteMember(id);
            refreshTable(manager.getAllMembers());
            onUpdate.run();
            JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    public void refreshTable(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member m : members) tableModel.addRow(m.toTableRow());
    }

    public void refresh() { refreshTable(manager.getAllMembers()); }
    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
