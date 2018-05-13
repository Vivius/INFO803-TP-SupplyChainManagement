package univ_smb.m1.info803.model;

public class Order {
    private static int index = 0;

    private int id;
    private Specification specification;
    private Packaging packaging;
    private Transporter transporter;

    public Order(Specification specification) {
        this.specification = specification;
        this.id = index++;
    }

    public int getId() {
        return id;
    }

    public Specification getSpecification() {
        return specification;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public Transporter getTransporter() {
        return transporter;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", specification=" + specification +
                ", packaging=" + packaging +
                ", transporter=" + transporter +
                '}';
    }
}
