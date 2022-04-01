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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public void createReview(Model model){
        model.addAttribute("createReview", new Review());
        model.addAttribute("Tags", new Tags());
    }

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

    public void loadReviews(Model model, String sortBy){  //load all reviews depending on sort type
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
        model.addAttribute("reviews", reviews);
    }

    public void loadReviewsByID(String userID, Model model, String sortBy){  //load reviews by id depending on sort type
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
        model.addAttribute("reviews", reviews.size() > 0 ? reviews : null);
    }

    public void loadReviewBySearch(Model model, String request){  //load reviews as a search result
        List<Review> foundReviews = repo.search(request);
        model.addAttribute("searchResult", foundReviews.size() > 0);
        model.addAttribute("searchRequest", request);
        model.addAttribute("reviews", foundReviews.size()>0 ? foundReviews : null);
    }

    public void getReviewByID(String reviewID, Model model){
        Review review = repo.getById(Long.parseLong(reviewID));
        model.addAttribute("review", review);
    }

    public void removeReviewByID(String reviewID){
        repo.deleteById(Long.parseLong(reviewID));
    }

    public void sendReviewToUpdate(String reviewID, RedirectAttributes ra, String profileID){  //get old review to update
        Review review = repo.getById(Long.parseLong(reviewID));
        ra.addFlashAttribute("updateReview", review);
        ra.addFlashAttribute("profileID", profileID);
    }

    public void loadReviewToUpdate(Review review, Model model){  //loading review to update
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));  //get old tags
        model.addAttribute("Tags", new Tags(tags.toString()));
        review.setText(new CopyDown().convert(review.getText()));  //convert from html to markdown
        model.addAttribute("updateReview", review);
    }

    public void addComment(Model model){
        model.addAttribute("newComment", new Comment());
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

    public void addRatingPossibility(String reviewID, Model model, Authentication auth){  //get possibility to rate
        Long currentUserID = userDetailsService.getCurrentUserID(auth);
        model.addAttribute("ratePossibility", isUserNotRated(reviewID, currentUserID, "RATING"));
        model.addAttribute("likePossibility", isUserNotRated(reviewID, currentUserID, "LIKE"));
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

    public void getLast5Tags(Model model){
        Set<Tag> last5tags = tagDetailsService.repo.findFirst5ByOrderByIdDesc();
        model.addAttribute("last5tags", last5tags.size()>0 ? last5tags : null);
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
