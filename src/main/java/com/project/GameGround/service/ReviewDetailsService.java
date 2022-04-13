package com.project.GameGround.service;

import com.project.GameGround.Constants;
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

import java.text.SimpleDateFormat;
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
        review.setPublishDate(new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));
        review.setAuthorRate(starValue);
        repo.save(review);
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
                reviews = repo.getReviewsByTag(sortBy);
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

    public Review getReviewByID(String reviewID){
        return repo.getById(Long.parseLong(reviewID));
    }

    public void removeReviewByID(String reviewID){
        repo.deleteById(Long.parseLong(reviewID));
    }

    public Review getReviewToUpdate(String reviewID){  //get old review to update
        return repo.getById(Long.parseLong(reviewID));
    }

    public Tags getOldTags(Review review){
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));  //get old tags
        return new Tags(tags.toString());
    }

    public Review reviewTextToMarkdown(Review review){  //loading review to update
        review.setText(new CopyDown().convert(review.getText()));  //convert from html to markdown
        return review;
    }

    public void saveComment(String reviewID, String userID, Comment comment){
        comment.setUser(userDetailsService.repo.getById(Long.parseLong(userID)));
        comment.setReviewID(Long.parseLong(reviewID));
        comment.setPublishDate(new SimpleDateFormat(Constants.dateTimeFormat).format(new Date()));
        Comment savedComment = commentDetailsService.repo.save(comment);
        Review review = repo.getById(Long.parseLong(reviewID));
        review.addComment(savedComment);
        repo.save(review);
    }

    public boolean isUserNotRated(String reviewID, Long currentUserID, String rateType){  //check if user rated to give him possibility
        List<RatedBy> usersRated = repo.getById(Long.parseLong(reviewID)).getBlockedToRate();
        for(RatedBy user : usersRated){
            if(Objects.equals(user.getUser().getId(), currentUserID) && user.getRateType().equals(rateType)){
                return false;
            }
        }
        return true;
    }

    public void changeRate(String reviewID, String userID, String starValue){
        Review review = repo.getById(Long.parseLong(reviewID));
        float rate = review.getRate();
        float rateCount = review.getRateCount();
        review.setRate((rateCount*rate + Float.parseFloat(starValue))/(rateCount+1));  //count new review rate
        review.setRateCount((int)(rateCount+1));
        RatedBy user = ratingDetailsService.repo.save(new RatedBy(Long.parseLong(reviewID), userDetailsService.repo.getById(Long.parseLong(userID)), "RATING"));
        review.addBlockedToRate(user);  //remember user rated
        repo.save(review);
    }

    public void likeReview(String reviewID, String userID){
        userDetailsService.repo.incrementLike(Long.parseLong(userID));
        Review review = repo.getById(Long.parseLong(reviewID));
        RatedBy user = ratingDetailsService.repo.save(new RatedBy(Long.parseLong(reviewID), userDetailsService.repo.getById(Long.parseLong(userID)), "LIKE"));
        review.addBlockedToRate(user);  //remember user liked
        repo.save(review);
    }

    public Set<Tag> getLast6Tags(){
        Set<Tag> last6tags = tagDetailsService.repo.findFirst6ByOrderByIdDesc();
        return last6tags.size()>0 ? last6tags : null;
    }

    public List<Review> getReviewsByGenre(String groupName){
        return repo.getReviewsByGroupName(groupName);
    }

    public List<String> getLastGenres(String withoutGenre){
        return repo.getLast4Genres(withoutGenre);
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
