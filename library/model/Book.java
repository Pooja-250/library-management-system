package library.model;

public class Book {

    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;

    private boolean isIssued;
    private String issuedTo;
    private String issueDate;


    public Book(int id, String title, String author,
                String genre, int year) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;

        this.isIssued = false;
        this.issuedTo = "";
        this.issueDate = "";
    }


    // Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public String getIssueDate() {
        return issueDate;
    }


    // Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }


    // Issue Book

    public void issue(String memberName, String date) {

        this.isIssued = true;
        this.issuedTo = memberName;
        this.issueDate = date;
    }


    // Return Book

    public void returnBook() {

        this.isIssued = false;
        this.issuedTo = "";
        this.issueDate = "";
    }


    // Table Data

    public Object[] toTableRow() {

    return new Object[]{

            id,
            title,
            author,
            genre,
            year,

            isIssued ? "Issued" : "Available",

            issuedTo.isEmpty() ? "-" : issuedTo,

            issueDate.isEmpty() ? "-" : issueDate
    };
}
}
