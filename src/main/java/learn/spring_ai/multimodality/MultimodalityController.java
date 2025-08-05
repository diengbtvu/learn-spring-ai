package learn.spring_ai.multimodality;
import learn.spring_ai.advisor.ReReadingAdvisor;
import learn.spring_ai.advisor.Response;
import learn.spring_ai.tools.DateTime;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController("/multimodality")
public class MultimodalityController {

    ChatClient chatClient;
    public MultimodalityController(ChatModel chatModel, ChatMemory chatMemory) {
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).order(3).build();
        this.chatClient = ChatClient.builder(chatModel)


                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new ReReadingAdvisor().withOrder(2),
                        memoryAdvisor

                )
                .defaultTools(
                        new DateTime()
                )
                .build();
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    private MimeType getMimeTypeFromExtension(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "png":
                return MimeTypeUtils.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MimeTypeUtils.IMAGE_JPEG;
            case "gif":
                return MimeTypeUtils.IMAGE_GIF;
            case "webp":
                return MimeType.valueOf("image/webp");
            case "bmp":
                return MimeType.valueOf("image/bmp");
            case "tiff":
            case "tif":
                return MimeType.valueOf("image/tiff");
            default:
                // Mặc định trả về PNG nếu không xác định được
                return MimeTypeUtils.IMAGE_PNG;
        }
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response uploadImage(@RequestPart("image") MultipartFile imageFile) throws IOException {
        // Lấy tên file gốc và phần mở rộng
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);

        // Tạo tên file duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Tạo Path object
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path destinationPath = uploadPath.resolve(uniqueFileName);
        imageFile.transferTo(destinationPath.toFile());

        // Xác định MIME type động
        MimeType mimeType = getMimeTypeFromExtension(fileExtension);

        // Gọi chatClient với MIME type phù hợp
        Response entity = this.chatClient.prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(mimeType, new FileSystemResource(destinationPath.toFile())))
                .call()
                .entity(Response.class);

        return entity;
    }


    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // No extension found
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase(); // Return extension in lowercase
    }
}

