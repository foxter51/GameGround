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
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

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

    public void createReview(Model model){
        model.addAttribute("createReview", new Review());
        model.addAttribute("Tags", new Tags());
    }

    public void saveReview(String userID, Review review, Tags tags){
        review.setUser(repo.getById(Long.parseLong(userID)));
        for (String tag : tags.getTagsString().split(" ")) {  //extract tags from string
            review.addTag(Objects.requireNonNullElseGet(tagRepo.isContains(tag), () -> new Tag(tag)));  //if tag already exists we use it, else create
        }
        review.setText(markdownToHTML(review.getText()));
        review.setPublishDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        reviewRepo.save(review);
    }

    public void loadReviews(Model model, String sortBy){
        List<Review> reviews = null;
        switch(sortBy){
            case "sort=dateASC":
                reviews = reviewRepo.findAll();
                break;
            case "sort=dateDSC":
                reviews = reviewRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
                break;
            case "sort=ratingASC":
                reviews = reviewRepo.findAll(Sort.by(Sort.Direction.ASC, "rate"));
                break;
            case "sort=ratingDSC":
                reviews = reviewRepo.findAll(Sort.by(Sort.Direction.DESC, "rate"));
                break;
            case "filter=ratingGE4":
                reviews = reviewRepo.getReviewsRatingGE4();
                break;
        }
        model.addAttribute("reviews", reviews);
    }

    public void loadReviewsByID(String userID, Model model, String sortBy){
        List<Review> reviews = reviewRepo.getReviewsByUserID(Long.parseLong(userID));
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
                reviews.removeIf(review -> review.getRate() < 4);
                break;
        }
        model.addAttribute("reviews", reviews.size() > 0 ? reviews : null);
    }

    public void getReviewByID(String reviewID, Model model){
        Review review = reviewRepo.getById(Long.parseLong(reviewID));
        model.addAttribute("review", review);
    }

    public void removeReviewByID(String reviewID){
        reviewRepo.deleteReviewByID(Long.parseLong(reviewID));
    }

    public void sendReviewToUpdate(String reviewID, RedirectAttributes ra, String profileID){
        Review review = reviewRepo.getById(Long.parseLong(reviewID));
        ra.addFlashAttribute("updateReview", review);
        ra.addFlashAttribute("profileID", profileID);
    }

    public void loadReviewToUpdate(Review review, Model model){
        StringBuilder tags = new StringBuilder();
        review.getTags().forEach(tag -> tags.append(tag.getTagName()).append(" "));
        model.addAttribute("Tags", new Tags(tags.toString()));
        review.setText(new CopyDown().convert(review.getText()));
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
        model.addAttribute("ratePossibility", !isUserRated(reviewID, currentUserID));
    }

    public boolean isUserRated(String reviewID, Long currentUserID){
        List<RatedBy> usersRated = reviewRepo.getById(Long.parseLong(reviewID)).getBlockedToRate();
        for(RatedBy user : usersRated){
            if(Objects.equals(user.getUser().getId(), currentUserID)){
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
        RatedBy user = ratingRepo.save(new RatedBy(Long.parseLong(reviewID), repo.getById(Long.parseLong(userID))));
        review.addBlockedToRate(user);  //remember user rated
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
