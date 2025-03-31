package models;

public class Contact {
    private final Integer id;
    private String name;
    private String email;
    private String phone;

    public Contact() {
        // Constructor
        this.id = null; // Default Value
    }

    public Contact(Integer id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // (id has no setter because it is final)
    public Integer getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

}
