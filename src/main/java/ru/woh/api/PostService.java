package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostModel> list(Integer page, Integer limit, Boolean includeNotAllowed) {
        return (
            includeNotAllowed
                ? this.postRepository.findAll(PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "createdAt")))
                : this.postRepository.findAllByIsAllowedTrue(PageRequest.of(
                    page,
                    limit,
                    new Sort(Sort.Direction.DESC, "createdAt")
                ))
        ).getContent();
    }

    public PostModel one(Long id) {
        return this.postRepository.findById(id).orElse(null);
    }

    public PostModel save(PostModel post) {
        return this.postRepository.save(post);
    }

    public void delete(PostModel post) {
        this.postRepository.delete(post);
    }
}
