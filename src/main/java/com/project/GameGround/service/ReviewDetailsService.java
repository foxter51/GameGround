package com.project.GameGround.service;

import com.project.GameGround.Tags;
import com.project.GameGround.entities.Review;
import com.project.GameGround.entities.Tag;
import com.project.GameGround.repositories.ReviewRepository;
import com.project.GameGround.repositories.TagRepository;
import com.project.GameGround.repositories.UserRepository;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Service
public class ReviewDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private TagRepository tagRepo;

    public void loadReviews(Model model){
        model.addAttribute("reviews", reviewRepo.findAll());
    }

    public void loadReviewsByID(String id, Model model){
        model.addAttribute("reviews", reviewRepo.getReviewsByUserID(Long.parseLong(id)));
    }

    public void createReview(Model model){
        Review review = new Review();
        model.addAttribute("createReview", review);
        model.addAttribute("Tags", new Tags());
    }

    public void saveReview(String id, Review review, Tags tags){
        review.setUser(repo.getById(Long.parseLong(id)));
        for (String tag : tags.getTagsString().split(" ")) {  //extract tags from string
            review.addTag(Objects.requireNonNullElseGet(tagRepo.isContains(tag), () -> new Tag(tag)));  //if tag already exists we use it, else create
        }
        review.setText(markdownToHTML(review.getText()));
        reviewRepo.save(review);
    }

    public void getReviewByID(String id, Model model){
        Review review = reviewRepo.getById(Long.parseLong(id));
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

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }
}
