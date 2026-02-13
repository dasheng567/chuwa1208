package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Post post1;
    private Post post2;
    private Comment comment1;
    private CommentDto commentDtoReq;
    private CommentDto commentDtoResp;

    @BeforeEach
    void setUp() {
        post1 = new Post();
        post1.setId(1L);

        post2 = new Post();
        post2.setId(2L);

        comment1 = new Comment();
        comment1.setId(10L);
        comment1.setPost(post1);
        comment1.setName("n1");
        comment1.setEmail("e1@test.com");
        comment1.setBody("b1");

        commentDtoReq = new CommentDto();
        commentDtoReq.setName("newName");
        commentDtoReq.setEmail("newEmail@test.com");
        commentDtoReq.setBody("newBody");

        commentDtoResp = new CommentDto();
        commentDtoResp.setName("mappedName");
        commentDtoResp.setEmail("mappedEmail@test.com");
        commentDtoResp.setBody("mappedBody");
    }

    // --------------------
    // createComment
    // --------------------

    @Test
    void createComment_success_setsPost_saves_andReturnsDto() {
        long postId = 1L;

        Comment mappedEntity = new Comment(); // result of dto->entity mapping
        Comment savedEntity = new Comment();
        savedEntity.setId(99L);
        savedEntity.setPost(post1);

        when(modelMapper.map(commentDtoReq, Comment.class)).thenReturn(mappedEntity);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, CommentDto.class)).thenReturn(commentDtoResp);

        CommentDto result = commentService.createComment(postId, commentDtoReq);

        assertNotNull(result);
        assertEquals("mappedName", result.getName());

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        Comment toSave = captor.getValue();
        assertNotNull(toSave.getPost());
        assertEquals(1L, toSave.getPost().getId());

        verify(postRepository).findById(postId);
        verify(modelMapper).map(commentDtoReq, Comment.class);
        verify(modelMapper).map(savedEntity, CommentDto.class);
    }

    @Test
    void createComment_postNotFound_throwsResourceNotFound() {
        long postId = 1L;

        when(modelMapper.map(commentDtoReq, Comment.class)).thenReturn(new Comment());
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.createComment(postId, commentDtoReq));

        verify(commentRepository, never()).save(any());
    }

    // --------------------
    // getCommentsByPostId
    // --------------------

    @Test
    void getCommentsByPostId_emptyList_returnsEmptyDtos() {
        long postId = 1L;
        when(commentRepository.findByPostId(postId)).thenReturn(Collections.emptyList());

        List<CommentDto> result = commentService.getCommentsByPostId(postId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), eq(CommentDto.class));
    }

    @Test
    void getCommentsByPostId_nonEmpty_mapsEachCommentToDto() {
        long postId = 1L;

        Comment c1 = new Comment(); c1.setId(1L); c1.setPost(post1);
        Comment c2 = new Comment(); c2.setId(2L); c2.setPost(post1);

        when(commentRepository.findByPostId(postId)).thenReturn(List.of(c1, c2));
        when(modelMapper.map(any(Comment.class), eq(CommentDto.class)))
                .thenReturn(new CommentDto());

        List<CommentDto> result = commentService.getCommentsByPostId(postId);

        assertEquals(2, result.size());
        verify(modelMapper, times(2)).map(any(Comment.class), eq(CommentDto.class));
    }

    // --------------------
    // getCommentById
    // --------------------

    @Test
    void getCommentById_success_samePost_returnsDto() {
        long postId = 1L;
        long commentId = 10L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
        when(modelMapper.map(comment1, CommentDto.class)).thenReturn(commentDtoResp);

        CommentDto result = commentService.getCommentById(postId, commentId);

        assertNotNull(result);
        verify(modelMapper).map(comment1, CommentDto.class);
    }

    @Test
    void getCommentById_postNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 10L));
    }

    @Test
    void getCommentById_commentNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 10L));
    }

    @Test
    void getCommentById_commentBelongsToDifferentPost_throwsBlogAPIException() {
        long postId = 1L;
        long commentId = 10L;

        Comment commentFromOtherPost = new Comment();
        commentFromOtherPost.setId(commentId);
        commentFromOtherPost.setPost(post2);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentFromOtherPost));

        assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(postId, commentId));

        verify(modelMapper, never()).map(any(), eq(CommentDto.class));
    }

    // --------------------
    // updateComment
    // --------------------

    @Test
    void updateComment_success_updatesFields_saves_andReturnsDto() {
        long postId = 1L;
        long commentId = 10L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment1));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(modelMapper.map(any(Comment.class), eq(CommentDto.class))).thenReturn(commentDtoResp);

        CommentDto result = commentService.updateComment(postId, commentId, commentDtoReq);

        assertNotNull(result);

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        Comment saved = captor.getValue();

        assertEquals("newName", saved.getName());
        assertEquals("newEmail@test.com", saved.getEmail());
        assertEquals("newBody", saved.getBody());
        assertEquals(1L, saved.getPost().getId());
    }

    @Test
    void updateComment_postNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDtoReq));
    }

    @Test
    void updateComment_commentNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.updateComment(1L, 10L, commentDtoReq));
    }

    @Test
    void updateComment_commentBelongsToDifferentPost_throwsBlogAPIException() {
        Comment commentFromOtherPost = new Comment();
        commentFromOtherPost.setId(10L);
        commentFromOtherPost.setPost(post2);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(commentFromOtherPost));

        assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(1L, 10L, commentDtoReq));

        verify(commentRepository, never()).save(any());
    }

    // --------------------
    // deleteComment
    // --------------------

    @Test
    void deleteComment_success_deletesComment() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(comment1));

        assertDoesNotThrow(() -> commentService.deleteComment(1L, 10L));

        verify(commentRepository).delete(comment1);
    }

    @Test
    void deleteComment_postNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));

        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteComment_commentNotFound_throwsResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 10L));

        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteComment_commentBelongsToDifferentPost_throwsBlogAPIException() {
        Comment commentFromOtherPost = new Comment();
        commentFromOtherPost.setId(10L);
        commentFromOtherPost.setPost(post2);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(commentRepository.findById(10L)).thenReturn(Optional.of(commentFromOtherPost));

        assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(1L, 10L));

        verify(commentRepository, never()).delete(any());
    }

    // --------------------
    // static util
    // --------------------

    @Test
    void commentServiceMapperUtil_doesNotThrow_andMaps() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("abc");
        comment.setEmail("a@b.com");
        comment.setBody("hello");

        CommentDto dto = assertDoesNotThrow(() -> CommentServiceImpl.commentServiceMapperUtil(comment));
        assertNotNull(dto);
        // If your CommentDto has same fields, you can assert them too:
        // assertEquals("abc", dto.getName());
    }
}