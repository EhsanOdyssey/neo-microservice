package neo.ehsanodyssey.spring.boot.micro.gw.client.book;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "book-service")
public interface BooksClient {

    @RequestMapping(value = "/books/{bookId}", method = {RequestMethod.GET})
    Book getBookById(@PathVariable("bookId") Long bookId);
}
