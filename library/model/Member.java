package library.model;

public class Member {

    private int id;
    private String name;
    private String phone;
    private String email;
    private String joinDate;
    private int booksIssued;


    public Member(int id,
                  String name,
                  String phone,
                  String email,
                  String joinDate) {

        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.joinDate = joinDate;

        this.booksIssued = 0;
    }


    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public int getBooksIssued() {
        return booksIssued;
    }


    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // Book Counter

    public void incrementBooks() {
        booksIssued++;
    }

    public void decrementBooks() {

        if (booksIssued > 0) {
            booksIssued--;
        }
    }


    // Table Row

    public Object[] toTableRow() {

        return new Object[]{

                id,
                name,
                phone,
                email,
                joinDate,
                booksIssued

        };
    }
}