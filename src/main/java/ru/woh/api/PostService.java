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
                ? this.postRepository.findAll(new PageRequest(page, limit, new Sort(Sort.Direction.DESC, "createdAt")))
                : this.postRepository.findAllByIsAllowedTrue(new PageRequest(
                    page,
                    limit,
                    new Sort(Sort.Direction.DESC, "createdAt")
                ))
        ).getContent();
    }

    public PostModel one(Long id) {
        return this.postRepository.findOne(id);
    }

    public PostModel save(PostModel post) {
        return this.postRepository.save(post);
    }

    public void delete(Long id) {
        this.postRepository.delete(id);
    }
}
