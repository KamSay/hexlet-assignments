package exercise;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;
import lombok.Setter;

@SpringBootApplication
@RestController
public class Application {
    @Setter
    private static  List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(@RequestParam(defaultValue = "10") Optional<Integer> limit) {
        List<Post> result = posts.stream().limit(limit.orElse(10)).toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        Optional<Post> result = posts.stream().filter(post -> post.getId().equals(id)).findFirst();
        return ResponseEntity.of(result);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        posts.add(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post post) {
        var found = posts.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (found.isPresent()) {
            Post page = found.get();
            page.setId(post.getId());
            page.setTitle(post.getTitle());
            page.setBody(post.getBody());
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
