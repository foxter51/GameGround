package com.project.GameGround.service;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.*;
import com.project.GameGround.repositories.*;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private RatingRepository ratingRepo;

    public void loadReviews(Model model){
        model.addAttribute("reviews", reviewRepo.findAll());
    }

    public void loadReviewsByID(String id, Model model){
        model.addAttribute("reviews", reviewRepo.getReviewsByUserID(Long.parseLong(id)));
    }

    public void createReview(Model model){
        model.addAttribute("createReview", new Review());
        model.addAttribute("Tags", new Tags());
    }

    public void saveReview(String id, Review review, Tags tags){
        review.setUser(repo.getById(Long.parseLong(id)));
        for (String tag : tags.getTagsString().split(" ")) {  //extract tags from string
            review.addTag(Objects.requireNonNullElseGet(tagRepo.isContains(tag), () -> new Tag(tag)));  //if tag already exists we use it, else create
        }
        review.setText(markdownToHTML(review.getText()));
        review.setPublishDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        reviewRepo.save(review);
    }

    public void getReviewByID(String id, Model model){
        Review review = reviewRepo.getById(Long.parseLong(id));
        List<Comment> comments = review.getComments();
        Collections.reverse(comments);
        review.setComments(comments);
        model.addAttribute("review", review);
    }

    public void removeReviewByID(String id){
        reviewRepo.deleteReviewByID(Long.parseLong(id));
    }

    public void updateReviewByID(String id, RedirectAttributes ra, String profileID){
        Review review = reviewRepo.getById(Long.parseLong(id));
        ra.addFlashAttribute("updateReview", review);
        ra.addFlashAttribute("profileID", profileID);
    }

    public void editReview(Review review, Model model){
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));
        model.addAttribute("Tags", new Tags(tags.toString()));
        model.addAttribute("updateReview", review);
    }

    public void addComment(Model model){
        model.addAttribute("newComment", new Comment());
    }

    public void saveComment(String reviewID, String userID, Comment comment){
        comment.setUser(repo.getById(Long.parseLong(userID)));
        comment.setReviewID(Long.parseLong(reviewID));
        comment.setPublishDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        Comment savedComment = commentRepo.save(comment);
        Review review = reviewRepo.getById(Long.parseLong(reviewID));
        review.addComment(savedComment);
        reviewRepo.save(review);
    }

    public void addRatingPossibility(String reviewID, Model model){
        Long currentUserID = (Long) model.getAttribute("currentUserID");
        model.addAttribute("ratePossibility", !isContainsCurrentUser(reviewID, currentUserID));
    }

    public boolean isContainsCurrentUser(String reviewID, Long currentUserID){
        for(RatedBy ratedBy : reviewRepo.getById(Long.parseLong(reviewID)).getBlockedToRate()){
            if(Objects.equals(ratedBy.getUser().getId(), currentUserID)){
                return true;
            }
        }
        return false;
    }

    public void changeRate(String reviewID, String userID, String starValue){
        Review review = reviewRepo.getById(Long.parseLong(reviewID));
        float rate = review.getRate();
        float rateCount = review.getRateCount();
        review.setRate((rateCount*rate + Float.parseFloat(starValue))/(rateCount+1));
        review.setRateCount((int)(rateCount+1));
        RatedBy savedRatedBy = ratingRepo.save(new RatedBy(Long.parseLong(reviewID), repo.getById(Long.parseLong(userID))));
        review.addBlockedToRate(savedRatedBy);
        reviewRepo.save(review);
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
