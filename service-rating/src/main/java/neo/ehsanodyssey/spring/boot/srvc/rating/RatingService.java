package neo.ehsanodyssey.spring.boot.srvc.rating;

import com.google.common.base.Preconditions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingCacheRepository cacheRepository;

    @HystrixCommand(commandKey = "ratingsByBookIdFromDB", fallbackMethod = "findCachedRatingsByBookId")
    public List<Rating> findRatingsByBookId(Long bookId) {
        return ratingRepository.findRatingsByBookId(bookId);
    }

    public List<Rating> findCachedRatingsByBookId(Long bookId) {
        return cacheRepository.findCachedRatingsByBookId(bookId);
    }

    @HystrixCommand(commandKey = "ratingsFromDB", fallbackMethod = "findAllCachedRatings")
    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    public List<Rating> findAllCachedRatings() {
        return cacheRepository.findAllCachedRatings();
    }

    @HystrixCommand(commandKey = "ratingsByIdFromDB", fallbackMethod = "findCachedRatingById", ignoreExceptions = { RatingNotFoundException.class })
    public Rating findRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFoundException("Rating not found. ID: " + ratingId));
    }

    public Rating findCachedRatingById(Long ratingId) {
        return cacheRepository.findCachedRatingById(ratingId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating createRating(Rating rating) {
        Rating newRating = new Rating();
        newRating.setBookId(rating.getBookId());
        newRating.setStars(rating.getStars());
        Rating persisted = ratingRepository.save(newRating);
        cacheRepository.createRating(persisted);
        return persisted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
        cacheRepository.deleteRating(ratingId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating updateRating(Map<String, String> updates, Long ratingId) {
        final Rating rating = findRatingById(ratingId);
        updates.keySet()
            .forEach(key -> {
                switch (key) {
                case "stars":
                    rating.setStars(Integer.parseInt(updates.get(key)));
                    break;
                }
            });
        Rating persisted = ratingRepository.save(rating);
        cacheRepository.updateRating(persisted);
        return persisted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating updateRating(Rating rating, Long ratingId) {
        Preconditions.checkNotNull(rating);
        Preconditions.checkState(rating.getId() == ratingId);
        Preconditions.checkNotNull(ratingRepository.findById(ratingId));
        return ratingRepository.save(rating);
    }

}
