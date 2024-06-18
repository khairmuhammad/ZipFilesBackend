# File Upload and Archive API

This project implements a REST API for uploading multiple files, archiving them into a single zip file, and returning the zip file to the user. Additionally, it tracks upload statistics such as the IP address and usage count per day.

## Design Choices

1. **Modularity**: The project is structured into controllers, services, repositories, models, and utility classes to maintain separation of concerns.
2. **Scalability**: The service layer handles business logic and can be extended to accommodate additional features without impacting other components.
3. **File Size Limitation**: Implemented a check to enforce the maximum file upload size of 1MB.
4. **Usage Statistics**: Stores the IP address and the usage count per day in an H2 in-memory database for simplicity.

## Future Improvements

1. **Multiple Archiving Methods**:
    - To support multiple archiving methods like 7z, we can introduce a strategy pattern where different compression algorithms are implemented as separate classes.
    - Example: Create an interface `ArchiveStrategy` with a method `archiveFiles(MultipartFile[] files)`.
    - Implement classes like `ZipArchiveStrategy` and `SevenZipArchiveStrategy` that implement this interface.
    - Use a factory or service locator pattern to select the appropriate strategy based on user input or configuration.

2. **Handling Increased Request Volume**:
    - Introduce a caching mechanism to store frequently accessed data.
    - Use asynchronous processing for file archiving to improve response times.
    - Implement rate limiting to prevent abuse and ensure fair usage.
    - Scale horizontally by deploying multiple instances of the application behind a load balancer.

3. **Support for Larger Files (1GB)**:
    - Switch to a file storage service like Amazon S3 for handling larger file uploads instead of in-memory processing.
    - Implement chunked file uploads to allow users to upload large files in smaller parts.
    - Increase the max file size limit in the configuration.

## Testing

Unit tests are provided to ensure the functionality of the controller and service layers. The tests cover:
- File size validation
- Archiving functionality
- Usage statistics tracking

## Running the Application

1. Build the project with Maven:
   ```bash
   mvn clean install

2. Run the application:
   ```bash
   mvn spring-boot:run

3. Access the H2 console at:
   ```bash
   http://localhost:8080/h2-console
   ```
   - JDBC URL: jdbc:h2:mem:testdb
   - Username: sa
   - Password: password