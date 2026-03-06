package config;

import java.io.IOException;

public interface PizzeriaConfigSource {
    PizzeriaConfig load() throws IOException;
}