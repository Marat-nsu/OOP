package checker.engine;

import java.nio.file.Path;

record RepositoryCheckout(Path repoPath, String errorMessage) {}
