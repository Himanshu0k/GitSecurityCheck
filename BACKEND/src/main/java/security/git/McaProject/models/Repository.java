package security.git.McaProject.models;

import jakarta.persistence.*;

@Entity
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String githubRepoId;
}