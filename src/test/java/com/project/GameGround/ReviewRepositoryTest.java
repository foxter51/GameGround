package com.project.GameGround;

import com.project.GameGround.entities.Review;
import com.project.GameGround.repositories.CommentRepository;
import com.project.GameGround.repositories.ReviewRepository;
import com.project.GameGround.repositories.TagRepository;
import com.project.GameGround.repositories.UserRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private UserRepository repo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateReview(){
        Review review = new Review();
        review.setReviewName("Skyrim");
        review.setGroupName("RPG");
        review.addTag(tagRepo.getById((long) 1));
        review.addTag(tagRepo.getById((long) 2));
        review.setText("Like every great RPG, the fifth Elder Scrolls game is a grand enterprise. It’s huge in scale, epic in its breadth and scope, and will occupy such vast quantities of your time that you may find yourself losing sleep, dodging work and testing the patience of family and friends while you play it. Most of all, it’s a game destined to cast a shadow over the rest of the genre for several years to come, just as The Elder Scrolls IV: Oblivion did before it. Despite competition from Fallout 3, Mass Effect 2 and The Witcher 2, Oblivion has remained the defining RPG of this hardware generation. Skyrim comfortably improves upon it.\n" +
                "The reason is simple: Skyrim hasn’t got the best narrative of any RPG, the best combat, the best magic system or even the best graphics, but it does have one of the biggest, richest and most completely immersive worlds you’ve ever seen. Technically speaking, the visuals aren’t as detailed or beautifully lit as those of Gears of War 3 or Uncharted 3, and while the character models and facial animation are much improved on those of Oblivion or Fallout 3, they still don’t match the work being done in the Mass Effect series. Heck, The Witcher 2 has more photorealistic forests and glistening water.\n" +
                "Yet there’s something about the nordic lands of Skyrim that drags you in and won’t let go. The alpine vistas – all rugged mountains, towering pines and gushing waterfalls – are magnificent. The thought that has gone into the art direction makes each isolated village, crumbling ruin and underground tomb feel distinctive, yet part of some cohesive whole, with a history, a style and a culture informing every carving and every forged motif. It’s also one of those rare games where the visuals, sound and music merge perfectly together into one experience that has you in its thrall. Not since Howard Shore’s score for Lord of the Rings has fantasy sounded this good.\n" +
                "Skyrim is also a world that encourages exploration. The game has a central plot – and it’s a good one – but Skyrim is a game where side quests never feel like side quests, just another facet of your mission to be the best hero (or anti-hero) you can be. You can’t turn a corner of a mountain path without spotting a ruin that demands investigation or a bandit camp that could be cleared out, and each settlement has at least one decent subplot, whether it’s a serial killer on the loose or a mysterious fire that might just be linked in to something darker.\n" +
                "And it’s a world upon which you’re free to stamp your own identity. Whose side will you take in political struggles and racial conflicts? Do you want to be a gruff warrior who lets a gleaming war-axe do the talking, or a silver-tongued charmer with a bag of illusions and tricks? Will you join the Companions or hook up with the Thieves Guild? It’s up to you, and you really don’t have to make up your mind at once. One of the beauties of Skyrim is that your character can emerge and evolve over time.\n" +
                "Skyrim continues Oblivion’s work of simplifying the RPG. On the console formats, combat effectively comes down to the two trigger buttons, with each controlling one hand, and whatever spell, shields or weapons you like assigned to either trigger (though bows and two-handed weapons, for obvious reasons, will occupy both). You can block incoming blows and charge spells or attacks by holding the trigger before release. And by combining attacks with movements, you can unleash different and more devastating moves. It’s easy to pick up, and not as tough on timing or strategy as combat in Dark Souls, but it provides you with a decent range of options – and there’s some skill involved.\n" +
                "Plus, if you want to play as a warrior-mage or a dual-wielding axe maniac, it’s easy. Just assign a spell and weapon, or the arms of your choice. You can even set favourites, giving you rapid access to a list of your most-used spells and weapons at the touch of a button.\n" +
                "Meanwhile, your inventory, your map and your spell list are always accessible from a cross-shaped interface available at the touch of the B or Circle buttons. You can scroll through either using the analogue stick, and the map is nearly tied into the quest log (accessible using the Start button) so that you can quickly see where to head for the next objective. You can fast travel to areas you’ve already discovered, and at no point do the game or its systems stand in the way of going where you want to go or doing what you want to do.\n" +
                "Some might call this dumbing down, but for us Skyrim is the next stone in a path that began with Ultima Underworld nearly twenty years ago. It’s an RPG that immerses you right in the world and the action, and never pushes you out for more than the second or so it takes to manually save your game (still a smart idea if you want to make progress).\n" +
                "The experience system has seen some changes. You still develop skills for simply performing actions, so if you spend a lot of time fighting with one-handed weapons while wearing light armour and casting destructive spells, then you’ll rapidly level up in those areas. At the same time, experience is constantly dripping into a central levelling pool, and as you gain new levels you can select which of three attributes – magicka, health or stamina – will go up, and assign points to a perk that will give you extra bonuses when using certain spells or weapons, or give you useful new skills or abilities. It’s a system that provides a perfect balance between accessibility and choice.\n" +
                "The levelling system is also important because it ties into one other big change for Skyrim. In Oblivion the difficulty level of any given area – the toughness and number of its monsters – was linked to your current level. Now it’s set when you enter a dungeon or hostile area for the first time, so if you find one part too tough, you can come back later when you feel more ready to conquer it. And you will find portions of Skyrim tough going. We’ve had battles we’ve only won through the application of a handy fireball scroll, and some we’ve been unable to survive except through pegging it. Things ease up with the addition of a new system of ‘shouts’ – powerful chants in dragon language that can hurl your foes off-balance or help you speed through traps – but Skyrim rarely feels too easy or too hard.\n" +
                "Finally, it would be a disservice to Skyrim if we didn’t mention the dragons. These awesome beasts – the central focus of the plot – aren’t dished out willy-nilly, but when they are they make a suitably dramatic high-point. They’re not always quite as formidable as they might look, but each provides its own memorable fight. In fact, Skyrim is a game that comes thick with epic moments, which is impressive given the sheer scale of the game. As an idea, we’d finished half of this year’s blockbusters in the time it took us to just uncover the main storyline of Skyrim, and we hadn’t taken every side-quest or raided every tomb along the way.\n" +
                "In a way, Skyrim takes the RPG back to its roots, with its dungeons and dragons, its tunnels and its trolls. There’s something about its eerie tombs and nordic landscapes that reaches back to Tolkien, Poul Anderson and even Wagner without ever feeling like a rip-off. For anyone who has ever wanted to explore those fantasy worlds, Skyrim is nothing short of a dream come true.\n" +
                "Verdict\n" +
                "The fifth Elder Scrolls game is a worth successor to Oblivion, and one of the best RPGs ever made. Technically, it’s not a huge a progression from Oblivion, but it’s a game where all the elements – graphics, sound, art design, music and gameplay – combine to make one incredibly immersive whole. Clear your diary, take a break and cancel Christmas if you have to: unless you have an allergy to sword and sorcery, you’ll need every spare second to play.\n" +
                "\n" +
                "\n");
        File img1 = new File("src/main/resources/images/1.jpg");
        File img2 = new File("src/main/resources/images/2.jpg");
        File img3 = new File("src/main/resources/images/3.jpg");
        try{
            review.setImg1(imgToBlob(img1));
            review.setImg2(imgToBlob(img2));
            review.setImg3(imgToBlob(img3));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        review.setUser(repo.getById((long) 1));
        review.addComment(commentRepo.getById((long) 1));
        review.setRate((float)5.0);
        Review savedReview = reviewRepo.save(review);
        Review existReview = entityManager.find(Review.class, savedReview.getId());
        assertThat(existReview.getId()).isEqualTo(review.getId());
    }

    public byte[] imgToBlob(File img) throws IOException {
        return FileUtils.readFileToByteArray(img);
    }
}
