package library.ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import library.manager.LibraryManager;
import library.model.Book;

public class IssueReturnPanel extends JPanel {
    private LibraryManager manager;
    private Runnable onUpdate;

    public IssueReturnPanel(LibraryManager manager, Runnable onUpdate) {
        this.manager = manager;
        this.onUpdate = onUpdate;
        setBackground(new Color(245, 246, 250));
        setLayout(new GridLayout(1, 2, 20, 0));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildIssueCard());
        add(buildReturnCard());
    }

    private JPanel buildIssueCard() {
        JPanel card = makeCard();
JLabel heading = new JLabel("📤 Issue Book");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        heading.setForeground(new Color(30, 30, 60));   
        heading.setBorder(new EmptyBorder(0, 0, 16, 0));
         JTextField bookIdF = makeField("Enter Book ID (e.g. 1)");
         JTextField memberIdF = makeField("Enter Member ID (e.g. 1)");

        JButton issueBtn = makeButton("Issue Book", new Color(45, 147, 72));
        issueBtn.addActionListener(e -> {
            try {
                int bId = Integer.parseInt(bookIdF.getText().trim());
                int mId = Integer.parseInt(memberIdF.getText().trim());
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String result = manager.issueBook(bId, mId, date);
                if (result.contains("successfully")) {
                    bookIdF.setText("");
                    memberIdF.setText("");
                    onUpdate.run();
                    JOptionPane.showMessageDialog(this,
                       "Book issued successfully!\nIssue Date : " + date,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(this, "Please enter valid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel infoBox = new JPanel(new GridLayout(3, 1, 0, 4));
        infoBox.setBackground(new Color(235, 245, 255));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 210, 255), 1, true),
            new EmptyBorder(12, 14, 12, 14)
        ));
        addInfoLine(infoBox, "📌 A member can issue up to 3 books.");
        addInfoLine(infoBox, "📌 Book IDs are available in the Books section.");
        addInfoLine(infoBox, "📌  Member IDs are available in the Members section.");

        card.add(heading);
        card.add(new JLabel("Book ID:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); setForeground(new Color(80,80,110)); }});
        card.add(bookIdF);
        card.add(Box.createVerticalStrut(6));
        card.add(new JLabel("Member ID:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); setForeground(new Color(80,80,110)); }});
        card.add(memberIdF);
        card.add(Box.createVerticalStrut(10));
        card.add(issueBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(infoBox);

        return card;
    }

    private JPanel buildReturnCard() {
        JPanel card = makeCard();

        JLabel heading = new JLabel("📥 Please return the book ");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        heading.setForeground(new Color(30, 30, 60));
        heading.setBorder(new EmptyBorder(0, 0, 16, 0));

        JTextField bookIdF = makeField("Enter Book ID to return");
        JLabel previewLbl = new JLabel(" ");
        previewLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        previewLbl.setForeground(new Color(100, 100, 150));

        bookIdF.addActionListener(e -> showPreview(bookIdF, previewLbl));

        JButton previewBtn = makeButton("Check Book", new Color(79, 142, 247));
        previewBtn.addActionListener(e -> showPreview(bookIdF, previewLbl));

        JButton returnBtn = makeButton("Confirm  the book !", new Color(192, 57, 43));
        returnBtn.addActionListener(e -> {
            try {
                int bId = Integer.parseInt(bookIdF.getText().trim());
                String result = manager.returnBook(bId);
                if (result.contains("successfully")){
                    bookIdF.setText("");
                    previewLbl.setText(" ");
                    onUpdate.run();
                    JOptionPane.showMessageDialog(this,
                        "Book return successfully !",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please Enter the valid Book id!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(heading);
        card.add(new JLabel("Book ID:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); setForeground(new Color(80,80,110)); }});
        card.add(bookIdF);
        card.add(Box.createVerticalStrut(6));
        card.add(previewBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(previewLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(returnBtn);

        return card;
    }

    private void showPreview(JTextField field, JLabel lbl) {
        try {
            int bId = Integer.parseInt(field.getText().trim());
            Book b = manager.findBook(bId);
            if (b == null) {
                lbl.setText("❌ Book not found!");
                lbl.setForeground(Color.RED);
            } else if (!b.isIssued()) {
                lbl.setText("⚠️This book is not currently issued .");
                lbl.setForeground(new Color(180, 100, 0));
            } else {
                lbl.setText("<html>✅ <b>" + b.getTitle() + "</b><br>Issued to: " + b.getIssuedTo() + " | Date: " + b.getIssueDate() + "</html>");
                lbl.setForeground(new Color(30, 120, 50));
            }
        } catch (NumberFormatException ex) {
            lbl.setText("Please enter valid id!");
        }
    }

    private JPanel makeCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return btn;
    }

    private void addInfoLine(JPanel p, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(60, 90, 160));
        p.add(lbl);
    }
}
