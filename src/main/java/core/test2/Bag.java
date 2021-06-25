package core.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component("bag")
@Component
public class Bag {
	private Book book;
	
	public Bag() {
		
	}
	@Autowired	//생성자를 통해 따로 주입하겠다.
	public Bag(Book book) {
		this.book = book;
	}
	@Override
	public String toString() {
		return "Bag [book=" + book + "]";
	}
	
//	@Autowired
	public void setBook(Book book) {
		this.book = book;
	}
}
