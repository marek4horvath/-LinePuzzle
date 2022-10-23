package sk.tuke.kpi.kp.linepuzzle.service;

import org.junit.Test;
import sk.tuke.kpi.kp.linepuzzle.entity.Comment;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommentServiceTest {
    private final String GAME_NAME = "line puzzle";
    private Date date = new Date();

    @Test
    public void testReset() {
        CommentService commentService = new CommentServiceJDBC();
        commentService.reset();
        assertEquals(0, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testAddComment() {
        CommentService commentService = new CommentServiceJDBC();
        commentService.reset();
        commentService.addComment(new Comment("Josko", GAME_NAME, "test1", date));

        List<Comment> comments = commentService.getComments(GAME_NAME);
        assertEquals(1, comments.size());

        assertEquals(GAME_NAME, comments.get(0).getGame());
        assertEquals("Josko", comments.get(0).getPlayer());
        assertEquals("test1", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

}
