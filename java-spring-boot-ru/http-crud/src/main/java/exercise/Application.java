package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts")
    public List<Post> getPosts(@RequestParam(defaultValue = "10") Optional<Integer> limit) {
        return posts.stream().limit(limit.orElse(10)).toList();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> getPost(@PathVariable Integer id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Integer id, @RequestBody Post post) {
        var found = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (found.isPresent()) {
            var page = found.get();
            page.setId(post.getId());
            page.setTitle(post.getTitle());
            page.setBody(post.getBody());
        }
        return post;
    }

    @DeleteMapping("posts/{id}")
    public void deletePost(@PathVariable Integer id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
