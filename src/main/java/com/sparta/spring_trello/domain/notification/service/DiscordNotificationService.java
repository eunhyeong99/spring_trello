package com.sparta.spring_trello.domain.notification.service;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.workspace.entity.Workspace;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiscordNotificationService {
    private final String discordWebhookUrl = "https://discord.com/api/webhooks/1296440711083720786/j4_uGipP_YnmgU7FogFosXJuxCS2s5YK0cHt1f6xSUYJC_6aOkVnS4KoopBBiwNqeRlt";

    private final RestTemplate restTemplate;

    public DiscordNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendDiscordNotification(String event, String messageContent) {
        // 메시지 작성
        String message = String.format(
                "**Event**: %s\n" +   // 이벤트 유형
                        "%s",                 // 메시지 내용
                event,                // 이벤트 이름
                messageContent        // 메시지에 포함할 세부 내용
        );

        // Discord에 보낼 메시지 구조 (JSON 형식)
        Map<String, String> payload = new HashMap<>();
        payload.put("content", message);

        // HTTP 요청 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(discordWebhookUrl, request, String.class);
    }
}
