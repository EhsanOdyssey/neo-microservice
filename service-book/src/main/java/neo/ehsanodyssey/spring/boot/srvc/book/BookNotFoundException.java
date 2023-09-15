package neo.ehsanodyssey.spring.boot.srvc.book;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotFoundException extends RuntimeException {
    BookNotFoundException(String message) {
        super(message);
    }
}
