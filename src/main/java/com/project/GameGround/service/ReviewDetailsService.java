package com.project.GameGround.service;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.*;
import com.project.GameGround.repositories.*;
import io.github.furstenheim.CopyDown;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewDetailsService {

    @Autowired
    protected ReviewRepository repo;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private TagDetailsService tagDetailsService;

    @Autowired
    private CommentDetailsService commentDetailsService;

    @Autowired
    private RatingDetailsService ratingDetailsService;

    public void saveReview(String userID, Review review, Tags tags, Integer starValue){
        review.setUser(userDetailsService.repo.getById(Long.parseLong(userID)));
        for (String tag : tags.getTagsString().trim().split(" ")) {  //extract tags from string
            Tag newTag = new Tag(tag);
            if(tag.length() > 1) review.addTag(Objects.requireNonNullElse(tagDetailsService.repo.isContains(newTag.getTagName()), newTag));  //if tag already exists we use it, else create
        }
        review.setText(markdownToHTML(review.getText()));
        review.setAuthorRate(starValue);
        repo.save(review);
    }

    public Review getReviewByID(String reviewID){
        return repo.getById(Long.parseLong(reviewID));
    }

    public List<Review> loadReviews(String sortBy){  //load all reviews depending on sort type
        List<Review> reviews;
        switch(sortBy){
            case "sort=dateASC":
                reviews = repo.findAll();
                break;
            case "sort=dateDSC":
                reviews = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
                break;
            case "sort=ratingASC":
                reviews = repo.findAll(Sort.by(Sort.Direction.ASC, "rate"));
                break;
            case "sort=ratingDSC":
                reviews = repo.findAll(Sort.by(Sort.Direction.DESC, "rate"));
                break;
            case "filter=ratingGE4":
                reviews = repo.getReviewsRatingGE4();
                break;
            default:  //find by tag
                reviews = repo.getReviewsByTagsTagName(sortBy);
                break;
        }
        return reviews.size() > 0 ? reviews : null;
    }

    public List<Review> loadReviewsByID(String userID, String sortBy){  //load reviews by id depending on sort type
        List<Review> reviews = repo.getReviewByUserId(Long.parseLong(userID));
        switch(sortBy){
            case "sort=dateASC":
                break;
            case "sort=dateDSC":
                reviews.sort(Comparator.comparing(Review::getId).reversed());
                break;
            case "sort=ratingASC":
                reviews.sort(Comparator.comparing(Review::getRate));
                break;
            case "sort=ratingDSC":
                reviews.sort(Comparator.comparing(Review::getRate).reversed());
                break;
            case "filter=ratingGE4":
                reviews = repo.getReviewsRatingGE4ByID(Long.parseLong(userID));
                break;
        }
        return reviews.size() > 0 ? reviews : null;
    }

    public List<Review> loadReviewBySearch(String request){  //load reviews as a search result
        return repo.search(request);
    }

    public List<Review> getReviewsByGenre(String groupName){
        return repo.getReviewsByGroupName(groupName);
    }

    public List<String> getLastGenres(String withoutGenre){
        return repo.getLast4Genres(withoutGenre);
    }

    public Review getReviewToUpdate(String reviewID){  //get old review to update
        Review review = repo.getById(Long.parseLong(reviewID));
        return reviewTextToMarkdown(review);
    }

    public void removeReviewByID(String reviewID){
        repo.deleteById(Long.parseLong(reviewID));
    }

    public void addCommentToReview(String reviewID, String userID, Comment comment){
        Review review = repo.getById(Long.parseLong(reviewID));
        Comment savedComment = commentDetailsService.addComment(comment, review, Long.parseLong(userID));
        review.addComment(savedComment);
        repo.save(review);
    }

    public void addRate(String reviewID, String userID, String starValue){
        Review review = repo.getById(Long.parseLong(reviewID));
        float rate = review.getRate();
        float rateCount = review.getRateCount();
        review.setRate((rateCount*rate + Float.parseFloat(starValue))/(rateCount+1));  //count new review rate
        review.setRateCount((int)(rateCount+1));
        RatedBy user = ratingDetailsService.repo.save(new RatedBy(review, userDetailsService.repo.getById(Long.parseLong(userID)), "RATING", Integer.parseInt(starValue)));
        review.addBlockedToRate(user);  //remember user rated
        repo.save(review);
    }

    public void changeRate(String reviewID, String userID){
        User user = userDetailsService.getProfileByID(Long.parseLong(userID));
        Review review = repo.getById(Long.parseLong(reviewID));
        float userRate = ratingDetailsService.repo.getRatedByReviewAndUserAndRateType(review, user, "RATING").get().getRating();
        float rate = review.getRate();
        float rateCount = review.getRateCount();
        float newRate = rateCount-1 != 0 ? (rateCount*rate - userRate)/(rateCount-1) : 0;  //count new review rate
        review.setRate(newRate);
        review.setRateCount((int)(rateCount-1));
        ratingDetailsService.repo.deleteByUserAndRateType(user, "RATING");  //delete record user rated
        repo.save(review);
    }

    public void likeReview(String reviewID, String userID){
        User currentUser = userDetailsService.getProfileByID(Long.parseLong(userID));
        Review review = repo.getById(Long.parseLong(reviewID));
        if(ratingDetailsService.isUserLiked(review, currentUser)){  //if user liked - decrement likes
            userDetailsService.repo.decrementLike(review.getUser().getId());
            ratingDetailsService.repo.deleteByUserAndRateType(currentUser, "LIKE");
        }
        else{  //else increment likes
            userDetailsService.repo.incrementLike(review.getUser().getId());
            RatedBy user = ratingDetailsService.repo.save(new RatedBy(review, currentUser, "LIKE"));
            review.addBlockedToRate(user);  //remember user liked
            repo.save(review);
        }
    }

    public Long getPrevReviewID(Long currentReviewID){
        return repo.getPrevRevID(currentReviewID);
    }

    public Long getNextReviewID(Long currentReviewID){
        return repo.getNextRevID(currentReviewID);
    }

    public Tags getOldTags(String reviewID){
        Review review = repo.getById(Long.parseLong(reviewID));
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));  //get old tags
        return new Tags(tags.toString());
    }

    public Review reviewTextToMarkdown(Review review){
        review.setText(new CopyDown().convert(review.getText()));  //convert from html to markdown
        return review;
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }
}
