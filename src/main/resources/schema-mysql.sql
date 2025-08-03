CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
                                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     conversation_id VARCHAR(255) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    INDEX idx_conversation_id (conversation_id)
    );