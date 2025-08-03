package learn.spring_ai.jdbcchatmemoryrepository;

public class ChatRequest {
    private String conversationId;
    private String message;

    public ChatRequest() {
    }

    public ChatRequest(String userId, String message) {
        this.conversationId = userId;
        this.message = message;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
