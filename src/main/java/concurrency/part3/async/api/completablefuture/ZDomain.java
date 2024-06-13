package concurrency.part3.async.api.completablefuture;

class Email {

	private long id;

	public Email() {
	}

	public Email(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Email [id=" + id + "]";
	}
}

class User {

	private long id;

	public User() {
	}

	public User(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + "]";
	}

}

class Quotation {
	private String serverName;
	private int amount;

	public Quotation(String name, int amount) {
		this.serverName = name;
		this.amount = amount;
	}

	public int amount() {
		return amount;
	}
}
