package library.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import library.model.Book;
import library.model.Member;

public class LibraryManager {

    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    private int bookIdCounter = 1;
    private int memberIdCounter = 1;

    public LibraryManager() {

        // Sample Books
        addBook("The Alchemist", "Paulo Coelho", "Fiction", 1988);
        addBook("Clean Code", "Robert C. Martin", "Programming", 2008);
        addBook("Effective Java", "Joshua Bloch", "Programming", 2018);
        addBook("Atomic Habits", "James Clear", "Self Help", 2018);
        addBook("Wings of Fire", "A.P.J. Abdul Kalam", "Biography", 1999);

        // Sample Members
addMember("Rahul Sharma", "9876543210",
        "rahul.sharma@gmail.com", "01/01/2024");

addMember("Priya Singh", "8765432109",
        "priya.singh@gmail.com", "15/02/2024");

addMember("Amit Kumar", "7654321098",
        "amit.kumar@gmail.com", "20/03/2024");
    }

    // -------------------- BOOKS --------------------

    public Book addBook(String title, String author,
                        String genre, int year) {

        Book book = new Book(bookIdCounter++,
                title, author, genre, year);

        books.add(book);
        return book;
    }

    public boolean deleteBook(int id) {

        Book book = findBook(id);

        if (book == null || book.isIssued())
            return false;

        books.remove(book);
        return true;
    }

    public Book findBook(int id) {

        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> searchBooks(String query) {

        String q = query.toLowerCase();

        return books.stream()
                .filter(book ->

                        book.getTitle()
                                .toLowerCase()
                                .contains(q)

                                ||

                                book.getAuthor()
                                        .toLowerCase()
                                        .contains(q)

                                ||

                                book.getGenre()
                                        .toLowerCase()
                                        .contains(q))

                .collect(Collectors.toList());
    }

    public int getTotalBooks() {
        return books.size();
    }

    public long getAvailableBooks() {

        return books.stream()
                .filter(book -> !book.isIssued())
                .count();
    }

    public long getIssuedBooks() {

        return books.stream()
                .filter(Book::isIssued)
                .count();
    }


    // -------------------- MEMBERS --------------------

    public Member addMember(String name,
                            String phone,
                            String email,
                            String joinDate) {

        Member member = new Member(
                memberIdCounter++,
                name,
                phone,
                email,
                joinDate);

        members.add(member);

        return member;
    }

    public boolean deleteMember(int id) {

        Member member = findMember(id);

        if (member == null ||
                member.getBooksIssued() > 0)

            return false;

        members.remove(member);

        return true;
    }

    public Member findMember(int id) {

        return members.stream()
                .filter(member ->
                        member.getId() == id)

                .findFirst()
                .orElse(null);
    }

    public List<Member> getAllMembers() {

        return new ArrayList<>(members);
    }

    public List<Member> searchMembers(String query) {

        String q = query.toLowerCase();

        return members.stream()

                .filter(member ->

                        member.getName()
                                .toLowerCase()
                                .contains(q)

                                ||

                                member.getPhone()
                                        .contains(q)

                                ||

                                member.getEmail()
                                        .toLowerCase()
                                        .contains(q))

                .collect(Collectors.toList());
    }

    public int getTotalMembers() {
        return members.size();
    }


    // ---------------- ISSUE BOOK ----------------

    public String issueBook(int bookId,
                            int memberId,
                            String issueDate) {

        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null)
            return "Book not found.";

        if (member == null)
            return "Member not found.";

        if (book.isIssued())
            return "This book has already been issued.";

        if (member.getBooksIssued() >= 3)
            return "A member cannot issue more than 3 books.";

        book.issue(member.getName(), issueDate);

        member.incrementBooks();

        return "Book issued successfully.";
    }


    // ---------------- RETURN BOOK ----------------

    public String returnBook(int bookId) {

        Book book = findBook(bookId);

        if (book == null)
            return "Book not found.";

        if (!book.isIssued())
            return "This book is not currently issued.";

        String memberName =
                book.getIssuedTo();

        members.stream()

                .filter(member ->
                        member.getName()
                                .equals(memberName))

                .findFirst()

                .ifPresent(Member::decrementBooks);

        book.returnBook();

        return "Book returned successfully.";
    }

}